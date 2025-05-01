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
        discipline: "",
        name: "",
        date: "",
        location: "",
        courts: "",
        participants: "",
        matchTime: ""
    });

    const [autocomplete, setAutocomplete] = useState<any>(null);
    const [isLocationModalOpen, setLocationModalOpen] = useState(false);

    const onPlaceChanged = () => {
        if (autocomplete !== null) {
            const place = autocomplete.getPlace();
            if (place.formatted_address) {
                setFormData(prev => ({ ...prev, location: place.formatted_address }));
                setLocationModalOpen(false);
            }
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        console.log("Tworzenie turnieju:", formData);
    };

    const handleDisciplineSelect = (discipline: string) => {
        setFormData(prev => ({ ...prev, discipline }));
    };
    
    const disciplines = [
        { name: "tennis", src: TennisIcon, alt: "Baseball" },
        { name: "pingpong", src: PingPongIcon, alt: "Ping Pong" },
        { name: "badminton", src: BadmintonIcon, alt: "Badminton" },
    ];

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
                            className={`discipline-icon-button ${formData.discipline === name ? "selected" : ""}`}>
                            <img src={src} alt={alt} />
                        </button>
                    ))}
                </div>

                <div className="create-tournament-form">
                    <form onSubmit={e => { e.preventDefault(); handleSubmit(); }}>
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
                                name="date"
                                type="date"
                                placeholder="dd.MM.yyyy"
                                onChange={handleChange}
                                value={formData.date}
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
