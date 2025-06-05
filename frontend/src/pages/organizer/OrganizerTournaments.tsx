import React, { useEffect, useState } from "react";
import {
    Alert,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Snackbar,
    Tab,
    Tabs,
    TextField,
    Typography
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
    const [tournaments, setTournaments] = useState<Tournament[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [showFilters, setShowFilters] = useState(true);
    const [filterName, setFilterName] = useState("");
    const [filterCity, setFilterCity] = useState("");
    const [filterStartDate, setFilterStartDate] = useState("");
    const [filterEndDate, setFilterEndDate] = useState("");
    const [filterType, setFilterType] = useState("");

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
                        page.permissions.includes(registrationData)
                    )
                    .flatMap((page) => page.path)
                    .includes("/organizer/tournaments")
            ) {
                navigate("/profile");
            }
            fetchAllTournaments();
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, []);

    const apiFetch = async (url: string, options: RequestInit = {}) => {
        const response = await fetch(`${HTTP_ADDRESS}${url}`, {
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            ...options
        });
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Błąd połączenia z serwerem");
        }
        return response.json();
    };

    const fetchAllTournaments = async () => {
        setIsLoading(true);
        try {
            const data = await apiFetch("/api/competitions/upcoming");
            setTournaments(data);
        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchAllTournaments();
    }, []);

    const handleEditTournament = (id: number) => {
        navigate("/tournaments/" + id);
    };

    const now = new Date();

    const filteredTournaments = tournaments.filter((tournament) => {
        const start = new Date(tournament.startTime);
        const end = new Date(tournament.endTime);
        const tabMatch =
            (tab === 0 && now < start && tournament.registrationOpen) ||
            (tab === 1 && now >= start && now < end) ||
            (tab === 2 && now > end);

        const nameMatch = tournament.name.toLowerCase().includes(filterName.toLowerCase());
        const cityMatch = tournament.city.toLowerCase().includes(filterCity.toLowerCase());
        const typeMatch = filterType === "" || tournament.type === filterType;

        return tabMatch && nameMatch && cityMatch && typeMatch;
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
        <div
            className="user-site-container"
            style={{
                display: "flex",
                flexDirection: "row",
                alignItems: "flex-start"
            }}
        >
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

                <Button
                    variant="outlined"
                    size="small"
                    onClick={() => setShowFilters((prev) => !prev)}
                    style={{ marginBottom: "1rem", marginTop: "1rem" }}
                >
                    {showFilters ? "Ukryj filtry" : "Pokaż filtry"}
                </Button>

                {showFilters && (
                    <div style={{ display: "flex", gap: "12px", marginBottom: "1rem", flexWrap: "wrap" }}>
                        <TextField
                            label="Nazwa"
                            variant="outlined"
                            size="small"
                            value={filterName}
                            onChange={(e) => setFilterName(e.target.value)}
                        />
                        <TextField
                            label="Miasto"
                            variant="outlined"
                            size="small"
                            value={filterCity}
                            onChange={(e) => setFilterCity(e.target.value)}
                        />
                        <TextField
                            label="Data od"
                            type="date"
                            variant="outlined"
                            size="small"
                            InputLabelProps={{ shrink: true }}
                            value={filterStartDate}
                            onChange={(e) => setFilterStartDate(e.target.value)}
                        />
                        <TextField
                            label="Data do"
                            type="date"
                            variant="outlined"
                            size="small"
                            InputLabelProps={{ shrink: true }}
                            value={filterEndDate}
                            onChange={(e) => setFilterEndDate(e.target.value)}
                        />
                    </div>
                )}


                <div className="tournament-list">
                    {isLoading ? (
                        <div style={{ display: "flex", justifyContent: "center", marginTop: "2rem" }}>
                            <CircularProgress color="primary" />
                        </div>
                    ) : filteredTournaments.length === 0 ? (
                        <Typography variant="body1" textAlign="center" style={{ marginTop: "1rem" }}>
                            Brak turniejów do wyświetlenia.
                        </Typography>
                    ) : (
                        filteredTournaments.map((tournament) => {
                            const start = new Date(tournament.startTime);
                            const editable = now < start;

                            return (<Card key={tournament.competitionId} sx={{ margin: "16px 0" }}>
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
                                                    tournament.startTime
                                                ).toLocaleDateString()}{" "}
                                                -{" "}
                                                {new Date(
                                                    tournament.endTime
                                                ).toLocaleDateString()}
                                            </span>
                                        </Typography>
                                        <div>
                                            <Typography
                                                variant="body2"
                                                color="textSecondary"
                                            >
                                                Miasto:{" "}
                                                <span style={{ color: "purple" }}>

                                                    {tournament.city}
                                            </span>
                                            </Typography>
                                        </div>
                                        <Typography
                                            variant="body2"
                                            color="textSecondary"
                                        >
                                            Status:{" "}
                                            <span style={{ color: "purple" }}>
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
                                            cursor: !editable ? "not-allowed" : "pointer"
                                        }}
                                        disabled={!editable}
                                    >
                                        Edytuj
                                    </button>
                                </CardContent>
                            </Card>);
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
