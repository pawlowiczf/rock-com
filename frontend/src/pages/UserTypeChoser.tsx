import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";
import { useState } from "react";



const UserTypeChoser: React.FC = () => {

    const [userType, setUserType] = useState<string>("");

    const handleButtonClick = (type: string) => {
        setUserType(type);
        console.log(`User type selected: ${type}`);
    }

    const finishRegister = () => {
        console.log(userType);
        if (userType === "") {
            alert("Wybierz typ użytkownika!");
        }
        else if (userType === "Participant") {
            window.location.href = "/register/information";
        }
        else if (userType === "Judge") {
            window.location.href = "/register/judge";
        }
        else if (userType === "Organizer") {
            window.location.href = "/register/information";
        }
    }

    const somefunction = () => {
        console.log("Button clicked!");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Wybierz typ użytkownika</h3>
                <div className="auth-form">
                        <div className="auth-input-group" style={{width: "50%"}}>
                            <button
                                onClick={() => handleButtonClick("Participant")} 
                                className={`auth-button ${userType === "Participant" ? "selected-button" : ""}`}>
                                    Uczestnik
                                </button> 
                        </div>
                        <div className="auth-input-group" style={{width: "50%"}}>
                            <button
                                onClick={() => handleButtonClick("Judge")}
                                className={`auth-button ${userType === "Judge" ? "selected-button" : ""}`}>
                                    Sędzia
                            </button> 
                        </div>
                        <div className="auth-input-group" style={{width: "50%"}}>
                            <button 
                                onClick={() => handleButtonClick("Organizer")}
                                className={`auth-button ${userType === "Organizer" ? "selected-button" : ""}`}>
                                    Organizator
                            </button>
                        </div>
                        <div className="auth-user-type-button-group" >
                            <button 
                                type="button" 
                                className="auth-button"
                                style={{width: "35%"}} 
                                onClick={() => {
                                    somefunction();
                                    window.location.href = "/register/account-creating";
                                }}
                            >
                                Wstecz
                            </button><button 
                                type="button" 
                                className="auth-button"
                                style={{width: "35%"}} 
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
