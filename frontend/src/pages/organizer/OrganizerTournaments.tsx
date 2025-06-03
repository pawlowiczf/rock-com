import React, { useState, useEffect } from "react";
import { Card, CardContent, Typography, Tabs, Tab } from "@mui/material";
import { useNavigate } from "react-router-dom";
import "../../styles/UserSite.css";
import { HTTP_ADDRESS } from '../../config.ts';
import TennisIcon from "../../assets/icons/tennis.svg";
import PingPongIcon from "../../assets/icons/pingpong.svg";
import BadmintonIcon from "../../assets/icons/badminton.svg";

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

    const now = new Date();

    const filteredTournaments = upcomingTournaments.filter((t) => {
        const start = new Date(t.startTime);
        const end = new Date(t.endTime);

        if (tab === 0) return now < start;
        if (tab === 1) return now >= start && now <= end;
        if (tab === 2) return now > end;
    });

    function getIcon(type: string): object {
        switch (type) {
            case "TENNIS_OUTDOOR":
                return TennisIcon;
            case "TABLE_TENNIS":
                return PingPongIcon;
            case "BADMINTON":
                return BadmintonIcon;
        }
    }


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
                    color="#a020f0"
                    variant="h6"
                    gutterBottom
                >
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
                        <Typography variant="h6" color="primary" textAlign="center">
                            Ładowanie turniejów...
                        </Typography>
                    ) : (
                        filteredTournaments.map((tournament) => {
                            const start = new Date(tournament.startTime);
                            const editable = now < start;

                            return (<Card key={tournament.competitionId} sx={{margin: "16px 0"}}>
                                <CardContent className="card-content">
                                    <div>
                                        <img src={getIcon(tournament.type)} alt={tournament.type}
                                             style={{width: "24px", height: "24px"}}/>
                                        <Typography variant="h6" color="secondary">
                                            {tournament.name}
                                        </Typography>
                                    </div>
                                    <div>
                                        <Typography variant="body2" color="textSecondary">
                                            Data:{" "}
                                            <span style={{color: "purple"}}>
                                                {new Date(tournament.startTime).toLocaleDateString()} - {new Date(tournament.endTime).toLocaleDateString()}
                                            </span>
                                        </Typography>
                                        <Typography variant="body2" color="textSecondary">
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
        </div>
    );
};

export default OrganizerTournaments;
