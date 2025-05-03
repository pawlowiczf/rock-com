import "../styles/EditMatch.css";
import { useState } from "react";
import Select, { SingleValue } from "react-select";

export const customSelectStyles = {
  control: (base: any) => ({
    ...base,
    width: "110%",
    border: "2px solid #ccc",
    borderRadius: "8px",
    minHeight: "45px",
    "&:hover": { borderColor: "#7b61ff" }
  }),
  option: (base: any, { isFocused, isSelected }: any) => ({
    ...base,
    backgroundColor: isSelected ? "#7b61ff" : isFocused ? "#f2f2f2" : "white",
    color: isSelected ? "white" : "black"
  }),
  singleValue: (base: any) => ({
    ...base,
    color: "#333"
  })
};
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
    date: "2025-06-10",
    location: "Kort 1",
    referee: null,
    player1: null,
    player2: null,
    score: "",
    protocol: ""
  });

  const isScoreValid = scorePattern.test(formData.score);

  const handleSubmit = () => {
    if (!scorePattern.test(formData.score)) {
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
            <input
              type="date"
              value={formData.date}
              onChange={(e) => setFormData({ ...formData, date: e.target.value })}
            />
          </div>

          <div className="edit-match-input-group">
            <input
              type="text"
              placeholder="Lokalizacja"
              value={formData.location}
              onChange={(e) => setFormData({ ...formData, location: e.target.value })}
            />
          </div>

           <div className="edit-match-input-group">
            <Select
              options={refereeOptions}
              placeholder="Wybierz sędziego"
              value={formData.referee}
              onChange={(selected) => setFormData({ ...formData, referee: selected })}
              styles={customSelectStyles}
              isSearchable
            />
          </div>
          
          <div className="edit-match-input-group">
            <Select
              options={playerOptions}
              placeholder="Gracz 1"
              value={formData.player1}
              onChange={(selected) => setFormData({ ...formData, player1: selected })}
              styles={customSelectStyles}
              isSearchable
            />
          </div>

          <div className="edit-match-input-group">
            <Select
              options={playerOptions}
              placeholder="Gracz 2"
              value={formData.player2}
              onChange={(selected) => setFormData({ ...formData, player2: selected })}
              styles={customSelectStyles}
              isSearchable
            />
          </div>

          <div className="edit-match-input-group">
            <input
              type="text"
              placeholder="Wynik (np. 6:4)"
              value={formData.score}
              onChange={(e) =>
                setFormData({ ...formData, score: e.target.value })
              }
              style={{ borderColor: formData.score && !isScoreValid ? 'red' : undefined }}
            />
            {!isScoreValid && formData.score && (
              <div style={{ color: 'red', fontSize: '0.8rem' }}>
                Nieprawidłowy format wyniku.
              </div>
            )}
          </div>

          <div className="edit-match-input-group">
                        <input type="file" name="licenceFile" accept=".pdf" required />
                    </div>
          <div className="edit-match-button-group">
          <button className="edit-match-button">ANULUJ</button>
          <button className="edit-match-button"
          onClick={handleSubmit}>
            POTWIEDŹ</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditMatch;
