import { useState } from "react";
import "../styles/CreateTournament.css";
import TennisIcon from "../assets/icons/tennis.svg";
import PingPongIcon from "../assets/icons/pingpong.svg";
import BadmintonIcon from "../assets/icons/badminton.svg";
import TextField from "@mui/material/TextField";
import z from "zod";

declare global {
    interface Window {
        google: any;
    }
}

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
});

const CreateTournament: React.FC = () => {
    const [formData, setFormData] = useState({
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
        postalCode: ""
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

    const validateForm = () => {
        try {
            TournamentSchema.parse(formData);
            setErrors({});
            return true;
        } catch (e) {
            
            if (e instanceof z.ZodError) {
                const newErrors: Record<string, string> = {};
                e.errors.forEach(err => {
                    const field = err.path[0] as string;
                    newErrors[field] = err.message;
                });
                console.error(newErrors)
                setErrors(newErrors);
            }
            return false;
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        setIsLoading(true);


        const competitionData = {
            type: formData.type,
            matchDurationMinutes: timeToMinutes(formData.matchTime),
            availableCourts: Number(formData.courts),
            participantsLimit: Number(formData.participants),
            streetAddress: formData.streetAddress,
            city: formData.city,
            postalCode: formData.postalCode,
            registrationOpen: false
        };
        
        // Geokodowanie lokalizacji

        if (window.google) {
            const geocoder = new window.google.maps.Geocoder();

            geocoder.geocode({ address: formData.location }, (results: any, status: any) => {
                if (status === "OK" && results.length > 0) {
                    const place = results[0];
                    const components = place.address_components;
                    let city = '', postalCode = '', streetNumber = '', route = '';

                    components.forEach((component: any) => {
                        const types = component.types;
                        if (types.includes('locality')) city = component.long_name;
                        if (types.includes('postal_code')) postalCode = component.long_name;
                        if (types.includes('street_number')) streetNumber = component.long_name;
                        if (types.includes('route')) route = component.long_name;
                    });

                    const streetAddress = `${route} ${streetNumber}`.trim();

                    const fullData = {
                        ...competitionData,
                        streetAddress,
                        city,
                        postalCode,
                    };

                    submitTournamentData(fullData);
                } else {
                    setIsLoading(false);
                    alert("Nie udało się rozpoznać lokalizacji");
                }
            });
        } else {
            setIsLoading(false);
            alert("Google Maps API nie jest dostępne");
        }
    };

    const submitTournamentData = async (data: typeof formData) => {
        try {
            console.log(data);
            const response = await fetch("http://localhost:8080/api/competitions", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                setIsLoading(false);
                alert("Turniej został pomyślnie stworzony!");
                window.location.reload();
            } else {
                const error = await response.json();
                console.error("Błąd:", error);
                setIsLoading(false);
                alert("Wystąpił błąd przy tworzeniu turnieju.");
            }
        } catch (error) {
            console.error("Błąd połączenia:", error);
            setIsLoading(false);
            alert("Błąd połączenia z serwerem.");
        }
        
    };

    const timeToMinutes = (time: string): number => {
        const [hours, minutes] = time.split(":").map(Number);
        return hours * 60 + minutes;
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
                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                label="Nazwa"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                error={!!errors.name}
                                helperText={errors.name}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                type="date"
                                label="Od"
                                name="fromDate"
                                InputLabelProps={{ shrink: true }}
                                value={formData.fromDate}
                                onChange={handleChange}
                                error={!!errors.fromDate}
                                helperText={errors.fromDate}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                type="date"
                                label="Do"
                                name="toDate"
                                InputLabelProps={{ shrink: true }}
                                value={formData.toDate}
                                onChange={handleChange}
                                error={!!errors.toDate}
                                helperText={errors.toDate}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                label="Lokalizacja"
                                name="location"
                                value={formData.location}
                                onChange={handleChange}
                                error={!!errors.location}
                                helperText={errors.location}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                type="number"
                                label="Liczba boisk"
                                name="courts"
                                value={formData.courts}
                                onChange={handleChange}
                                error={!!errors.courts}
                                helperText={errors.courts}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                type="number"
                                label="Limit uczestników"
                                name="participants"
                                value={formData.participants}
                                onChange={handleChange}
                                error={!!errors.participants}
                                helperText={errors.participants}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <TextField
                                fullWidth
                                type="time"
                                label="Czas trwania meczu"
                                name="matchTime"
                                value={formData.matchTime}
                                onChange={handleChange}
                                InputLabelProps={{ shrink: true }}
                                error={!!errors.matchTime}
                                helperText={errors.matchTime}
                                required
                            />
                        </div>

                        <button className="create-tournament-button" type="submit">Utwórz</button>
                    </form>
                </div>
            </div>
        </div>
    </>
    );
};

export default CreateTournament;
