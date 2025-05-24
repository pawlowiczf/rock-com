import React, { useState, useEffect } from "react";
import { Card, CardContent, Typography, Tabs, Tab } from "@mui/material";
import { useNavigate } from "react-router-dom";
import "../styles/UserSite.css";
import { HTTP_ADDRESS } from '../config.ts';

const OrganizerTournaments = () => {
    const navigate = useNavigate();
    const [tab, setTab] = useState(0);
    const [upcomingTournaments, setUpcomingTournaments] = useState<any[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const fetchUpcomingTournaments = async () => {
        setIsLoading(true);
        try {
            const response = await fetch(`${HTTP_ADDRESS}/api/competitions/upcoming`, {
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

    const handleEditTournament = (id: number) => {
        navigate("/tournaments/edit/"+id);
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
                                                : () => handleEditTournament(tournament.competitionId)
                                        }
                                        style={{
                                            backgroundColor:
                                                !tournament.registrationOpen ? "gray" : undefined,
                                            cursor: !tournament.registrationOpen ? "not-allowed" : "pointer",
                                        }}
                                        disabled={!tournament.registrationOpen}
                                    >
                                        Edytuj
                                    </button>
                                </CardContent>
                            </Card>
                        ))
                    )}
                </div>
            
            </div>
        </div>
    );
};

export default OrganizerTournaments;
