import "../styles/EditTournament.css";
import { useState } from "react";
import { useParams } from "react-router-dom";
import DeleteIcon from "../assets/icons/cross.svg";
import TextField from '@mui/material/TextField';
import z from "zod";

const TournamentSchema = z.object({
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
    streetAddress: z.string().optional(),
    city: z.string().optional(),
    postalCode: z.string().optional(),
}).refine(data => new Date(data.toDate) > new Date(data.fromDate), {
    message: "Data zakończenia musi być po dacie rozpoczęcia",
    path: ["toDate"]
});

declare global {
    interface Window {
        google: any;
    }
}

const EditTournament: React.FC = () => {
    const { id } = useParams<{ id: string }>();

    console.log("Tournament ID:", id);

    const [formData, setFormData] = useState({
        name: "Wielki Tenis",
        fromDate: "2025-12-12",
        toDate: "2025-12-14",
        location: "Stadion Wisły",
        courts: "12",
        participants: "12",
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

    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isLoading, setIsLoading] = useState(false);

    const removeParticipant = (index: number) => {
        setParticipants(prev => prev.filter((_, i) => i !== index));
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
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
                setErrors(newErrors);
            }
            return false;
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        setIsLoading(true);

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

                    submitTournamentData({
                        ...formData,
                        streetAddress,
                        city,
                        postalCode
                    });
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
        const competitionData = {
            competitionId: null,
            name: data.name,
            fromDate: data.fromDate,
            toDate: data.toDate,
            location: data.location,
            availableCourts: Number(data.courts),
            participantsLimit: Number(data.participants),
            streetAddress: data.streetAddress,
            city: data.city,
            postalCode: data.postalCode,
            registrationOpen: false
        };

        try {
            const response = await fetch("http://localhost:8080/api/competitions/" + id , {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(competitionData),
            });

            if (response.ok) {
              setIsLoading(false);
              alert("Turniej został pomyślnie zaktualizowany!");
              window.location.reload();
          } else {
              const error = await response.json();
              console.error("Błąd:", error);
              setIsLoading(false);
              alert("Wystąpił błąd przy aktualizacji turnieju.");
          }
        } catch (error) {
            console.error("Błąd połączenia:", error);
            setIsLoading(false);
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
                value={formData.courts}
                onChange={(e) => setFormData({ ...formData, courts: e.target.value })}
              />
            </div>

            <div className="edit-tournament-input-group">
              <TextField
                fullWidth
                label="Limit uczestników"
                type="number"
                value={formData.participants}
                onChange={(e) => setFormData({ ...formData, participants: e.target.value })}
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
