import "../styles/Auth.css";
import "../styles/AccountCreating.css";
const AccountCreating: React.FC = () => {
    const termsText: string =
        "Wyrażam zgode na przetwarzanie mojego numeru PESEL przez Akademię Górniczo-Hutnicza w celu weryfikacji mojej tożasmości.  Dane będą przechowywane przez okres 6 miesięcy. Zostałem(am) przy tym poinformowany(a) o moich prawach w zakresie ochrony danych osobowych, w tym o prawie do dostępu do moich danych, ich poprawiania, usuwania oraz wycofywania zgody w dowolnym momencie";

    const somefunction = () => {
        console.log("Button clicked!");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Utwórz konto</h3>
                <div className="auth-form">
                    <form>
                        <div className="auth-input-group">
                            <input
                                type="text"
                                name="name"
                                placeholder="Imię"
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="text"
                                name="lastname"
                                placeholder="Nazwisko"
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="text"
                                name="PESEL"
                                placeholder="PESEL"
                                required
                            />
                        </div>
                        <div className="auth-terms">
                            <input type="checkbox" name="terms" required />
                            <label htmlFor="terms" className="auth-terms-label">
                                <span className="auth-terms-text">
                                    {termsText}
                                </span>
                            </label>
                        </div>
                        <button
                            type="button"
                            className="auth-terms-button"
                            onClick={() => {
                                somefunction();
                                window.location.href =
                                    "/register/chose-user-type";
                            }}
                        >
                            Dalej
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default AccountCreating;
