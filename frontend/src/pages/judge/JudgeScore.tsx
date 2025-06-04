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
import { useNavigate } from "react-router-dom";
import pages from "../Guard/Guard";

const matches = [
    {
        name: "Wielki Tenis",
        date: "12.05.2025",
        opponent: "Barbara Kamienica",
        referee: "Tomasz Szkieletor",
        location: "Stadion Wisły, hala A",
    },
    {
        name: "Wielki Tenis",
        date: "15.05.2025",
        opponent: "Jan Kowalski",
        referee: "Anna Sędziowska",
        location: "Stadion Narodowy, hala B",
    },
    {
        name: "Wielki Tenis",
        date: "20.05.2025",
        opponent: "Marek Nowak",
        referee: "Piotr Gwizdek",
        location: "Arena Kraków, hala C",
    },
    {
        name: "Wielki Tenis4",
        date: "25.05.2025",
        opponent: "Katarzyna Zielińska",
        referee: "Joanna Piłka",
        location: "Stadion Lecha, hala D",
    },
];

const JudgeScore = () => {
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedTournament, setSelectedTournament] = useState<any>(null);
    const [scores, setScores] = useState<{ [key: number]: string }>({});
    const [inputScore, setInputScore] = useState({ player1: "", player2: "" });
    const navigate = useNavigate();

    useEffect(() => {
        const registrationData = sessionStorage.getItem("permissions")?.toLowerCase();
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
        const fetchMatchResults = async () => {
            const userId = sessionStorage.getItem("userId");
            if (!userId) {
                navigate("/login");
                return;
            }
            try {
                const response = await fetch(
                    `${userId}/matches/results`,
                    {
                        method: "GET",
                        credentials: "include",
                    },
                );
                if (!response.ok) {
                    throw new Error("Błąd podczas pobierania wyników meczów");
                }
                const data = await response.json();
                console.log("Pobrane wyniki meczów:", data);
                // Update the matches state with fetched data
            } catch (error) {
                console.error("Błąd podczas pobierania wyników meczów:", error);
                navigate("/login");
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

    const handleScore = (index: number) => {
    }

    const handleAddingScore = () => {
        if (selectedTournament) {
            setScores((prevScores) => ({
                ...prevScores,
                [selectedTournament.index]: `${inputScore.player1} - ${inputScore.player2}`,
            }));
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
                                <Typography
                                    variant="subtitle1"
                                    fontWeight="bold"
                                >
                                    {match.name}
                                </Typography>
                                <div>
                                    <Typography variant="body2">
                                        Data: {match.date}
                                    </Typography>
                                    <Typography variant="body2">
                                        Gracz 1:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.opponent}
                                        </span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Gracz 2:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.referee}
                                        </span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Lokalizacja:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.location}
                                        </span>
                                    </Typography>
                                </div>
                                {scores[idx] ? (
                                    <Typography
                                        variant="body2"
                                        style={{
                                            marginTop: "16px",
                                            color: "#A020F0",
                                            fontWeight: "bold",
                                        }}
                                    >
                                        Wynik:
                                        <p style={{ color: "gray" }}>
                                            {scores[idx]}
                                        </p>
                                    </Typography>
                                ) : (
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={() =>
                                            handleOpenDialog(match, idx)
                                        }
                                        style={{
                                            marginTop: "16px",
                                            backgroundColor: "#A020F0",
                                            color: "white",
                                        }}
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
