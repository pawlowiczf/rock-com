import { useState } from "react";
import "../styles/CreateTournament.css";
import TennisIcon from "../assets/icons/tennis.svg";
import PingPongIcon from "../assets/icons/pingpong.svg";
import BadmintonIcon from "../assets/icons/badminton.svg";
import TextField from "@mui/material/TextField";
import z from "zod";

const TournamentSchema = z.object({
    type: z.string(),
    name: z.string().min(1, "Nazwa jest wymagana"),
    fromDate: z.string().min(1, "Data rozpoczęcia jest wymagana"),
    toDate: z.string().min(1, "Data zakończenia jest wymagana"),
    location: z.string().min(1, "Lokalizacja jest wymagana"),
    courts: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, {
        message: "Liczba boisk musi być większa od 0",
    }),
    participants: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, {
        message: "Limit uczestników musi być większy od 0",
    }),
    matchTime: z.string().min(1, "Czas trwania meczu jest wymagany"),
    streetAddress: z.string().optional(),
    city: z.string().optional(),
    postalCode: z.string().optional(),
}).refine((data) => new Date(data.toDate) > new Date(data.fromDate), {
    message: "Data zakończenia musi być po dacie rozpoczęcia",
    path: ["toDate"],
}).refine(
    (data) => new Date(data.fromDate) >= new Date(),
    "Data rozpoczęcia musi być w przyszłości",
);

type TournamentFormData = z.infer<typeof TournamentSchema>;

const timeToMinutes = (time: string): number => {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
};

const geocodeAddress = (address: string): Promise<any> => {
    return new Promise((resolve, reject) => {
        if (!window.google) {
            reject("Google Maps API nie jest dostępne");
            return;
        }

        const geocoder = new window.google.maps.Geocoder();
        geocoder.geocode({ address }, (results: any, status: any) => {
            if (status === "OK" && results.length > 0) {
                resolve(results[0]);
            } else {
                reject("Nie udało się rozpoznać lokalizacji");
            }
        });
    });
};

const CreateTournament: React.FC = () => {
    const [formData, setFormData] = useState<TournamentFormData>({
        type: "TENNIS_OUTDOOR",
        name: "",
        fromDate: "",
        toDate: "",
        location: "",
        courts: "",
        participants: "",
        matchTime: "",
        streetAddress: "",
        city: "",
        postalCode: "",
    });

    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleDisciplineSelect = (discipline: string) => {
        setFormData(prev => ({ ...prev, type: discipline }));
    };

    const validateForm = (): boolean => {
        const result = TournamentSchema.safeParse(formData);
        if (result.success) {
            setErrors({});
            return true;
        }
        const newErrors = Object.fromEntries(result.error.errors.map(err => [err.path[0], err.message]));
        setErrors(newErrors);
        return false;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        setIsLoading(true);

        try {
            const result = await geocodeAddress(formData.location);
            const components = result.address_components;
            let city = '', postalCode = '', streetNumber = '', route = '';

            components.forEach((component: any) => {
                const types = component.types;
                if (types.includes('locality')) city = component.long_name;
                if (types.includes('postal_code')) postalCode = component.long_name;
                if (types.includes('street_number')) streetNumber = component.long_name;
                if (types.includes('route')) route = component.long_name;
            });

            const streetAddress = `${route} ${streetNumber}`.trim();

            const competitionData = {
                type: formData.type,
                matchDurationMinutes: timeToMinutes(formData.matchTime),
                availableCourts: Number(formData.courts),
                participantsLimit: Number(formData.participants),
                streetAddress,
                city,
                postalCode,
                registrationOpen: true
            };

            await submitTournamentData(competitionData);
        } catch (err: any) {
            alert(err);
            setIsLoading(false);
        }
    };

    const submitTournamentData = async (data: any) => {
        try {
            // 1. Utwórz turniej
            const response = await fetch("http://localhost:8080/api/competitions", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            });
    
            if (!response.ok) {
                const error = await response.json();
                alert("Wystąpił błąd przy tworzeniu turnieju: " + (error.message || ""));
                setIsLoading(false);
                return;
            }
    
            // 2. Odbierz competitionId z odpowiedzi
            const createdCompetition = await response.json();
            const competitionId = createdCompetition.competitionId;
    
            // 3. Przygotuj dane dat turnieju
            const competitionDate = {
                competitionId,
                startTime: new Date(formData.fromDate).toISOString(),
                endTime: new Date(formData.toDate).toISOString()
            };
    
            // 4. Wyślij daty turnieju
            const dateResponse = await fetch("http://localhost:8080/api/competitions-dates", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify([competitionDate]),
            });
    
            if (!dateResponse.ok) {
                const error = await dateResponse.json();
                alert("Wystąpił błąd przy zapisie dat turnieju");
                console.error(error);
                setIsLoading(false);
                return;
            }
    
            // 5. Sukces
            alert("Turniej został pomyślnie stworzony!");
            window.location.reload();
    
        } catch (error) {
            alert("Błąd połączenia z serwerem.");
        } finally {
            setIsLoading(false);
        }
    };
    

    const disciplines = [
        { name: "TENNIS_OUTDOOR", src: TennisIcon, alt: "Tennis" },
        { name: "TABLE_TENNIS", src: PingPongIcon, alt: "Ping Pong" },
        { name: "BADMINTON", src: BadmintonIcon, alt: "Badminton" },
    ];

    return (
        <>
            {isLoading && (
                <div className="spinner-overlay">
                    <div className="spinner"></div>
                </div>
            )}
            <div className="create-tournament-container">
                <div className="create-tournament-window">
                    <h2 className="create-tournament-header">Utwórz turniej</h2>
                    <div className="edit-tournament-scroll-pane">
                        <div className="discipline-icons">
                            {disciplines.map(({ name, src, alt }) => (
                                <button
                                    key={name}
                                    type="button"
                                    onClick={() => handleDisciplineSelect(name)}
                                    className={`discipline-icon-button ${formData.type === name ? "selected" : ""}`}
                                >
                                    <img src={src} alt={alt} />
                                </button>
                            ))}
                        </div>

                        <form className="create-tournament-form" onSubmit={handleSubmit}>
                            {[
                                { label: "Nazwa", name: "name" },
                                { label: "Od", name: "fromDate", type: "date" },
                                { label: "Do", name: "toDate", type: "date" },
                                { label: "Lokalizacja", name: "location" },
                                { label: "Liczba boisk", name: "courts", type: "number" },
                                { label: "Limit uczestników", name: "participants", type: "number" },
                                { label: "Czas trwania meczu", name: "matchTime", type: "time" },
                            ].map(({ label, name, type = "text" }) => (
                                <div className="create-tournament-input-group" key={name}>
                                    <TextField
                                        fullWidth
                                        label={label}
                                        name={name}
                                        type={type}
                                        value={(formData as any)[name]}
                                        onChange={handleChange}
                                        error={!!errors[name]}
                                        helperText={errors[name]}
                                        InputLabelProps={type === "date" || type === "time" ? { shrink: true } : undefined}
                                        required
                                    />
                                </div>
                            ))}
                            <button
                                className="create-tournament-button"
                                type="submit"
                                disabled={isLoading}
                            >
                                Utwórz
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </>
    );
};

export default CreateTournament;
