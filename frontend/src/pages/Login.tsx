import "../styles/Auth.css";

const Login: React.FC = () => {
    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Logowanie</h3>
                <div className="auth-form">
                    <form>
                        <div className="auth-input-group">
                            <input type="email" name="email" placeholder="Email" required />
                        </div>
                        <div className="auth-input-group">
                            <input type="password" name="password" placeholder="Hasło" required />
                        </div>
                        <button type="submit" className="auth-button">Zaloguj się</button>
                    </form>
                    <p className="auth-bottom-text">
                        Nie masz konta? <a href="/register" className="auth-link">Zarejestruj się</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;
