import "../styles/EditMatch.css";
import { useState } from "react";
import Select from "react-select";
import TextField from "@mui/material/TextField";

export const customSelectStyles = {
  control: (base: any, { isFocused }: any) => ({
    ...base,
    width: "100%",
    border: isFocused ? "2px solid #A020F0" : "1px solid #ccc",
    textAlign: "left",
    borderRadius: "8px",
    boxShadow: "none",
    transition: "none",
    minHeight: "45px",
    "&:hover": { 
      borderColor: isFocused ? "#A020F0" : "black" 
    },
  }),
  option: (base: any, { isFocused, isSelected,  }: any) => ({
    ...base,
    backgroundColor: isSelected ? "#A020F0" : isFocused ? "#f2f2f2" : "white",
    color: isSelected ? "white" : "black",
    "&:active": { 
      backgroundColor: "#D1B3FF"
    },
    
  }),
  singleValue: (base: any) => ({
    ...base,
    color: "#333"
  }),
  menu: (base: any) => ({
    ...base,
    zIndex: 3,
  }),
};

export const selectLabelStyle = {
  fontSize: "0.8rem",
  color: "#333",
  marginLeft: "0.8rem", 
  marginTop: "-0.5rem", 
  textAlign: "left",
  display: "block",

}

const scorePattern = /^\d+:\d+$/;

const refereeOptions = [
  { value: "Ref1", label: "Jan Kowalski" },
  { value: "Ref2", label: "Anna Nowak" },
  { value: "Ref3", label: "Piotr Wiśniewski" },
];

const playerOptions = [
  { value: "P1", label: "Kamil Stoch" },
  { value: "P2", label: "Robert Lewandowski" },
  { value: "P3", label: "Iga Świątek" },
];

type OptionType = { value: string; label: string };

const EditMatch: React.FC = () => {
  const [formData, setFormData] = useState<{
    date: string;
    location: string;
    referee: OptionType | null;
    player1: OptionType | null;
    player2: OptionType | null;
    score: string;
    protocol: string;
  }>({
    date: "2025-06-10T12:12",
    location: "Kort 1",
    referee: null,
    player1: null,
    player2: null,
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
              InputLabelProps={{ shrink: true }}
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
            <label
              htmlFor="referee"
              style={selectLabelStyle}>
              Sędzia
            </label>
            <Select
              id="referee"
              options={refereeOptions}
              placeholder="Sędzia"
              value={formData.referee}
              onChange={(selected) => setFormData({ ...formData, referee: selected })}
              styles={customSelectStyles}
              isSearchable
            />
          </div>

          <div className="edit-match-input-group">
          <label
              htmlFor="player1"
              style={selectLabelStyle}>
              Gracz 1
            </label>
            <Select
              id="player1"
              options={playerOptions}
              placeholder="Gracz 1"
              value={formData.player1}
              onChange={(selected) => setFormData({ ...formData, player1: selected })}
              styles={customSelectStyles}
              isSearchable
            />
          </div>

          <div className="edit-match-input-group">
          <label
              htmlFor="player2"
              style={selectLabelStyle}>
              Gracz 2
            </label>
            <Select
              id="player2"
              options={playerOptions}
              placeholder="Gracz 2"
              value={formData.player2}
              onChange={(selected) => setFormData({ ...formData, player2: selected })}
              styles={customSelectStyles}
              isSearchable
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
          <label
              htmlFor="protocol"
              style={selectLabelStyle}>
              Protokół
            </label>
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
