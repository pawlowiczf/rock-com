import "../../styles/EditTournament.css";
import React, { useState, useEffect, JSX } from "react";
import { useParams, useNavigate } from "react-router-dom";
import DeleteIcon from "../../assets/icons/cross.svg";
import EditIcon from "../../assets/icons/pencil.svg";
import TennisIcon from "../../assets/icons/tennis.svg";
import PingPongIcon from "../../assets/icons/pingpong.svg";
import BadmintonIcon from "../../assets/icons/badminton.svg";
import TextField from "@mui/material/TextField";
import z from "zod";
import { HTTP_ADDRESS } from "../../config.ts";
import pages from "../Guard/Guard";
import { Button, Typography } from "@mui/material";

const TournamentSchema = z.object({
    type: z.string(),
    name: z.string().min(1, "Nazwa jest wymagana"),
    streetAddress: z.string().min(1, "Adres ulicy jest wymagany"),
    availableCourts: z
        .string()
        .refine((val) => !isNaN(Number(val)) && Number(val) > 0, {
            message: "Liczba boisk musi być większa od 0",
        }),
    participantsLimit: z
        .string()
        .refine((val) => !isNaN(Number(val)) && Number(val) >= 2, {
            message: "Limit uczestników musi być conajmniej 2",
        }),
    matchDurationMinutes: z.string().min(1, "Czas trwania meczu jest wymagany"),
    city: z.string().min(1, "Miasto jest wymagane"),
    postalCode: z
        .string()
        .regex(
            /^[0-9]{2}-[0-9]{3}$/,
            "Kod pocztowy musi być w formacie xx-xxx",
        ),
});

interface Participant {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    city: string;
    phoneNumber: string;
    birthDate: string;
}

declare global {
    interface Window {
        google: any;
    }
}

const EditTournament: () => JSX.Element = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

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
                    .includes("/tournaments/:id")
            ) {
                navigate("/profile");
            }
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, []);

    const [formData, setFormData] = useState({
        type: "TENNIS_OUTDOOR",
        name: "",
        streetAddress: "Stadion Wisły",
        availableCourts: "12",
        participantsLimit: "12",
        matchDurationMinutes: "00:45",
        city: "",
        postalCode: "",
    });

    const [participants, setParticipants] = useState<Participant[]>([]);
    const [removedParticipants, setRemovedParticipants] = useState<Participant[]>([]);

    const [errors, setErrors] = useState<Record<string, string>>({});

    const [activeTab, setActiveTab] = useState<"participants" | "matches">("participants");
    const [matches, setMatches] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const fetchMatches = async () => {
        try {
            setIsLoading(true);
            const allMatches = [];

                const response = await fetch(`${HTTP_ADDRESS}/api/matches/competitions/${id}`, {
                    method: "GET",
                    credentials: "include",
                    headers: { "Content-Type": "application/json" },
                });
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text || "Błąd połączenia z serwerem");
                }
                const data = await response.json();

            allMatches.push(...data["SCHEDULED"]);

            setMatches(allMatches);
        } catch (error) {
            console.error("Błąd podczas pobierania meczów:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (activeTab === "matches" && matches.length === 0) {
            fetchMatches();
        }
    }, [activeTab]);


    useEffect(() => {
        const fetchTournamentData = async () => {
            try {
                const data = await apiFetch(`/api/competitions/${id}`);

                setFormData((prev) => ({
                    ...prev,
                    type: data.type || prev.type,
                    name: data.name || prev.name,
                    streetAddress: data.streetAddress || prev.streetAddress,
                    availableCourts:
                        data.availableCourts?.toString() ||
                        prev.availableCourts,
                    participantsLimit:
                        data.participantsLimit?.toString() ||
                        prev.participantsLimit,
                    matchDurationMinutes:
                        minutesToTime(data.matchDurationMinutes) ||
                        prev.matchDurationMinutes,
                    city: data.city || "",
                    postalCode: data.postalCode || "",
                }));
            } catch (error) {
                console.error("Błąd ładowania danych:", error);
                alert("Nie udało się załadować danych turnieju");
            }
        };

        const fetchParticipants = async () => {
            try {
                const data = await apiFetch(`/api/competitions/${id}/participants`);
                console.log(data);
                const allParticipants = [...data.confirmed, ...data.waiting];
                setParticipants(allParticipants);
            } catch (error) {
                console.error("Błąd pobierania uczestników:", error);
            }
        };

        fetchTournamentData();
        fetchParticipants();
    }, [id]);

    const removeParticipant = (index: number) => {
        const participantToRemove = participants[index];
        const confirmed = window.confirm(
            `Czy na pewno chcesz usunąć uczestnika ${participantToRemove.firstName} ${participantToRemove.lastName}?`
        );
        if (confirmed) {
            setParticipants((prev) => prev.filter((_, i) => i !== index));
            setRemovedParticipants((prev) => [...prev, participantToRemove]);
        }
    };

    const handleDisciplineSelect = (discipline: string) => {
        setFormData((prev) => ({ ...prev, type: discipline }));
    };

    const validateForm = () => {
        try {
            TournamentSchema.parse(formData);
            setErrors({});
            return true;
        } catch (e) {
            if (e instanceof z.ZodError) {
                const newErrors: Record<string, string> = {};
                e.errors.forEach((err) => {
                    const field = err.path[0] as string;
                    newErrors[field] = err.message;
                });
                console.error(newErrors);
                setErrors(newErrors);
            }
            return false;
        }
    };

    const sendResignations = async () => {
        for (const participant of removedParticipants) {
            try {
                const response = await fetch(`${HTTP_ADDRESS}/api/competitions/${id}/resignation`, {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        credentials: "include",
                        body: JSON.stringify({ userId: participant.userId }),
                    }
                );
                if (!response.ok) {
                    console.error(`Błąd rezygnacji uczestnika ${participant.userId}`);
                }
            } catch (error) {
                console.error("Błąd połączenia:", error);
            }
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;
        setIsLoading(true);
        await submitTournamentData(formData);
        await sendResignations();
    };

    const handleOpening = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;
        setIsLoading(true);
        try {
            const response = await fetch(`${HTTP_ADDRESS}/api/competitions/${id}/openRegistration`, {
                method: "PUT",
                credentials: "include"
            });

            if (response.ok) {
                const participantsLimit = await response.json();
                setFormData({ ...formData, participantsLimit: participantsLimit })
                setIsLoading(false);
                alert("Turniej został otwarty!");
                navigate("/organizer/tournaments");
            } else {
                const error = await response.json();
                console.error("Błąd:", error);
                setIsLoading(false);
                alert("Wystąpił błąd przy aktualizacji turnieju.");
            }
        } catch (error) {
            console.error("Błąd połączenia:", error);
            setIsLoading(false);
            alert("Błąd połączenia z serwerem.");
        }
    };

    const handleStarting = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;
        setIsLoading(true);
        try {
            const response = await fetch(`${HTTP_ADDRESS}/api/competitions/${id}/start`, {
                method: "PUT",
                credentials: "include"
            });

            if (response.ok) {
                setIsLoading(false);
                alert("Turniej został rozpoczęty!");
                navigate("/organizer/tournaments");
            } else {
                const error = await response.json();
                console.error("Błąd:", error);
                setIsLoading(false);
                alert("Wystąpił błąd przy aktualizacji turnieju.");
            }
        } catch (error) {
            console.error("Błąd połączenia:", error);
            setIsLoading(false);
            alert("Błąd połączenia z serwerem.");
        }
    };

    const submitTournamentData = async (
        data: typeof formData
    ) => {
        const competitionData = {
            competitionId: id,
            type: data.type,
            name: data.name,
            streetAddress: data.streetAddress,
            availableCourts: Number(data.availableCourts),
            participantsLimit: Number(data.participantsLimit),
            matchDurationMinutes: timeToMinutes(data.matchDurationMinutes),
            city: data.city,
            postalCode: data.postalCode,
        };

        console.log(competitionData);

        try {
            const response = await fetch(
                `${HTTP_ADDRESS}/api/competitions/` + id,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(competitionData),
                    credentials: "include",
                },
            );

            if (response.ok) {
                setIsLoading(false);
                alert("Turniej został pomyślnie zaktualizowany!");
                navigate("/organizer/tournaments");
            } else {
                const error = await response.json();
                console.error("Błąd:", error);
                setIsLoading(false);
                alert("Wystąpił błąd przy aktualizacji turnieju.");
            }
        } catch (error) {
            console.error("Błąd połączenia:", error);
            setIsLoading(false);
            alert("Błąd połączenia z serwerem.");
        }
    };

    function getAge(birthDate: string | Date): number {
        const birth = new Date(birthDate);
        const today = new Date();

        let age = today.getFullYear() - birth.getFullYear();
        const monthDiff = today.getMonth() - birth.getMonth();
        const dayDiff = today.getDate() - birth.getDate();

        // Jeśli jeszcze nie obchodziliśmy urodzin w tym roku, odejmij 1
        if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
            age--;
        }

        return age;
    }

    const getNameById = (id: string) => {
        if (id == null){
            return "TBD";
        }

        for (const participant of participants) {
            if (String(participant.userId) === String(id)) {
                return `${participant.firstName} ${participant.lastName}`;
            }
        }
        return id; // fallback jeśli nie znaleziono
    };

    const timeToMinutes = (time: string): number => {
        const [hours, minutes] = time.split(":").map(Number);
        return hours * 60 + minutes;
    };

    const minutesToTime = (minutes: number): string => {
        const h = Math.floor(minutes / 60)
            .toString()
            .padStart(2, "0");
        const m = (minutes % 60).toString().padStart(2, "0");
        return `${h}:${m}`;
    };

    const disciplines = [
        { name: "TENNIS_OUTDOOR", src: TennisIcon, alt: "Tennis" },
        { name: "TABLE_TENNIS", src: PingPongIcon, alt: "Ping Pong" },
        { name: "BADMINTON", src: BadmintonIcon, alt: "Badminton" },
    ];

    return (
        <>
            {isLoading && (
                <div className="spinner-overlay">
                    <div className="spinner"></div>
                </div>
            )}
            <div className="edit-tournament-container">
                <div className="edit-tournament-window">
                    <h3 className="edit-tournament-header">Edytuj turniej</h3>
                    <div className="edit-tournament-scroll-pane">
                        <div className="discipline-icons">
                            {disciplines.map(({ name, src, alt }) => (
                                <button
                                    key={name}
                                    type="button"
                                    onClick={() => handleDisciplineSelect(name)}
                                    className={`discipline-icon-button ${formData.type === name ? "selected" : ""}`}
                                >
                                    <img src={src} alt={alt} />
                                </button>
                            ))}
                        </div>
                        <div className="edit-tournament-form">
                            <div className="edit-tournament-input-group">
                                <TextField
                                    fullWidth
                                    label="Nazwa"
                                    value={formData.name}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            name: e.target.value
                                        })
                                    }
                                    error={!!errors.name}
                                    helperText={errors.name}
                                />
                            </div>

                            <div className="edit-tournament-input-group">
                                <TextField
                                    fullWidth
                                    label="Lokalizacja"
                                    value={formData.streetAddress}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            streetAddress: e.target.value
                                        })
                                    }
                                    error={!!errors.streetAddress}
                                    helperText={errors.streetAddress}
                                />
                            </div>

                            <div className="create-tournament-location">
                                <TextField
                                    name="postalCode"
                                    label="Kod pocztowy"
                                    value={formData.postalCode}
                                    type="text"
                                    error={!!errors.postalCode}
                                    helperText={errors.postalCode}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            postalCode: e.target.value
                                        })
                                    }
                                    fullWidth
                                    required
                                />
                                <TextField
                                    name="city"
                                    label="Miasto"
                                    value={formData.city}
                                    type="text"
                                    error={!!errors.city}
                                    helperText={errors.city}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            city: e.target.value
                                        })
                                    }
                                    fullWidth
                                    required
                                />
                            </div>

                            <div className="edit-tournament-input-group">
                                <TextField
                                    fullWidth
                                    label="Liczba boisk"
                                    type="number"
                                    value={formData.availableCourts}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            availableCourts: e.target.value
                                        })
                                    }
                                    error={!!errors.availableCourts}
                                    helperText={errors.availableCourts}
                                />
                            </div>

                            <div className="edit-tournament-input-group">
                                <TextField
                                    fullWidth
                                    label="Limit uczestników"
                                    type="number"
                                    value={formData.participantsLimit}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            participantsLimit: e.target.value
                                        })
                                    }
                                    error={!!errors.participantsLimit}
                                    helperText={errors.participantsLimit}
                                />
                            </div>

                            <div className="edit-tournament-input-group">
                                <TextField
                                    fullWidth
                                    type="time"
                                    label="Czas trwania meczu"
                                    name="matchDurationMinutes"
                                    error={!!errors.matchDurationMinutes}
                                    helperText={errors.matchDurationMinutes}
                                    value={formData.matchDurationMinutes}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            matchDurationMinutes:
                                            e.target.value
                                        })
                                    }
                                    InputLabelProps={{ shrink: true }}
                                />
                            </div>

                            <div className="edit-tournament-input-group">
                                <button className="create-tournament-button">
                                    Edytuj daty
                                </button>
                            </div>

                            <div className="edit-tournament-tabs">
                                <Button
                                    className={`edit-tournament-tab ${activeTab === "participants" ? "active" : ""}`}
                                    variant="outlined"
                                    size="small"
                                    style={{ marginBottom: "1rem", marginTop: "1rem"}}
                                    onClick={() => setActiveTab("participants")}
                                >
                                    Uczestnicy
                                </Button>
                                <Button
                                    className={`edit-tournament-tab ${activeTab === "matches" ? "active" : ""}`}
                                    variant="outlined"
                                    size="small"
                                    style={{ marginBottom: "1rem", marginTop: "1rem"}}
                                    onClick={() => setActiveTab("matches")}
                                >
                                    Mecze
                                </Button>
                            </div>


                            {activeTab === "participants" && (
                                <>
                                    <div className="edit-tournament-participantsLimit-header">
                                        Lista uczestników
                                    </div>
                                    {participants.length === 0 ? (
                                        <Typography variant="body1" textAlign="center" style={{ marginTop: "1rem" }}>
                                            Brak uczestników do wyświetlenia.
                                        </Typography>
                                    ) : (participants.map((participant, i) => (
                                        <div key={participant.userId} className="edit-tournament-participant-item">
                                            <span>{participant.firstName} {participant.lastName}, {getAge(participant.birthDate)}</span>
                                            <img
                                                src={DeleteIcon}
                                                alt="Usuń"
                                                className="participant-remove-icon"
                                                onClick={() => removeParticipant(i)}
                                            />
                                        </div>
                                    )))}
                                </>
                            )}

                            {activeTab === "matches" && (
                                <>
                                    <div className="edit-tournament-participantsLimit-header">
                                        Lista meczów
                                    </div>
                                    {matches.length === 0 ? (
                                            <Typography variant="body1" textAlign="center" style={{ marginTop: "1rem" }}>
                                                Brak meczów do wyświetlenia.
                                            </Typography>
                                        ) :matches.sort((a, b) => a.matchId - b.matchId).map((match, i) => (
                                        <div key={i} className="edit-tournament-participant-item">
                                            <span>{match.matchId}: {getNameById(match.player1Id)} vs {getNameById(match.player2Id)}</span>
                                            <img
                                                src={EditIcon}
                                                alt="Edytuj"
                                                className="match-edit-icon"
                                                onClick={() => navigate("/matches/" +match.matchId)}
                                            />
                                        </div>
                                    ))}
                                </>
                            )}

                        </div>
                    </div>

                    <div className="edit-tournament-button-group">
                        <button
                            className="edit-tournament-button accept"
                            onClick={(e) => handleSubmit(e)}
                            type="submit"
                        >
                            AKCEPTUJ
                        </button>
                        <button
                            className="edit-tournament-button start"
                            onClick={(e) => handleOpening(e)}

                            type="submit"
                        >
                            OTWÓRZ ZAPISY
                        </button>
                        <button
                            className="edit-tournament-button accept"
                            onClick={(e) => handleStarting(e)}
                            type="submit"
                        >
                            ROZPOCZNIJ
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
};

export default EditTournament;
