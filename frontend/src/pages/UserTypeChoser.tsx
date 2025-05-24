import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";
import { useState } from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { HTTP_ADDRESS } from '../config.ts';

type UserType = "Participant" | "Judge" | "Organizer" | "";

const UserTypeChoser: React.FC = () => {
    const navigate = useNavigate();
    const [userType, setUserType] = useState<UserType>("");
    const [error, setError] = useState<string>("");

    useEffect(() => {
        const registrationData = sessionStorage.getItem("registrationData");
        if (!registrationData) {
            navigate("/register");
        }
    }, [navigate]);

    const handleButtonClick = (type: UserType) => {
        setUserType(type);
        setError("");
        console.log(`User type selected: ${type}`);
    };

    const handleBackButtonClick = () => {
        navigate("/register/account-creating");
    };

    const finishRegister = () => {
        console.log(userType);
        if (userType === "") {
            setError("Wybierz typ użytkownika!");
            return;
        }
        const prevData = JSON.parse(
            sessionStorage.getItem("registrationData") || "{}",
        );
        const updatedData = { ...prevData, userType };
        sessionStorage.setItem("registrationData", JSON.stringify(updatedData));

        if (userType === "Judge") {
            navigate("/register/judge");
        } else {
            submitRegistration(updatedData);
        }
    };

    interface RegistrationData {
        userType: UserType;
        firstName: string;
        lastName: string;
        email: string;
        password: string;
        birthdate: string;
    }

    const submitRegistration = async (data: RegistrationData) => {
        try {
            if (data.userType === "Participant") {
                const participantData = {
                    firstName: data.firstName,
                    lastName: data.lastName,
                    email: data.email,
                    password: data.password,
                    city: "Kraków",
                    phoneNumber: "123456789",
                    birthDate: data.birthdate,
                };

                const response = await fetch(
                    `${HTTP_ADDRESS}/api/participants`,

                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(participantData),
                    },
                );
                navigate("/login");
                if (!response.ok) {
                    throw new Error("Failed to register participant");
                }
            }
            else{
                navigate("/register/information");
            }
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
                <h3 className="auth-header">Wybierz typ użytkownika</h3>
                <div className="auth-form">
                    <div className="auth-input-group" style={{ width: "50%" }}>
                        <button
                            onClick={() => handleButtonClick("Participant")}
                            className={`auth-button ${userType !== "Participant" ? "selected-button" : ""}`}
                        >
                            Uczestnik
                        </button>
                    </div>
                    <div className="auth-input-group" style={{ width: "50%" }}>
                        <button
                            onClick={() => handleButtonClick("Judge")}
                            className={`auth-button ${userType !== "Judge" ? "selected-button" : ""}`}
                        >
                            Sędzia
                        </button>
                    </div>
                    <div className="auth-input-group" style={{ width: "50%" }}>
                        <button
                            onClick={() => handleButtonClick("Organizer")}
                            className={`auth-button ${userType !== "Organizer" ? "selected-button" : ""}`}
                        >
                            Organizator
                        </button>
                    </div>
                    {error && <p className="auth-error">{error}</p>}
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
                </div>
            </div>
        </div>
    );
};

export default UserTypeChoser;
