import React, { useState } from "react";
import { Card, CardContent, Typography, Tabs, Tab } from "@mui/material";
import "../styles/UserSite.css";

const TennisTournaments = () => {
    const [tab, setTab] = useState(0);

    const handleJoinTournament = (tournamentId: number) => {
        console.log(`Dołączono do turnieju o ID: ${tournamentId}`);
    };

    const tournaments = [
        {
            id: 1,
            name: "Turniej 1",
            date: "2023-10-01",
            status: "Nadchodzący",
            category: "NADCHODZĄCE",
        },
        {
            id: 2,
            name: "Turniej 2",
            date: "2023-10-15",
            status: "W toku",
            category: "W TOKU",
        },
        {
            id: 3,
            name: "Turniej 3",
            date: "2023-09-20",
            status: "Zakończony",
            category: "ZAKOŃCZONE",
        },
        {
            id: 4,
            name: "Turniej 4",
            date: "2023-11-05",
            status: "Nadchodzący",
            category: "NADCHODZĄCE",
        },
        {
            id: 5,
            name: "Turniej 5",
            date: "2023-10-10",
            status: "W toku",
            category: "W TOKU",
        },
        {
            id: 6,
            name: "Turniej 5",
            date: "2023-10-10",
            status: "W toku",
            category: "W TOKU",
        },
        {
            id: 7,
            name: "Turniej 5",
            date: "2023-10-10",
            status: "W toku",
            category: "W TOKU",
        },
        {
            id: 8,
            name: "Turniej 5",
            date: "2023-10-10",
            status: "W toku",
            category: "W TOKU",
        },
        {
            id: 9,
            name: "Turniej 6",
            date: "2023-09-25",
            status: "Zakończony",
            category: "ZAKOŃCZONE",
        },
    ];

    const filteredTournaments = tournaments.filter((tournament) => {
        if (tab === 0) return tournament.category === "NADCHODZĄCE";
        if (tab === 1) return tournament.category === "W TOKU";
        if (tab === 2) return tournament.category === "ZAKOŃCZONE";
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
                    {filteredTournaments.map((tournament) => (
                        <Card key={tournament.id} sx={{ margin: "16px 0" }}>
                            <CardContent className="card-content">
                                <Typography variant="h6" color="secondary">
                                    {tournament.name}
                                </Typography>
                                <div>
                                    <Typography
                                        variant="body2"
                                        color="textSecondary"
                                    >
                                        Data:{" "}
                                        <span style={{ color: "purple" }}>
                                            {tournament.date}
                                        </span>
                                    </Typography>
                                    <Typography
                                        variant="body2"
                                        color="textSecondary"
                                    >
                                        Status:{" "}
                                        <span style={{ color: "purple" }}>
                                            {tournament.status}
                                        </span>
                                    </Typography>
                                </div>
                                <button
                                    color="primary"
                                    className="user-button"
                                    onClick={
                                        tournament.status !== "Nadchodzący"
                                            ? undefined
                                            : () =>
                                                handleJoinTournament(
                                                    tournament.id
                                                )
                                    }
                                    style={{
                                        backgroundColor:
                                            tournament.status !== "Nadchodzący"
                                                ? "gray"
                                                : undefined,
                                        cursor:
                                            tournament.status !== "Nadchodzący"
                                                ? "not-allowed"
                                                : "pointer",
                                    }}
                                    disabled={tournament.status !== "Nadchodzący"}
                                >
                                    Dołącz
                                </button>
                            </CardContent>
                        </Card>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default TennisTournaments;
