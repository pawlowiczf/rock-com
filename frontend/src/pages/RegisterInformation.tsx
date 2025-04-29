import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";



const RegisterInformation: React.FC = () => {

    const informationText: string = "Po weryfikacji przez pracownika firmy otrzymasz informacje mailową o aktywacji Twojego konta. W przypadku braku informacji w ciągu dwóch dni roboczych prosimy o kontakt: "

    const somefunction = () => {
        console.log("Button clicked!");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Twoje Konto jest weryfikowane</h3>
                <div className="auth-form">
                    <p className="auth-information-text">
                        {informationText}<a href="prawdziwy@mail.com" className="auth-link">prawdziwy@mail.com</a>
                    </p>
                    <div className="auth-user-type-button-group">
                        <button 
                            type="button" 
                            className="auth-button"
                            style={{width: "35%"}}
                            onClick={() => {
                                somefunction();
                                window.location.href = "/login";
                            }}
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
