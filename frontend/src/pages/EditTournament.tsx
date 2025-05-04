import "../styles/EditTournament.css";
import { useState } from "react";
import DeleteIcon from "../assets/icons/cross.svg";
import TextField from '@mui/material/TextField';

const EditTournament: React.FC = () => {
  const [formData, setFormData] = useState({
    name: "Wielki Tenis",
    fromDate: "2025-12-12",
    toDate: "2025-12-14",
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

const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // Geokodowanie lokalizacji
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
                    console.log(streetAddress);
                    console.log(city);
                    console.log(postalCode);


                    // Ustawiamy dane o lokalizacji
                    setFormData(prev => ({
                        ...prev,
                        streetAddress: streetAddress,
                        city,
                        postalCode
                    }));

                    // Przesyłamy dane do backendu
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
            availableCourts: Number(data.fields),
            participantsLimit: Number(data.participantLimit),
            streetAddress: data.streetAddress,
            city: data.city,
            postalCode: data.postalCode,
            registrationOpen: false
        };

        try {
            console.log(competitionData)
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

  return (
    <div className="edit-tournament-container">
      <div className="edit-tournament-window">
        <h3 className="edit-tournament-header">Edytuj turniej</h3>
        <div className="edit-tournament-scroll-pane">
          <div className="edit-tournament-form">
            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Nazwa"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              />
            </div>

            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Od"
                type="date"
                InputLabelProps={{ shrink: true }}
                value={formData.fromDate}
                onChange={(e) => setFormData({ ...formData, fromDate: e.target.value })}
              />
            </div>

            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Do"
                type="date"
                InputLabelProps={{ shrink: true }}
                value={formData.toDate}
                onChange={(e) => setFormData({ ...formData, toDate: e.target.value })}
              />
            </div>

            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Lokalizacja"
                value={formData.location}
                onChange={(e) => setFormData({ ...formData, location: e.target.value })}
              />
            </div>

            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Liczba boisk"
                type="number"
                value={formData.fields}
                onChange={(e) => setFormData({ ...formData, fields: e.target.value })}
              />
            </div>

            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Limit uczestników"
                type="number"
                value={formData.participantLimit}
                onChange={(e) => setFormData({ ...formData, participantLimit: e.target.value })}
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
          <button className="edit-tournament-button accept" onClick={handleSubmit}>
            AKCEPTUJ
          </button>
          <button className="edit-tournament-button start">
            ROZPOCZNIJ
          </button>
        </div>
      </div>
    </div>
  );
};

export default EditTournament;
