import "../../styles/Auth.css";
import "../../styles/AccountCreating.css";

import React, { useEffect, useState } from "react";
import z from "zod";
import { useNavigate } from "react-router-dom";
import TextField from "@mui/material/TextField";

const accountSchema = z.object({
    firstName: z.string().min(1, "Imię jest wymagane"),
    lastName: z.string().min(1, "Nazwisko jest wymagane"),
    birthdate: z
        .string()
        .regex(/^\d{4}-\d{2}-\d{2}$/, "Data urodzenia musi być w formacie YYYY-MM-DD")
        .refine(date => !isNaN(new Date(date).getTime()), "Data urodzenia musi być poprawną datą")
        .refine(date => new Date(date) <= new Date(), "Data urodzenia nie może być w przyszłości"),
    termsAccepted: z.literal(true, {
        errorMap: () => ({ message: "Musisz zaakceptować regulamin" })
    })
});

const AccountCreating: React.FC = () => {
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [birthdate, setBirthdate] = useState("");
    const [termsAccepted, setTermsAccepted] = useState(false);
    const [errors, setErrors] = useState<Record<string, string>>({});

    const termsText: string =
        "Wyrażam zgode na przetwarzanie mojego numeru PESEL przez Akademię Górniczo-Hutnicza w celu weryfikacji mojej tożasmości.  Dane będą przechowywane przez okres 6 miesięcy. Zostałem(am) przy tym poinformowany(a) o moich prawach w zakresie ochrony danych osobowych, w tym o prawie do dostępu do moich danych, ich poprawiania, usuwania oraz wycofywania zgody w dowolnym momencie";

    useEffect(() => {
        const registrationData = sessionStorage.getItem("registrationData");
        if (!registrationData) navigate("/register");
    }, [navigate]);

    const validateForm = () => {
        const result = accountSchema.safeParse({ firstName, lastName, birthdate, termsAccepted });
        if (result.success) {
            setErrors({});
            return true;
        }
        const newErrors: Record<string, string> = {};
        result.error.errors.forEach((err) => {
            if (err.path.length > 0) {
                newErrors[err.path[0]] = err.message;
            }
        });
        setErrors(newErrors);
        return false;
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        const prevData = JSON.parse(sessionStorage.getItem("registrationData") || "{}");
        const newData = {
            ...prevData,
            firstName,
            lastName,
            birthdate,
            termsAccepted
        };
        sessionStorage.setItem("registrationData", JSON.stringify(newData));
        navigate("/register/chose-user-type");
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Utwórz konto</h3>
                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="auth-input-group">
                        <TextField
                            label="Imię"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            error={!!errors.firstName}
                            helperText={errors.firstName}
                            fullWidth
                        />
                    </div>
                    <div className="auth-input-group">
                        <TextField
                            label="Nazwisko"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            error={!!errors.lastName}
                            helperText={errors.lastName}
                            fullWidth
                        />
                    </div>
                    <div className="auth-input-group">
                        <TextField
                            label="Data urodzenia"
                            type="date"
                            value={birthdate}
                            onChange={(e) => setBirthdate(e.target.value)}
                            error={!!errors.birthdate}
                            helperText={errors.birthdate}
                            InputLabelProps={{ shrink: true }}
                            fullWidth
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
                    <button type="submit" className="auth-terms-button">
                        Dalej
                    </button>
                </form>
            </div>
        </div>
    );
};

export default AccountCreating;
