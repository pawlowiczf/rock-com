import { useEffect } from "react";
import { useRouter } from "next/navigation";
import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";
import { useNavigate } from "react-router-dom";

const RegisterInformation: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const registrationData = sessionStorage.getItem("registrationData");
        if (!registrationData) {
            navigate("/register");
        }
    }, [navigate]);

    const informationText: string =
        "Po weryfikacji przez pracownika firmy otrzymasz informacje mailową o aktywacji Twojego konta. W przypadku braku informacji w ciągu dwóch dni roboczych prosimy o kontakt: ";

    const handleFinish = () => {
        sessionStorage.removeItem("registrationData");
        navigate("/login");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Twoje Konto jest weryfikowane</h3>
                <div className="auth-form">
                    <p className="auth-information-text">
                        {informationText}
                        <a href="prawdziwy@mail.com" className="auth-link">
                            prawdziwy@mail.com
                        </a>
                    </p>
                    <div className="auth-user-type-button-group">
                        <button
                            type="button"
                            className="auth-button"
                            style={{ width: "35%" }}
                            onClick={handleFinish}
                        >
                            Zakończ
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegisterInformation;
