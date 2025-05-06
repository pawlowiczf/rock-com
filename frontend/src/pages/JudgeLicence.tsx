import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";
import { useState } from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const JudgeLicence: React.FC = () => {
    const navigate = useNavigate();
    const [licenceFile, setLicenceFile] = useState<File | null>(null);
    const [licenceNumber, setLicenceNumber] = useState<string>("");
    const [error, setError] = useState<string>("");

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0] || null;
        if (file) {
            setLicenceFile(file);
        }
    }

    const handleLicenceNumberChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setLicenceNumber(event.target.value);
    }

    const handleBackButtonClick = () => {
    }

    const finishRegister = () => {
        const prevData = JSON.parse(
            sessionStorage.getItem("registrationData") || "{}",
        );
        const updatedData = { ...prevData, licenceNumber };
        sessionStorage.setItem("registrationData", JSON.stringify(updatedData));

        submitRegistration(updatedData);
    };

    interface JudgeRegistrationData {
        firstName: string;
        lastName: string;
        email: string;
        birthdate: string;
        licenceNumber: string;
    }

    const submitRegistration = async (data: JudgeRegistrationData) => {
        try {
            const participantData = {
                firstName: data.firstName,
                lastName: data.lastName,
                email: data.email,
                city: "Kraków",
                phoneNumber: "123456789",
                birthDate: data.birthdate,
                licenceNumber: data.licenceNumber,
            };
            console.log("Participant data:", participantData);
            const response = await fetch(
                "http://localhost:8080/api/referee",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(participantData),
                },
            );
            const licenceResponse = await fetch(
                "http://localhost:8080/api/referee/licence",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        licenceNumber: data.licenceNumber,
                        licenceFile: licenceFile,
                    }),
                },
            );
        
            if (!response.ok) {
                throw new Error("Failed to register participant");
            }
            if (!licenceResponse.ok) {
                throw new Error("Failed to upload licence file");
            }
            navigate("/register/information");
        } catch (error) {
            console.error("Error during registration:", error);
            setError(
                "Wystąpił błąd podczas rejestracji. Proszę spróbować ponownie.",
            );
        }
    };



    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Załącz licencje sędziego</h3>
                <div className="auth-form">
                    <form>
                        <div className="auth-input-group">
                            <input
                                type="text"
                                name="licenceNumber"
                                placeholder="Numer licencji"
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="file"
                                name="licenceFile"
                                accept=".png, .jpg, .jpeg, .bmp"
                                required
                            />
                        </div>
                        <div className="auth-user-type-button-group">
                           <button
                            type="button"
                            className="auth-button"
                            style={{ width: "35%" }}
                            onClick={handleBackButtonClick}
                            >
                                Wstecz
                            </button>
                            <button
                                type="button"
                                className="auth-button"
                                style={{ width: "35%" }}
                                onClick={() => {
                                    finishRegister();
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
