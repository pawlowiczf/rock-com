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
        console.log("licenceFile", file);
    }

    const handleLicenceNumberChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setLicenceNumber(event.target.value);
        console.log("licenceNumber", event.target.value);
    }

    const handleBackButtonClick = () => {
        const prevData = JSON.parse(
            sessionStorage.getItem("registrationData") || "{}",
        );
        sessionStorage.setItem("registrationData", JSON.stringify(prevData));
        navigate("/register/personal-data");
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
        userId: string;
        licenceNumber: string;
    }

    const submitRegistration = async (data: JudgeRegistrationData) => {
        try {
            const registrationData = sessionStorage.getItem("registrationData");
            if (!registrationData) {
                navigate("/register");
                return;
            }
            const parsedData = JSON.parse(registrationData);
            console.log("Parsed data:", parsedData);
            
            const response = await fetch(
                "http://localhost:8080/api/referee",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        userId: parsedData.user_id.userId,
                    }),
                },
            );

            const formData = new FormData();
            formData.append("licenceNumber", data.licenceNumber);
            if (licenceFile) {
            formData.append("licenceFile", licenceFile);
            }

            const licenceResponse = await fetch(
                "http://localhost:8080/api/referee/licence",
                {
                    method: "POST",
                    body: formData,
                }
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
                                value={licenceNumber}
                                onChange={handleLicenceNumberChange}
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="file"
                                name="licenceFile"
                                accept=".png, .jpg, .jpeg, .bmp"
                                onChange={handleFileChange}
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
