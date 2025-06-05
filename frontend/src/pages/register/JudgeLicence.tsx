import React, { useState,useEffect } from "react";
import "../../styles/Auth.css";
import "../../styles/UserTypeChoser.css";
import tennisIcon from "../../assets/icons/tennis.svg";
import pingpongIcon from "../../assets/icons/pingpong.svg";
import badmintonIcon from "../../assets/icons/badminton.svg";
import { useNavigate } from "react-router-dom";
import { HTTP_ADDRESS } from "../../config.ts";



const JudgeLicence: React.FC = () => {
    const navigate = useNavigate();
    useEffect(() => {
            const registrationData = sessionStorage.getItem("registrationData");
            console.log("Registration Data:", registrationData);
            if (!registrationData) {
                navigate("/register");
            }
            else if (JSON.parse(registrationData).userType !== "Judge") {
                navigate("/register");
            }
        }, [navigate]);
    const [licences, setLicences] = useState([
        { licenceNumber: "", discipline: "TENNIS_OUTDOOR" },
    ]);

    const disciplineOptions = [
        { value: "TENNIS_OUTDOOR", label: "Tenis", icon: tennisIcon },
        { value: "TABLE_TENNIS", label: "Ping-pong", icon: pingpongIcon },
        { value: "BADMINTON", label: "Badminton", icon: badmintonIcon },
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

    const handleLicenceSubmit = async() => {
         const data = JSON.parse(sessionStorage.getItem("registrationData") || "{}");
            const judgeData = {
                firstName: data.firstName,
                lastName: data.lastName,
                email: data.email,
                password: data.password,
                city: "Kraków",
                phoneNumber: "123456789",
            }
            console.log("Judge Data:", judgeData);
            const response = await fetch(
                `${HTTP_ADDRESS}/api/referees`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(judgeData),
                },
            );
            if (!response.ok) {
                const errorData = await response.json();
                console.error("Error submitting judge data:", errorData);
                alert("Wystąpił błąd podczas zapisywania danych sędziego. Spróbuj ponownie.");
                return;
            }

            const responseData = await response.json();
            console.log("Created Referee:", responseData);

            const refereeId = responseData.userId

        for (const licence of licences) {
            const licenceData = {
                licenceType: licence.discipline,
                userId: refereeId,
                license: licence.licenceNumber,

            };
            console.log("Licence Data:", licenceData);
            const response = await fetch(
                `${HTTP_ADDRESS}/api/licences`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(licenceData),
                },
            );
            if (!response.ok) {
                const errorData = await response.json();
                console.error("Error submitting licences:", errorData);
                alert("Wystąpił błąd podczas zapisywania licencji. Spróbuj ponownie.");
                return;
            }
        }
        navigate("/register/information");
    }

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
                                    handleLicenceSubmit();
                                    navigate("/register/information");
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
