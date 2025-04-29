import "../styles/Auth.css";

const Register: React.FC = () => {

    const somefunction = () => {
        // Placeholder for the function to be executed on button click
        console.log("Button clicked!");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Rejestracja</h3>
                <div className="auth-form">
                    <form>
                        <div className="auth-input-group">
                            <input type="email" name="email" placeholder="Email" required />
                        </div>
                        <div className="auth-input-group">
                            <input type="password" name="password" placeholder="Hasło" required />
                        </div>
                        <div className="auth-input-group">
                            <input type="password" name="repeat-password" placeholder="Powtórz hasło" required />
                        </div>
                        <button 
                            type="button" 
                            className="auth-button" 
                            onClick={() => {
                                somefunction();
                                window.location.href = "/register/account-creating";
                            }}
                        >
                            Zarejestruj się
                        </button>
                    </form>
                    <p className="auth-bottom-text">
                        Masz już konto? <a href="/login" className="auth-link">Zaloguj się</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Register;
