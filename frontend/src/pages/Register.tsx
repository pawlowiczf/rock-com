import "../styles/Auth.css";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import z from "zod";

const RegisterSchema = z
    .object({
        email: z.string().email("Niepoprawny adres e-mail"),
        password: z.string().min(8, "Hasło musi mieć co najmniej 8 znaków"),
        repeatPassword: z.string(),
    })
    .refine((data) => data.password === data.repeatPassword, {
        message: "Hasła muszą być takie same",
        path: ["repeatPassword"],
    });

const Register: React.FC = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [error, setError] = useState<Record<string, string>>({});

    const validateForm = () => {
        try {
            RegisterSchema.parse({ email, password, repeatPassword });
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
        console.log("handleSubmit");
        e.preventDefault();
        if (validateForm()) {
            sessionStorage.setItem(
                "registrationData",
                JSON.stringify({ email, password }),
            );
            navigate("/register/account-creating");
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Rejestracja</h3>
                <div className="auth-form">
                    <form onSubmit={handleSubmit}>
                        <div className="auth-input-group">
                            <input
                                type="email"
                                value={email}
                                placeholder="Email"
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Hasło"
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="password"
                                value={repeatPassword}
                                placeholder="Powtórz hasło"
                                onChange={(e) =>
                                    setRepeatPassword(e.target.value)
                                }
                                required
                            />
                        </div>
                        {error.repeatPassword && (
                            <p className="auth-error">{error.repeatPassword}</p>
                        )}
                        {error.email && (
                            <p className="auth-error">{error.email}</p>
                        )}
                        {error.password && (
                            <p className="auth-error">{error.password}</p>
                        )}
                        <button type="submit" className="auth-button">
                            Zarejestruj się
                        </button>
                    </form>
                    <p className="auth-bottom-text">
                        Masz już konto?{" "}
                        <a href="/login" className="auth-link">
                            Zaloguj się
                        </a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Register;
