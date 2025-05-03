import { useState } from "react";
import Modal from "react-modal";
import { GoogleMap, Autocomplete } from '@react-google-maps/api';
import "../styles/CreateTournament.css";
import TennisIcon from "../assets/icons/tennis.svg";
import PingPongIcon from "../assets/icons/pingpong.svg";
import BadmintonIcon from "../assets/icons/badminton.svg";

// Ustawienie elementu root dla modala
Modal.setAppElement("#root");

const CreateTournament: React.FC = () => {
    const [formData, setFormData] = useState({
        typeId: 0, 
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

    const [autocomplete, setAutocomplete] = useState<any>(null);
    const [isLocationModalOpen, setLocationModalOpen] = useState(false);

    const onPlaceChanged = () => {
        if (autocomplete !== null) {
            const place = autocomplete.getPlace();
            if (place.formatted_address) {
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
    
                const fullStreetAddress = `${route} ${streetNumber}`.trim();
    
                setFormData(prev => ({
                    ...prev,
                    location: place.formatted_address,
                    streetAddress: fullStreetAddress,
                    city,
                    postalCode
                }));
    
                setLocationModalOpen(false);
            }
        }
    };
    

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleDisciplineSelect = (discipline: number) => {
        setFormData(prev => ({ ...prev, typeId: discipline }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // Tworzenie obiektu danych do wysłania
        const competitionData = {
            competitionId: null, // Brak ID, gdy tworzymy nowy
            type: formData.typeId,  // Przekazanie typu dyscypliny (np. "BADMINTON")
            matchDurationMinutes: timeToMinutes(formData.matchTime),
            availableCourts: Number(formData.courts),
            participantsLimit: Number(formData.participants),
            streetAddress: formData.streetAddress,
            city: formData.city,
            postalCode: formData.postalCode,
            registrationOpen: false // Domyślnie ustawiamy jako "false"
        };

        // Wysłanie zapytania POST do backendu
        try {
            const response = await fetch("/api/competitions", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(competitionData), // Przesyłanie danych w formacie JSON
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

                <div className="create-tournament-form">
                    <form onSubmit={handleSubmit}>
                        <div className="create-tournament-input-group">
                            <input
                                name="name"
                                type="text"
                                placeholder="Nazwa"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <input
                                name="fromDate"
                                type="date"
                                placeholder="dd.MM.yyyy"
                                onChange={handleChange}
                                value={formData.fromDate}
                                required
                            />
                        </div>
                        
                        <div className="create-tournament-input-group">
                            <input
                                name="toDate"
                                type="date"
                                placeholder="dd.MM.yyyy"
                                onChange={handleChange}
                                value={formData.toDate}
                                required
                            />
                        </div>
                        

                        <div
                            className="create-tournament-input-group"
                            onClick={() => setLocationModalOpen(true)}
                            style={{ cursor: "pointer" }}
                        >
                            <input
                                type="text"
                                placeholder="Lokalizacja"
                                value={formData.location}
                                readOnly
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <input
                                name="courts"
                                type="number"
                                placeholder="Liczba boisk"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <input
                                name="participants"
                                type="number"
                                placeholder="Limit uczestników"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="create-tournament-input-group">
                            <input
                                name="matchTime"
                                type="time"
                                placeholder="Czas trwania meczu"
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <button className="create-tournament-button" type="submit">Utwórz</button>
                    </form>
                </div>
            </div>

            {/* Modal do wyboru lokalizacji */}
            <Modal
                isOpen={isLocationModalOpen}
                onRequestClose={() => setLocationModalOpen(false)}
                contentLabel="Wybierz lokalizację"
                className="location-modal"
                overlayClassName="location-modal-overlay"
            >
                <h2>Wybierz lokalizację</h2>
                    <Autocomplete
                        onLoad={setAutocomplete}
                        onPlaceChanged={onPlaceChanged}
                    >
                        <input
                            type="text"
                            placeholder="Szukaj lokalizacji..."
                            style={{ width: "100%", padding: "0.5rem", marginBottom: "1rem" }}
                        />
                    </Autocomplete>

                    <div style={{ height: "300px", width: "100%", marginBottom: "1rem" }}>
                        <GoogleMap
                            mapContainerStyle={{ height: "100%", width: "100%" }}
                            center={{ lat: 50.06143, lng: 19.93658 }}
                            zoom={12}
                        />
                    </div>

                <button onClick={() => setLocationModalOpen(false)} style={{ marginTop: "1rem" }}>
                    Anuluj
                </button>
            </Modal>
        </div>
    );
};

export default CreateTournament;
