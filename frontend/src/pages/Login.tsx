import "../styles/Auth.css";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { HTTP_ADDRESS } from "../config.ts";

const Login: React.FC = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState<string>("");
    const navigate = useNavigate();

    useEffect(() => {
        sessionStorage.setItem("permissions", "");
        sessionStorage.setItem("isLoggedIn", "false");
    }, [navigate]);

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault();
        setError(""); // Reset error message

        const formData = new URLSearchParams();
        formData.append("username", email);
        formData.append("password", password);
        try {
            const response = await fetch(`${HTTP_ADDRESS}/api/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                credentials: "include",
                body: formData.toString(),
            });
            if (!response.ok) {
                throw new Error("Niepoprawne dane logowania");
            }
            console.log("Zalogowano pomyślnie:", response);
            try {
                const permissionsResponse = await fetch(
                    `${HTTP_ADDRESS}/api/users/authorities/${email}`,
                    {
                        method: "GET",
                        credentials: "include",
                    },
                );
                if (!permissionsResponse.ok) {
                    throw new Error("Błąd podczas pobierania uprawnień");
                }
                const permissions = await permissionsResponse.text();
                sessionStorage.setItem("isLoggedIn", "true");
                sessionStorage.setItem("permissions", permissions);
                navigate("/profile");
            } catch (error) {
                console.error("Błąd podczas ustawiania sesji:", error);
                setError("Błąd podczas logowania. Spróbuj ponownie.");
            }
        } catch (error) {
            console.log(error);
            setError("Niepoprawne dane logowania");
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-window">
                <h3 className="auth-header">Logowanie</h3>
                <div className="auth-form">
                    <form onSubmit={handleLogin}>
                        <div className="auth-input-group">
                            <input
                                type="email"
                                name="email"
                                placeholder="Email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div className="auth-input-group">
                            <input
                                type="password"
                                name="password"
                                placeholder="Hasło"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="auth-button">
                            Zaloguj się
                        </button>
                    </form>
                    {error && <p className="auth-error">{error}</p>}
                    <p className="auth-bottom-text">
                        Nie masz konta?{" "}
                        <a href="/register" className="auth-link">
                            Zarejestruj się
                        </a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;
