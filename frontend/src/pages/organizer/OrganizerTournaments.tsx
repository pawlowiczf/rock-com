import React, { useEffect, useState } from "react";
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

interface Tournament {
    competitionId: number;
    name: string;
    type: string;
    startTime: string;
    endTime: string;
    registrationOpen: boolean;
    city: string;
}

const OrganizerTournaments = () => {
    const navigate = useNavigate();
    const [tab, setTab] = useState(0);
    const [upcomingTournaments, setUpcomingTournaments] = useState<Tournament[]>([]);
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
            fetchUpcomingTournaments();
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, []);

    const apiFetch = async (url: string, options: RequestInit = {}) => {
        const response = await fetch(`${HTTP_ADDRESS}${url}`, {
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            ...options,
        });
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Błąd połączenia z serwerem");
        }
        return response.json();
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

    const handleEditTournament = (id: number) => {
        navigate("/tournaments/edit/" + id);
    };

    const now = new Date();

    const filteredTournaments = upcomingTournaments.filter((t) => {
        const start = new Date(t.startTime);
        const end = new Date(t.endTime);

        if (tab === 0) return now < start;
        if (tab === 1) return now >= start && now <= end;
        if (tab === 2) return now > end;
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
                        filteredTournaments.map((tournament) => {
                            const start = new Date(tournament.startTime);
                            const editable = now < start;

                            return (<Card key={tournament.competitionId} sx={{margin: "16px 0"}}>
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
                                            <span style={{color: "purple"}}>
                                                {now < new Date(tournament.startTime)
                                                    ? "Nadchodzący"
                                                    : now > new Date(tournament.endTime)
                                                        ? "Zakończony"
                                                        : "W toku"}
                                            </span>
                                        </Typography>
                                    </div>
                                    <button
                                        className="user-button"
                                        onClick={() =>
                                            handleEditTournament(tournament.competitionId)
                                        }
                                        style={{
                                            backgroundColor:
                                                !editable ? "gray" : undefined,
                                            cursor: !editable ? "not-allowed" : "pointer",
                                        }}
                                        disabled={!editable}
                                    >
                                        Edytuj
                                    </button>
                                </CardContent>
                            </Card>)
                        })
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
