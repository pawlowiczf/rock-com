import { Card, CardContent, Typography } from "@mui/material";
import "../../styles/UserSite.css";
import { useEffect } from "react";
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

const UpcomingMatches = () => {
    const navigate = useNavigate();
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
                                        Data: {match.date}
                                    </Typography>
                                    <Typography variant="body2">
                                        Przeciwnik:{" "}
                                        <span style={{ color: "#A020F0" }}>
                                            {match.opponent}
                                        </span>
                                    </Typography>
                                    <Typography variant="body2">
                                        Sędzia:{" "}
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
                            </CardContent>
                        </Card>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default UpcomingMatches;
