import {
    Card,
    CardContent,
    Typography,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
} from "@mui/material";
import React, { useState, useEffect } from "react";
import "../../styles/UserSite.css";
import {HTTP_ADDRESS} from "../../config.ts";
import { useNavigate } from "react-router-dom";
import pages from "../Guard/Guard";




const JudgeScore = () => {
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedTournament, setSelectedTournament] = useState<any>(null);
    const [scores, setScores] = useState<{ [key: number]: string }>({});
    const [inputScore, setInputScore] = useState({ player1: "", player2: "" });
    const [matches, setMatches] = useState<any[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const registrationData = sessionStorage.getItem("permissions")?.toLowerCase();
        console.log(registrationData);
        const isLoggedIn = sessionStorage.getItem("isLoggedIn");
        console.log("Permissions:", registrationData);
        if (!isLoggedIn || isLoggedIn !== "true") {
            navigate("/login");
        }
        if (registrationData) {
            if (
                !pages
                    .filter((page) =>
                        page.permissions.includes(registrationData),
                    )
                    .flatMap((page) => page.path)
                    .includes("/judge/score")
            ) {
                navigate("/profile");
            }
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, []);

    useEffect(() => {
        const fetchMatches = async () => {
            const refereeId = sessionStorage.getItem("userId");
            if (!refereeId) {
                navigate("/profile");
                return;
            }
            try {
                const response = await fetch(
                    `${HTTP_ADDRESS}/api/referees/${refereeId}/matches`,
                    {
                        method: "GET",
                        credentials: "include",
                    },
                );
                if (!response.ok) {
                    throw new Error("Błąd podczas pobierania meczów");
                }
                const data = await response.json();
                setMatches(data); // <- aktualizacja stanu meczy
            } catch (error) {
                console.error("Błąd podczas pobierania meczów:", error);
            }
        };
        fetchMatches();
    }, [navigate]);



    useEffect(() => {
        const fetchMatchResults = async () => {
            const userId = sessionStorage.getItem("userId");
            if (!userId) {
                navigate("/profile");
                return;
            }
            try {
                const response = await fetch(
                    `${HTTP_ADDRESS}/${userId}/matches/results`,
                    {
                        method: "GET",
                        credentials: "include",
                    },
                );
                console.log(response);
                if (!response.ok) {
                    throw new Error("Błąd podczas pobierania wyników meczów");
                }
                const data = await response.json();
                console.log("Pobrane wyniki meczów:", data);
                // Update the matches state with fetched data
            } catch (error) {
                console.error("Błąd podczas pobierania wyników meczów:", error);
            }
        };
        fetchMatchResults();
    }, [navigate]);

    const handleOpenDialog = (tournament: any, index: number) => {
        setSelectedTournament({ ...tournament, index });
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setSelectedTournament(null);
        setInputScore({ player1: "", player2: "" });
    };

    const handleAddingScore = async () => {
        if (selectedTournament) {
            setScores((prevScores) => ({
                ...prevScores,
                [selectedTournament.index]: `${inputScore.player1} - ${inputScore.player2}`,
            }));
            const response = await fetch(`${HTTP_ADDRESS}/api/matches/${selectedTournament.id}/results`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(inputScore),
                credentials: "include"
            })
            if (!response.ok) {
                alert("Wystąpił błąd podczas zapisywania wyniku.")
            }
        }

        handleCloseDialog();
    };

    return (
        <div
            className="user-site-container"
            style={{
                height: "100%",
                display: "flex",
                flexDirection: "row",
                alignItems: "flex-start",
            }}
        >
            <div className="user-site-window">
                <Typography
                    textAlign="center"
                    color="#a020f0"
                    variant="h6"
                    gutterBottom
                >
                    Historia meczy
                </Typography>
                <div className="tournament-list">
                    {matches.map((match, idx) => (
                        <Card key={idx} sx={{ mb: 2 }}>
                            <CardContent className="card-content">
                                <Typography variant="subtitle1" fontWeight="bold">
                                    Mecz #{match.competitionId}
                                </Typography>
                                <div>
                                    <Typography variant="body2">
                                        Data: {new Date(match.matchDate).toLocaleString("pl-PL")}
                                    </Typography>
                                    <Typography variant="body2">
                                        Gracz 1: <span style={{ color: "#A020F0" }}>{match.player1Name}</span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Gracz 2: <span style={{ color: "#A020F0" }}>{match.player2Name}</span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Status: <span style={{ color: "#A020F0" }}>{match.status}</span>
                                    </Typography>
                                </div>
                                {match.score ? (
                                    <Typography variant="body2" style={{ marginTop: "16px", color: "#A020F0", fontWeight: "bold" }}>
                                        Wynik:
                                        <p style={{ color: "gray" }}>{match.score}</p>
                                    </Typography>
                                ) : (
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={() => handleOpenDialog(match, idx)}
                                        style={{ marginTop: "16px", backgroundColor: "#A020F0", color: "white" }}
                                    >
                                        Wprowadź wynik
                                    </Button>
                                )}
                            </CardContent>
                        </Card>
                    ))}

                </div>
                <Dialog open={openDialog} onClose={handleCloseDialog}>
                    <DialogTitle>Wprowadź wynik</DialogTitle>
                    <DialogContent>
                        {selectedTournament && (
                            <Typography variant="body1">
                                Wprowadź wynik dla meczu: <br />
                                <strong>{selectedTournament.name}</strong>
                                <br />
                                Data: {selectedTournament.date} <br />
                                Gracz 1: {selectedTournament.opponent} <br />
                                Gracz 2: {selectedTournament.referee} <br />
                                Lokalizacja: {selectedTournament.location}{" "}
                                <br />
                                <br />
                            </Typography>
                        )}
                        <Typography variant="body1">
                            Wynik Gracza 1:
                            <input
                                type="text"
                                value={inputScore.player1}
                                onChange={(e) =>
                                    setInputScore((prev) => ({
                                        ...prev,
                                        player1: e.target.value,
                                    }))
                                }
                                placeholder="Wynik Gracza 1"
                                style={{
                                    width: "100%",
                                    padding: "8px",
                                    marginTop: "8px",
                                    borderRadius: "4px",
                                    border: "1px solid #ccc",
                                }}
                            />
                        </Typography>
                        <Typography
                            variant="body1"
                            style={{ marginTop: "16px" }}
                        >
                            Wynik Gracza 2:
                            <input
                                type="text"
                                value={inputScore.player2}
                                onChange={(e) =>
                                    setInputScore((prev) => ({
                                        ...prev,
                                        player2: e.target.value,
                                    }))
                                }
                                placeholder="Wynik Gracza 2"
                                style={{
                                    width: "100%",
                                    padding: "8px",
                                    marginTop: "8px",
                                    borderRadius: "4px",
                                    border: "1px solid #ccc",
                                }}
                            />
                        </Typography>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog} color="secondary">
                            Anuluj
                        </Button>
                        <Button
                            onClick={handleAddingScore}
                            variant="contained"
                            color="primary"
                        >
                            Potwierdź
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        </div>
    );
};

export default JudgeScore;
