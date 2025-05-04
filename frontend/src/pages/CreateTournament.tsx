import { useState } from "react";
import "../styles/CreateTournament.css";
import TennisIcon from "../assets/icons/tennis.svg";
import PingPongIcon from "../assets/icons/pingpong.svg";
import BadmintonIcon from "../assets/icons/badminton.svg";
import TextField from "@mui/material/TextField";

declare global {
    interface Window {
        google: any;
    }
}

const CreateTournament: React.FC = () => {
    const [formData, setFormData] = useState({
        typeId: 1,
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

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleDisciplineSelect = (discipline: number) => {
        setFormData(prev => ({ ...prev, typeId: discipline }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (window.google) {
            const geocoder = new window.google.maps.Geocoder();

            geocoder.geocode({ address: formData.location }, (results: any, status: any) => {
                if (status === "OK" && results.length > 0) {
                    const place = results[0];
                    const components = place.address_components;
                    let city = '';
                    let postalCode = '';
                    let streetNumber = '';
                    let route = '';

                    components.forEach((component: any) => {
                        const types = component.types;
                        if (types.includes('locality')) {
                            city = component.long_name;
                        }
                        if (types.includes('postal_code')) {
                            postalCode = component.long_name;
                        }
                        if (types.includes('street_number')) {
                            streetNumber = component.long_name;
                        }
                        if (types.includes('route')) {
                            route = component.long_name;
                        }
                    });

                    const streetAddress = `${route} ${streetNumber}`.trim();

                    setFormData(prev => ({
                        ...prev,
                        streetAddress,
                        city,
                        postalCode
                    }));

                    submitTournamentData({
                        ...formData,
                        streetAddress,
                        city,
                        postalCode
                    });
                } else {
                    alert("Nie udało się rozpoznać lokalizacji");
                }
            });
        } else {
            alert("Google Maps API nie jest dostępne");
        }
    };

    const submitTournamentData = async (data: typeof formData) => {
        const competitionData = {
            competitionId: null,
            type: data.typeId,
            matchDurationMinutes: timeToMinutes(data.matchTime),
            availableCourts: Number(data.courts),
            participantsLimit: Number(data.participants),
            streetAddress: data.streetAddress,
            city: data.city,
            postalCode: data.postalCode,
            registrationOpen: false
        };

        try {
            const response = await fetch("/api/competitions", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(competitionData),
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Turniej stworzony", result);
                alert("Turniej został pomyślnie stworzony!");
            } else {
                const error = await response.json();
                console.error("Błąd przy tworzeniu turnieju", error);
                alert("Wystąpił błąd przy tworzeniu turnieju.");
            }
        } catch (error) {
            console.error("Błąd połączenia", error);
            alert("Błąd połączenia z serwerem.");
        }
    };

    const disciplines = [
        { name: 1, src: TennisIcon, alt: "Tennis" },
        { name: 2, src: PingPongIcon, alt: "Ping Pong" },
        { name: 3, src: BadmintonIcon, alt: "Badminton" },
    ];

    const timeToMinutes = (time: string): number => {
        const [hours, minutes] = time.split(":").map(Number);
        return hours * 60 + minutes;
    };

    return (
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
                            className={`discipline-icon-button ${formData.typeId === name ? "selected" : ""}`}>
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
                            required
                        />
                    </div>

                    <button className="create-tournament-button" type="submit">Utwórz</button>
                </form>
            </div>
            </div>
        </div>
    );
};

export default CreateTournament;
