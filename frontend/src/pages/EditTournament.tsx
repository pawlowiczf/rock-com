import "../styles/EditTournament.css";
import { useState } from "react";
import Modal from "react-modal";
import { GoogleMap, Autocomplete } from '@react-google-maps/api';
import DeleteIcon from "../assets/icons/cross.svg";

const EditTournament: React.FC = () => {
  const [formData, setFormData] = useState({
    name: "Wielki Tenis",
    date: "2025-12-12",
    location: "Stadion Wisły",
    fields: "12",
    participantLimit: "12",
    streetAddress: "",
    city: "",
    postalCode: ""
  });

  const [participants, setParticipants] = useState([
    "Piotr Budynek, M, 34l.",
    "Piotr Budynek, M, 34l.",
    "Piotr Budynek, M, 34l.",
    "Piotr Budynek, M, 34l.",
    "Piotr Budynek, M, 34l.",
  ]);

  const removeParticipant = (index: number) => {
    setParticipants(prev => prev.filter((_, i) => i !== index));
  };

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
  return (
    <div className="edit-tournament-container">
      <div className="edit-tournament-window">
        <h3 className="edit-tournament-header">Edytuj turniej</h3>
        <div className="edit-tournament-scroll-pane">
            <div className="edit-tournament-form">
                <div className="edit-tournament-input-group">
                    <input
                    type="text"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    placeholder="Nazwa"
                    />
                </div>
                <div className="edit-tournament-input-group icon-input">
                    <input
                    type="date"
                    value={formData.date}
                    onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                    />
                </div>

                <div
                    className="edit-tournament-input-group icon-input"
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

                <div className="edit-tournament-input-group">
                    <input
                    type="number"
                    value={formData.fields}
                    onChange={(e) => setFormData({ ...formData, fields: e.target.value })}
                    placeholder="Liczba boisk"
                    
                    />
                </div>
                <div className="edit-tournament-input-group">
                    <input
                    type="number"
                    value={formData.participantLimit}
                    onChange={(e) => setFormData({ ...formData, participantLimit: e.target.value })}
                    placeholder="Limit uczestników"
            
                    />
                </div>
                <div className="edit-tournament-participants-header">
                    Lista uczestników
                </div>
                {participants.map((p, i) => (
                    <div key={i} className="edit-tournament-participant-item">
                    <span>{p}</span>
                    <img
                    src={DeleteIcon}
                    alt="Usuń"
                    className="participant-remove-icon"
                    onClick={() => removeParticipant(i)}
                    />
                    </div>
                ))}
                </div>
            </div>
            <div className="edit-tournament-button-group">
                <button className="edit-tournament-button accept">AKCEPTUJ</button>
                <button className="edit-tournament-button start">ROZPOCZNIJ</button>
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

export default EditTournament;
