import "../styles/EditMatch.css";
import { useState } from "react";
import TextField from "@mui/material/TextField";

const scorePattern = /^\d+:\d+$/;

const EditMatch: React.FC = () => {
  const [formData, setFormData] = useState<{
    date: string;
    location: string;
    referee: string;
    player1: string;
    player2: string;
    score: string;
    protocol: string;
  }>({
    date: "2025-06-10T12:12",
    location: "Kort 1",
    referee: "",
    player1: "",
    player2: "",
    score: "",
    protocol: ""
  });

  const isScoreValid = scorePattern.test(formData.score);

  const handleSubmit = () => {
    if (!isScoreValid) {
      alert("Wynik musi być w formacie liczba:liczba, np. 6:4");
      return;
    }
    console.log("Formularz poprawny:", formData);
  };

  return (
    <div className="edit-match-container">
      <div className="edit-match-window">
        <h3 className="edit-match-header">Edytuj mecz</h3>
        <div className="edit-match-form">

          <div className="edit-match-input-group">
            <TextField
              type="datetime-local"
              value={formData.date}
              onChange={(e) => setFormData({ ...formData, date: e.target.value })}
              fullWidth
              size="small"
              label="Data i godzina"
            />
          </div>

          <div className="edit-match-input-group">
            <TextField
              label="Lokalizacja"
              value={formData.location}
              onChange={(e) => setFormData({ ...formData, location: e.target.value })}
              fullWidth
              size="small"
            />
          </div>


          <div className="edit-match-input-group">
            <TextField
              type="text"
              value={formData.referee}
              onChange={(e) => setFormData({ ...formData, referee: e.target.value })}
              fullWidth
              size="small"
              label="Sędzia"
            />
          </div>

          <div className="edit-match-input-group">
            <TextField
              type="text"
              value={formData.player1}
              onChange={(e) => setFormData({ ...formData, player1: e.target.value })}
              fullWidth
              size="small"
              label="Gracz 1"
            />
          </div>

          <div className="edit-match-input-group">
            <TextField
              type="text"
              value={formData.player2}
              onChange={(e) => setFormData({ ...formData, player2: e.target.value })}
              fullWidth
              size="small"
              label="Gracz 1"
            />
          </div>

          <div className="edit-match-input-group">
            <TextField
              label="Wynik (np. 6:4)"
              value={formData.score}
              onChange={(e) => setFormData({ ...formData, score: e.target.value })}
              error={!!formData.score && !isScoreValid}
              helperText={formData.score && !isScoreValid ? "Nieprawidłowy format wyniku" : ""}
              fullWidth
              size="small"
            />
          </div>
          <div className="edit-match-input-group">
              <div id="protocol" className="edit-match-input-item">
            <input type="file" name="licenceFile" accept=".pdf" required />
            </div>
          </div>

          <div className="edit-match-button-group">
            <button className="edit-match-button">ANULUJ</button>
            <button className="edit-match-button" onClick={handleSubmit}>
              POTWIEDŹ
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditMatch;
