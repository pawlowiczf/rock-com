import { Card, CardContent, Typography } from "@mui/material";
import "../../styles/UserSite.css";
import {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";
import pages from "../Guard/Guard";
import {HTTP_ADDRESS} from "../../config.ts";

interface Match {
    name: number;
    matchDate: string;
    status: string;
    opponent: string;
    score: string;
    referee: string;
}

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

const UpcomingMatches = () => {
    const navigate = useNavigate();
    const [matches, setMatches] = useState<Match[]>([]);

    useEffect(() => {
        const registrationData = sessionStorage.getItem("permissions")?.toLowerCase();
        const isLoggedIn = sessionStorage.getItem("isLoggedIn");
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
                    .includes("/matches")
            ) {
                navigate("/profile");
            }
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, []);

    const fetchMatches = async () => {
        const data = await apiFetch("/api/participants/matches");
        setMatches(data);
    }

    useEffect(() => {
        fetchMatches();
    }, []);

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
                    Nadchodzące mecze
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
                                        Data: {match.matchDate.replace("T", " ")}
                                    </Typography>
                                    <Typography variant="body2">
                                        Sędzia:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.referee}
                                        </span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Przeciwnik:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.opponent?.trim() ? match.opponent : "TBD"}
                                        </span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Wynik:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.score?.trim() ? match.score : "TBD"}
                                        </span>
                                    </Typography>
                                </div>
                            </CardContent>
                        </Card>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default UpcomingMatches;
