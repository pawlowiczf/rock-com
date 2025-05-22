import React, { useState, useEffect } from "react";
import { Card, CardContent, Typography, Tabs, Tab, Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@mui/material";
import "../styles/UserSite.css";

const UserTournaments = () => {
    const [tab, setTab] = useState(0);
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedTournament, setSelectedTournament] = useState<any>(null);
    const [upcomingTournaments, setUpcomingTournaments] = useState<any[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const fetchUpcomingTournaments = async () => {
        setIsLoading(true);
        try {
            const response = await fetch("http://localhost:8080/api/competitions/upcoming", {
                credentials: "include",
            });
            if (!response.ok) {
                throw new Error("Nie udało się pobrać danych o nadchodzących turniejach.");
            }
            const data = await response.json();
            console.log(data)
            setUpcomingTournaments(data);
        } catch (error) {
            alert(error);
        } finally {
            setIsLoading(false);
        }
    };


    useEffect(() => {
        fetchUpcomingTournaments();
    }, []);

    const handleOpenDialog = (tournament: any) => {
        setSelectedTournament(tournament);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setSelectedTournament(null);
    };

    const handleProceedToPayment = () => {
        console.log(`Proceeding to payment for: ${selectedTournament.name}`);
        handleJoinTournament(selectedTournament.competitionId);
        handleCloseDialog();
    };

    const handleJoinTournament = (tournamentId: number) => {
        console.log(`Dołączono do turnieju o ID: ${tournamentId}`);
    };


    const filteredTournaments = upcomingTournaments.filter((tournament) => {
        if (tab === 0) return tournament.registrationOpen;
        if (tab === 1) return !tournament.registrationOpen; 
        return false; 
    });

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
                <Typography
                    textAlign="center"
                    color="primary"
                    variant="h6"
                    gutterBottom
                >
                    Turnieje tenisa ziemnego
                </Typography>
                <Tabs
                    value={tab}
                    onChange={(e, newVal) => setTab(newVal)}
                    centered
                    textColor="primary"
                    indicatorColor="primary"
                >
                    <Tab label="NADCHODZĄCE" />
                    <Tab label="W TOKU" />
                    <Tab label="ZAKOŃCZONE" />
                </Tabs>
                <div className="tournament-list">
                    {isLoading ? (
                        <Typography variant="h6" color="primary" textAlign="center">
                            Ładowanie turniejów...
                        </Typography>
                    ) : (
                        filteredTournaments.map((tournament) => (
                            <Card key={tournament.competitionId} sx={{ margin: "16px 0" }}>
                                <CardContent className="card-content">
                                    <Typography variant="h6" color="secondary">
                                        {tournament.type} {tournament.competitionId}
                                    </Typography>
                                    <div>
                                        <Typography variant="body2" color="textSecondary">
                                            Data:{" "}
                                            <span style={{ color: "purple" }}>
                                                {new Date(tournament.startTime).toLocaleDateString()} - {new Date(tournament.endTime).toLocaleDateString()}
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
                                            !tournament.registrationOpen
                                                ? undefined
                                                : () => handleOpenDialog(tournament)
                                        }
                                        style={{
                                            backgroundColor:
                                                !tournament.registrationOpen ? "gray" : undefined,
                                            cursor: !tournament.registrationOpen ? "not-allowed" : "pointer",
                                        }}
                                        disabled={!tournament.registrationOpen}
                                    >
                                        Dołącz
                                    </button>
                                </CardContent>
                            </Card>
                        ))
                    )}
                </div>
                <Dialog open={openDialog} onClose={handleCloseDialog}>
                    <DialogTitle>Dołącz do turnieju</DialogTitle>
                    <DialogContent>
                        {selectedTournament && (
                            <Typography variant="body1">
                                Czy potwierdzasz zapisanie się na zawody: <strong>{selectedTournament.type}</strong><br />
                                Data: {new Date(selectedTournament.startTime).toLocaleDateString()} <br />
                                Lokalizacja: {selectedTournament.city} <br />
                                Koszt zapisu: <strong>300zł</strong> <br />
                            </Typography>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog} color="secondary">
                            Anuluj
                        </Button>
                        <Button onClick={handleProceedToPayment} variant="contained" color="primary">
                            Tak, przejdź do płatności
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        </div>
    );
};

export default UserTournaments;
