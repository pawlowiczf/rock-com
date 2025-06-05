import "../../styles/EditMatch.css";
import { useState, useEffect, JSX } from "react";
import TextField from "@mui/material/TextField";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import InputLabel from "@mui/material/InputLabel";
import FormControl from "@mui/material/FormControl";
import { useNavigate, useParams } from "react-router-dom";
import { HTTP_ADDRESS } from "../../config.ts";
import pages from "../Guard/Guard";

const scorePattern = /^\d+:\d+$/;

interface Participant {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    city: string;
    phoneNumber: string;
    birthDate: string;
}

const apiFetch = async (url: string, options: RequestInit = {}) => {
    const response = await fetch(`${HTTP_ADDRESS}${url}`, {
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        ...options,
    });
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Wystąpił błąd");
    }
    return response.json();
};

const EditMatch: () => JSX.Element = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [participants, setParticipants] = useState<Participant[]>([]);

    // Zmieniamy formData tak, by player1Id itd. przechowywały ID, a nie imię i nazwisko
    const [formData, setFormData] = useState({
        matchId: "",
        competitionId: "",
        player1Id: "",
        player2Id: "",
        refereeId: "",
        score: "",
        winnerId: "",
        status: "",
    });

    useEffect(() => {
        if (!id) return;

        const fetchMatchAndParticipants = async () => {
            try {
                const match = await apiFetch(`/api/matches/${id}`);
                const data = await apiFetch(`/api/competitions/${match.competitionId}/participants`);
                const allParticipants = [...data.confirmed, ...data.waiting];

                setParticipants(allParticipants);

                setFormData({
                    matchId: match.matchId || "",
                    competitionId: match.competitionId || "",
                    player1Id: String(match.player1Id || ""),
                    player2Id: String(match.player2Id || ""),
                    refereeId: String(match.refereeId || ""),
                    score: match.score || "",
                    winnerId: String(match.winnerId || ""),
                    status: match.status || "",
                });
            } catch (error) {
                console.error("Błąd podczas pobierania danych meczu lub uczestników:", error);
            }
        };

        fetchMatchAndParticipants();
    }, [id]);

    const isScoreValid = scorePattern.test(formData.score);

    useEffect(() => {
        const registrationData = sessionStorage.getItem("permissions")?.toLowerCase();
        const isLoggedIn = sessionStorage.getItem("isLoggedIn");

        if (!isLoggedIn || isLoggedIn !== "true") {
            navigate("/login");
        } else if (registrationData) {
            const allowedPaths = pages
                .filter((page) => page.permissions.includes(registrationData))
                .flatMap((page) => page.path);
            if (!allowedPaths.includes("/tournaments/:id")) {
                navigate("/profile");
            }
        } else {
            navigate("/login");
        }
    }, [navigate]);

    const handleSubmit = async () => {
        // Walidacja: gracze nie mogą być tacy sami
        if (formData.player1Id === formData.player2Id) {
            alert("Gracz 1 i Gracz 2 nie mogą być tym samym uczestnikiem.");
            return;
        }

        // Walidacja: wynik musi mieć poprawny format
        if (!isScoreValid) {
            alert("Wynik musi być w formacie liczba:liczba, np. 6:4");
            return;
        }

        try {
            const updatedScore = {
                score: formData.score,
            };

            const response = await fetch(`${HTTP_ADDRESS}/api/matches/${id}`, {
                method: "PUT",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(updatedScore),
            });

            if (!response.ok) {
                const errorText = await response.text();
                console.error(errorText);
                return;
            }

            alert("Wynik meczu został pomyślnie zaktualizowany.");
            navigate(`/tournaments/${formData.competitionId}`); // przekierowanie po sukcesie

        } catch (error: any) {
            alert("Wystąpił błąd podczas aktualizacji: " + error.message);
        }
    };



    return (
        <div className="edit-match-container">
            <div className="edit-match-window">
                <h3 className="edit-match-header">Edytuj mecz</h3>
                <div className="edit-match-form">

                    <FormControl fullWidth size="small" className="edit-match-input-group">
                        <InputLabel id="referee-label" >Sędzia</InputLabel>
                        <Select
                            labelId="referee-label"
                            value={formData.refereeId}
                            label="Sędzia"
                            className="edit-match-input-group"
                            disabled
                            onChange={(e) =>
                                setFormData({
                                    ...formData,
                                    refereeId: e.target.value,
                                })
                            }
                        >
                            {participants.map((p) => (
                                <MenuItem key={p.userId} value={String(p.userId)}>
                                    {p.firstName} {p.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControl fullWidth size="small" className="edit-match-input-group">
                        <InputLabel id="player1-label">Gracz 1</InputLabel>
                        <Select
                            labelId="player1-label"
                            value={formData.player1Id}
                            label="Gracz 1"
                            className="edit-match-input-group"
                            disabled
                            onChange={(e) =>
                                setFormData({
                                    ...formData,
                                    player1Id: e.target.value,
                                })
                            }
                        >
                            {participants.map((p) => (
                                <MenuItem key={p.userId} value={String(p.userId)}>
                                    {p.firstName} {p.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControl fullWidth size="small" className="edit-match-input-group">
                        <InputLabel id="player2-label">Gracz 2</InputLabel>
                        <Select
                            labelId="player2-label"
                            value={formData.player2Id}
                            label="Gracz 2"
                            disabled
                            className="edit-match-input-group"
                            onChange={(e) =>
                                setFormData({
                                    ...formData,
                                    player2Id: e.target.value,
                                })
                            }
                        >
                            {participants.map((p) => (
                                <MenuItem key={p.userId} value={String(p.userId)}>
                                    {p.firstName} {p.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <div className="edit-match-input-group">
                        <TextField
                            label="Wynik (np. 6:4)"
                            value={formData.score}
                            className="edit-match-input-group"
                            onChange={(e) =>
                                setFormData({
                                    ...formData,
                                    score: e.target.value,
                                })
                            }
                            error={!!formData.score && !isScoreValid}
                            helperText={
                                formData.score && !isScoreValid
                                    ? "Nieprawidłowy format wyniku"
                                    : ""
                            }
                            fullWidth
                            size="small"
                        />
                    </div>

                    <div className="edit-match-button-group">
                        <button className="edit-match-button">ANULUJ</button>
                        <button
                            className="edit-match-button"
                            onClick={handleSubmit}
                        >
                            POTWIEDŹ
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default EditMatch;
