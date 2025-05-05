import "../styles/Auth.css";
import "../styles/AccountCreating.css";

import React, { useEffect } from "react";
import { useState } from "react";
import z from "zod";
import { useNavigate } from "react-router-dom";

const accountSchema = z.object({
    firstName: z.string().min(1, "Imię jest wymagane"),
    lastName: z.string().min(1, "Nazwisko jest wymagane"),
    birthdate: z
        .string()
        .regex(
            /^\d{4}-\d{2}-\d{2}$/,
            "Data urodzenia musi być w formacie YYYY-MM-DD",
        )
        .refine(
            (date) => !isNaN(new Date(date).getTime()),
            "Data urodzenia musi być poprawną datą",
        ),
    termsAccepted: z.literal(true, {
        errorMap: () => ({ message: "Musisz zaakceptować regulamin" }),
    }),
});

const AccountCreating: React.FC = () => {
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [birthdate, setBirthdate] = useState("");
    const [termsAccepted, setTermsAccepted] = useState(false);
    const [error, setError] = useState<Record<string, string>>({});

    useEffect(() => {
        const registrationData = sessionStorage.getItem("registrationData");
        if (!registrationData) {
            navigate("/register");
        }
    }, [navigate]);

    const termsText: string =
        "Wyrażam zgode na przetwarzanie mojej daty urodzenia przez Akademię Górniczo-Hutnicza w celu weryfikacji mojej tożasmości. Dane będą przechowywane przez okres 6 miesięcy. Zostałem(am) przy tym poinformowany(a) o moich prawach w zakresie ochrony danych osobowych, w tym o prawie do dostępu do moich danych, ich poprawiania, usuwania oraz wycofywania zgody w dowolnym momencie";

    const validateForm = () => {
        try {
            accountSchema.parse({ firstName, lastName, birthdate, termsAccepted });
            setError({});
            return true;
        } catch (e) {
            if (e instanceof z.ZodError) {
                const fieldErrors: Record<string, string> = {};
                e.errors.forEach((error) => {
                    if (error.path.length > 0) {
                        fieldErrors[error.path[0]] = error.message;
                    }
                });
                setError(fieldErrors);
            }
            return false;
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (validateForm()) {
            const prevData = JSON.parse(
                sessionStorage.getItem("registrationData") || "{}",
            );
            const newData = {
                ...prevData,
                firstName,
                lastName,
                birthdate,
                termsAccepted,
            };
            sessionStorage.setItem("registrationData", JSON.stringify(newData));
            navigate("/register/chose-user-type");
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Utwórz konto</h3>
                <div className="auth-form">
                    <form onSubmit={handleSubmit}>
                        <div className="auth-input-group">
                            <input
                                type="text"
                                value={firstName}
                                placeholder="Imię"
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="text"
                                value={lastName}
                                placeholder="Nazwisko"
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="date"
                                value={birthdate}
                                placeholder="Data urodzenia"
                                onChange={(e) => setBirthdate(e.target.value)}
                                required
                            />
                        </div>
                        <div className="auth-terms">
                            <input
                                type="checkbox"
                                checked={termsAccepted}
                                onChange={(e) =>
                                    setTermsAccepted(e.target.checked)
                                }
                                required
                            />
                            <label htmlFor="terms" className="auth-terms-label">
                                <span className="auth-terms-text">
                                    {termsText}
                                </span>
                            </label>
                        </div>
                        {error.firstName && (
                            <p className="auth-error">{error.firstName}</p>
                        )}
                        {error.lastName && (
                            <p className="auth-error">{error.lastName}</p>
                        )}
                        {error.birthdate && (
                            <p className="auth-error">{error.birthdate}</p>
                        )}
                        {error.termsAccepted && (
                            <p className="auth-error">{error.termsAccepted}</p>
                        )}
                        <button type="submit" className="auth-terms-button">
                            Dalej
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default AccountCreating;
