import "../styles/Auth.css";
import "../styles/UserTypeChoser.css";



const JudgeLicence: React.FC = () => {


    const somefunction = () => {
        console.log("Button clicked!");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Załącz licencje sędziego</h3>
                <div className="auth-form">
                <form>
                    <div className="auth-input-group">
                        <input type="text" name="licenceNumber" placeholder="Numer licencji" required />
                    </div>
                    <div className="auth-input-group">
                        <input type="file" name="licenceFile" accept=".png, .jpg, .jpeg, .bmp" required />
                    </div>
                        <div className="auth-user-type-button-group">
                            <button 
                                type="button" 
                                className="auth-button" 
                                style={{width: "35%"}}
                                onClick={() => {
                                    somefunction();
                                    window.location.href = "/register/chose-user-type";
                                }}
                            >
                                Wstecz
                            </button><button 
                                type="button" 
                                className="auth-button"
                                style={{width: "35%"}}
                                onClick={() => {
                                    somefunction();
                                    window.location.href = "/register/information";
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
