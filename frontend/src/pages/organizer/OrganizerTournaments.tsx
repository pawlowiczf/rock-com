import React, { use, useEffect, useState } from "react";
import {
    Card,
    CardContent,
    Typography,
    Tabs,
    Tab,
    CircularProgress,
    Snackbar,
    Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import "../../styles/UserSite.css";
import { HTTP_ADDRESS } from "../../config.ts";
import TennisIcon from "../../assets/icons/tennis.svg";
import PingPongIcon from "../../assets/icons/pingpong.svg";
import BadmintonIcon from "../../assets/icons/badminton.svg";
import pages from "../Guard/Guard";
import { set } from "zod";

interface Tournament {
    competitionId: number;
    name: string;
    type: string;
    startTime: string;
    endTime: string;
    registrationOpen: boolean;
    city: string;
}

interface Match {
    matchId: number;
    tournamentId: number;
    player1: string;
    player2: string;
    score: string;
    status: string;
}

const OrganizerTournaments = () => {
    const navigate = useNavigate();
    const [tab, setTab] = useState(0);
    const [tournaments, setTournaments] = useState<Tournament[]>([]);
    const [matches, setMatches] = useState<Match[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

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
                    .includes("/organizer/tournaments")
            ) {
                navigate("/profile");
            }
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, []);

    useEffect(() => {
        fetchAllTournaments();
        fetchAllMatches();
    }, []);

    const apiFetchMatches = async () => {
        for (const tournament of tournaments) {
            const competitionId = tournament.competitionId;
            const response = await fetch(`${HTTP_ADDRESS}/api/matches/competitions/${competitionId}`, {
                method: "GET",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
            });
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || "Błąd połączenia z serwerem");
            }
            const data = await response.json();
            setMatches([...matches, ...data]);
        }
        return matches;
    }

    const apiFetchCompetitions = async () => {
        const response = await fetch(`${HTTP_ADDRESS}/api/competitions`, {
            method: "GET",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
        });
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Błąd połączenia z serwerem");
        }
        const data = await response.json();
        setTournaments(data);
    };

    const fetchAllMatches = async () => {
        setIsLoading(true);
        try {
            const data = await apiFetchMatches();
        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    }


    const fetchAllTournaments = async () => {
        setIsLoading(true);
        try {
            const data = await apiFetchCompetitions();
        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleEditTournament = (id: number) => {
        navigate("/tournaments/edit/" + id);
    };

    const filteredTournaments = tournaments.filter((tournament) => {
        if (tab === 0) return tournament.registrationOpen;
        if (tab === 1) return !tournament.registrationOpen;
        return false;
    });

    const getIcon = (type: string): string => {
        switch (type) {
            case "TENNIS_OUTDOOR":
                return TennisIcon as string;
            case "TABLE_TENNIS":
                return PingPongIcon as string;
            case "BADMINTON":
                return BadmintonIcon as string;
        }
    };

    return (
        <div className="user-site-container" style={{ display: "flex", flexDirection: "row", alignItems: "flex-start" }}>
            <div className="user-site-window">
                <Typography textAlign="center" color="#a020f0" variant="h6" gutterBottom>
                    Turnieje
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
                        <div style={{ display: "flex", justifyContent: "center", marginTop: "2rem" }}>
                            <CircularProgress color="primary" />
                        </div>
                    ) : filteredTournaments.length === 0 ? (
                        <Typography variant="body1" textAlign="center" style={{ marginTop: "2rem" }}>
                            Brak turniejów do wyświetlenia.
                        </Typography>
                    ) : (
                        filteredTournaments.map((tournament) => (
                            <Card
                                key={tournament.competitionId}
                                sx={{ margin: "16px 0" }}
                            >
                                <CardContent className="card-content">
                                    <div>
                                        <img
                                            src={getIcon(tournament.type)}
                                            alt={tournament.type}
                                            style={{
                                                width: "24px",
                                                height: "24px",
                                            }}
                                        />
                                        <Typography
                                            variant="h6"
                                            color="secondary"
                                        >
                                            {tournament.name}
                                        </Typography>
                                    </div>
                                    <div>
                                        <Typography
                                            variant="body2"
                                            color="textSecondary"
                                        >
                                            Data:{" "}
                                            <span style={{ color: "purple" }}>
                                                {new Date(
                                                    tournament.startTime,
                                                ).toLocaleDateString()}{" "}
                                                -{" "}
                                                {new Date(
                                                    tournament.endTime,
                                                ).toLocaleDateString()}
                                            </span>
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            color="textSecondary"
                                        >
                                            Status:{" "}
                                            <span style={{ color: "purple" }}>
                                                {tournament.registrationOpen
                                                    ? "Otwarte"
                                                    : "Zakończone"}
                                            </span>
                                        </Typography>
                                    </div>
                                    <button
                                        className="user-button"
                                        onClick={
                                            !tournament.registrationOpen
                                                ? undefined
                                                : () =>
                                                      handleEditTournament(
                                                          tournament.competitionId,
                                                      )
                                        }
                                        style={{
                                            backgroundColor:
                                                !tournament.registrationOpen
                                                    ? "gray"
                                                    : undefined,
                                            cursor: !tournament.registrationOpen
                                                ? "not-allowed"
                                                : "pointer",
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

            {/* Snackbar for error display */}
            <Snackbar open={!!error} autoHideDuration={6000} onClose={() => setError(null)}>
                <Alert onClose={() => setError(null)} severity="error">
                    {error}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default OrganizerTournaments;
