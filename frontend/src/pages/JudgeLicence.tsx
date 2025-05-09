import React, { useState } from "react";
import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";
import tennisIcon from "../assets/icons/tennis.svg";
import pingpongIcon from "../assets/icons/pingpong.svg";
import badmintonIcon from "../assets/icons/badminton.svg";
import { useNavigate } from "react-router-dom";

const JudgeLicence: React.FC = () => {
    const navigate = useNavigate();
    const somefunction = () => {
        console.log("Button clicked!");
    };
    const [licences, setLicences] = useState([
        { licenceNumber: "", discipline: "tenis" },
    ]);

    const disciplineOptions = [
        { value: "tenis", label: "Tenis", icon: tennisIcon },
        { value: "pingpong", label: "Ping-pong", icon: pingpongIcon },
        { value: "badminton", label: "Badminton", icon: badmintonIcon },
    ];

    const updateLicence = (index: number, field: string, value: string) => {
        const updated = [...licences];
        updated[index] = { ...updated[index], [field]: value };
        setLicences(updated);
    };

    const addLicence = () => {
        setLicences([...licences, { licenceNumber: "", discipline: "tenis" }]);
    };

    const removeLicence = (index: number) => {
        setLicences(licences.filter((_, i) => i !== index));
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Załącz licencje sędziego</h3>
                <div className="auth-form">
                    <form>
                        {licences.map((licence, index) => (
                            <div
                                key={index}
                                className="auth-input-group"
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: "10px",
                                    marginBottom: "10px",
                                }}
                            >
                                <div
                                    className="discipline-buttons"
                                    style={{ display: "flex", gap: "15px" }}
                                >
                                    {disciplineOptions.map((option) => (
                                        <button
                                            key={option.value}
                                            type="button"
                                            onClick={() =>
                                                updateLicence(
                                                    index,
                                                    "discipline",
                                                    option.value,
                                                )
                                            }
                                            className={`discipline-button ${licence.discipline === option.value ? "selected" : ""}`}
                                        >
                                            <img
                                                src={option.icon}
                                                alt={option.label}
                                                style={{
                                                    width: 24,
                                                    height: 24,
                                                }}
                                            />
                                        </button>
                                    ))}
                                </div>

                                <input
                                    type="text"
                                    placeholder="Numer licencji"
                                    required
                                    value={licence.licenceNumber}
                                    onChange={(e) =>
                                        updateLicence(
                                            index,
                                            "licenceNumber",
                                            e.target.value,
                                        )
                                    }
                                />

                                {index > 0 && (
                                    <button
                                        type="button"
                                        className="delete-button"
                                        onClick={() => removeLicence(index)}
                                    >
                                        ✕
                                    </button>
                                )}
                            </div>
                        ))}

                        <button
                            type="button"
                            className="add-button"
                            onClick={addLicence}
                        >
                            ➕ Dodaj licencję
                        </button>

                        <div className="auth-user-type-button-group">
                            <button
                                type="button"
                                className="auth-button"
                                style={{ width: "35%" }}
                                onClick={() => {
                                    navigate("/registration/chose-user-type");
                                }}
                            >
                                Wstecz
                            </button>
                            <button
                                type="button"
                                className="auth-button"
                                style={{ width: "35%" }}
                                onClick={() => {
                                    navigate(
                                        "/register/information"
                                    );
                                }}
                            >
                                Dalej
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default JudgeLicence;
