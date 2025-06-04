import React, { useEffect, useState } from "react";
import {
    Button,
    Card,
    CardContent,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Tab,
    Tabs,
    Typography,
    CircularProgress,
    Snackbar,
    Alert
} from "@mui/material";
import "../../styles/UserSite.css";
import { HTTP_ADDRESS } from "../../config.ts";
import TennisIcon from "../../assets/icons/tennis.svg";
import PingPongIcon from "../../assets/icons/pingpong.svg";
import BadmintonIcon from "../../assets/icons/badminton.svg";

interface Tournament {
    competitionId: number;
    name: string;
    type: string;
    startTime: string;
    endTime: string;
    registrationOpen: boolean;
    city: string;
}

const UserTournaments = () => {
    const [tab, setTab] = useState(0);
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null);
    const [upcomingTournaments, setUpcomingTournaments] = useState<Tournament[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [joinedTournaments, setJoinedTournaments] = useState<number[]>([]);

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

    const checkIfUserIsJoined = async (tournaments: Tournament[]) => {
        const userId = sessionStorage.getItem("userId");
        if (!userId) return;

        const joined: number[] = [];

        await Promise.all(
            tournaments.map(async (tournament) => {
                try {
                    const data = await apiFetch(`/api/competitions/${tournament.competitionId}/participants`);
                    const allParticipants = [...data.confirmed, ...data.waiting];
                    const isUserIn = allParticipants.some((p: any) => String(p.userId) === userId);
                    if (isUserIn) {
                        joined.push(tournament.competitionId);
                    }
                } catch (err) {
                    console.error(`Błąd przy sprawdzaniu turnieju ${tournament.competitionId}:`, err);
                }
            })
        );

        setJoinedTournaments(joined);
    };


    const fetchUpcomingTournaments = async () => {
        setIsLoading(true);
        try {
            const data = await apiFetch("/api/competitions/upcoming");
            setUpcomingTournaments(data);
        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchUpcomingTournaments();
    }, []);

    useEffect(() => {
        if (upcomingTournaments.length > 0) {
            checkIfUserIsJoined(upcomingTournaments);
        }
    }, [upcomingTournaments]);

    const handleOpenDialog = (tournament: Tournament) => {
        setSelectedTournament(tournament);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setSelectedTournament(null);
    };

    const handleProceedToPayment = async () => {
        if (!selectedTournament) return;
        try {
            await apiFetch(`/api/competitions/${selectedTournament.competitionId}/enroll`, {
                method: "POST",
            });
            setSuccessMessage("Pomyślnie dołączono do turnieju!");
            window.location.reload();
        } catch (err: any) {
            setError(err.message);
        } finally {
            handleCloseDialog();
        }
    };

    const filteredTournaments = upcomingTournaments.filter((tournament) => {
        if (tab === 0) return tournament.registrationOpen;
        if (tab === 1) return !tournament.registrationOpen;
        return false;
    });

    const getIcon = (type: string): string => {
        switch (type) {
            case "TENNIS_OUTDOOR":
                return TennisIcon;
            case "TABLE_TENNIS":
                return PingPongIcon;
            case "BADMINTON":
                return BadmintonIcon;
            default:
                return "";
        }
    };

    return (
        <div
            className="user-site-container"
            style={{
                display: "flex",
                flexDirection: "row",
                alignItems: "flex-start",
            }}
        >
            <div className="user-site-window">
                <Typography textAlign="center" color="#a020f0" variant="h6" gutterBottom>
                    Turnieje
                </Typography>
                <Tabs value={tab} onChange={(e, newVal) => setTab(newVal)} centered textColor="primary" indicatorColor="primary">
                    <Tab label="NADCHODZĄCE" />
                    <Tab label="W TOKU" />
                    <Tab label="ZAKOŃCZONE" />
                </Tabs>
                <div className="tournament-list">
                    {isLoading ? (
                        <div style={{ display: "flex", justifyContent: "center", marginTop: "2rem" }}>
                            <CircularProgress color="primary" />
                        </div>
                    ) : filteredTournaments.length === 0 ? (
                        <Typography variant="body1" textAlign="center" style={{ marginTop: "2rem" }}>
                            Brak turniejów do wyświetlenia.
                        </Typography>
                    ) : (
                        filteredTournaments.map((tournament) => {
                            const isJoined = joinedTournaments.includes(tournament.competitionId);
                            return (
                                <Card key={tournament.competitionId} sx={{ margin: "16px 0" }}>
                                    <CardContent className="card-content">
                                        <div>
                                            <img
                                                src={getIcon(tournament.type)}
                                                alt={tournament.type}
                                                style={{
                                                    width: "24px",
                                                    height: "24px"
                                                }}
                                            />
                                            <Typography variant="h6" color="secondary">
                                                {tournament.name}
                                            </Typography>
                                        </div>
                                        <div>
                                            <Typography variant="body2" color="textSecondary">
                                                Data:{" "}
                                                <span style={{ color: "purple" }}>
                            {new Date(tournament.startTime).toLocaleDateString()} -{" "}
                                                    {new Date(tournament.endTime).toLocaleDateString()}
                        </span>
                                            </Typography>
                                            <Typography variant="body2" color="textSecondary">
                                                Status:{" "}
                                                <span style={{ color: "purple" }}>
                            {tournament.registrationOpen ? "Otwarte" : "Zakończone"}
                        </span>
                                            </Typography>
                                        </div>
                                        <button
                                            className="user-button"
                                            onClick={
                                                tournament.registrationOpen && !isJoined
                                                    ? () => handleOpenDialog(tournament)
                                                    : undefined
                                            }
                                            style={{
                                                backgroundColor:
                                                    !tournament.registrationOpen || isJoined ? "gray" : undefined,
                                                cursor:
                                                    !tournament.registrationOpen || isJoined ? "not-allowed" : "pointer"
                                            }}
                                            disabled={!tournament.registrationOpen || isJoined}
                                        >
                                            {isJoined ? "Zapisano" : "Dołącz"}
                                        </button>
                                    </CardContent>
                                </Card>
                            );
                        })
                    )}
                </div>

                {/* Dialog */}
                <Dialog open={openDialog} onClose={handleCloseDialog}>
                    <DialogTitle>Dołącz do turnieju</DialogTitle>
                    <DialogContent>
                        {selectedTournament && (
                            <Typography variant="body1">
                                Czy potwierdzasz zapisanie się na zawody:{" "}
                                <strong>{selectedTournament.type}</strong>
                                <br />
                                Data:{" "}
                                {new Date(
                                    selectedTournament.startTime,
                                ).toLocaleDateString()}{" "}
                                <br />
                                Lokalizacja: {selectedTournament.city} <br />
                                Koszt zapisu: <strong>300zł</strong> <br />
                            </Typography>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog} color="secondary">
                            Anuluj
                        </Button>
                        <Button
                            onClick={handleProceedToPayment}
                            variant="contained"
                            color="primary"
                        >
                            Tak, przejdź do płatności
                        </Button>
                    </DialogActions>
                </Dialog>

                {/* Snackbar Notifications */}
                <Snackbar open={!!error} autoHideDuration={6000} onClose={() => setError(null)}>
                    <Alert onClose={() => setError(null)} severity="error">
                        {error}
                    </Alert>
                </Snackbar>
                <Snackbar open={!!successMessage} autoHideDuration={6000} onClose={() => setSuccessMessage(null)}>
                    <Alert onClose={() => setSuccessMessage(null)} severity="success">
                        {successMessage}
                    </Alert>
                </Snackbar>
            </div>
        </div>
    );
};

export default UserTournaments;
