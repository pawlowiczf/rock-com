import { useState } from "react";
import "../../styles/CreateTournament.css";
import TennisIcon from "../../assets/icons/tennis.svg";
import PingPongIcon from "../../assets/icons/pingpong.svg";
import BadmintonIcon from "../../assets/icons/badminton.svg";
import TextField from "@mui/material/TextField";
import z from "zod";
import { HTTP_ADDRESS } from '../../config.ts';


const BaseSchema = z.object({
    type: z.string(),
    name: z.string().min(1, "Nazwa jest wymagana"),
    fromDate: z.string().min(1, "Data rozpoczęcia jest wymagana"),
    toDate: z.string().min(1, "Data zakończenia jest wymagana"),
    courts: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, {
        message: "Liczba boisk musi być większa od 0",
    }),
    participants: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, {
        message: "Limit uczestników musi być większa od 0",
    }),
    matchTime: z.string().min(1, "Czas trwania meczu jest wymagany"),
    streetAddress: z.string().optional(),
    city: z.string().optional(),
    postalCode: z.string().regex(/^\d{2}-\d{3}$/, "Kod pocztowy musi być w formacie xx-xxx"),
});

const TournamentSchema = BaseSchema
  .refine((data) => new Date(data.toDate) > new Date(data.fromDate), {
    message: "Data zakończenia musi być po dacie rozpoczęcia",
    path: ["toDate"],
  })
  .refine(
    (data) => new Date(data.fromDate) >= new Date(),
    "Data rozpoczęcia musi być w przyszłości"
  );

// Teraz możesz bezpiecznie wyciągać części:
const StepOneSchema = BaseSchema.pick({
  name: true,
  fromDate: true,
  toDate: true,
  matchTime: true,
});

const StepThreeSchema = BaseSchema.pick({
  location: true,
  courts: true,
  participants: true,
});


const timeToMinutes = (time) => {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
};

const CreateTournament = () => {
    const [formData, setFormData] = useState({
        type: "TENNIS_OUTDOOR",
        name: "",
        fromDate: "",
        toDate: "",
        location: "",
        courts: "",
        participants: "",
        matchTime: "",
        streetAddress: "",
        city: "",
        postalCode: "",
    });
    const [dailyTimes, setDailyTimes] = useState({});
    const [errors, setErrors] = useState({});
    const [step, setStep] = useState(1);
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleDisciplineSelect = (discipline) => {
        setFormData(prev => ({ ...prev, type: discipline }));
    };

    const validateStep = (schema) => {
        const result = schema.safeParse(formData);
        if (result.success) {
            setErrors({});
            return true;
        }
        const newErrors = Object.fromEntries(result.error.errors.map(err => [err.path[0], err.message]));
        setErrors(newErrors);
        console.error(newErrors);
        return false;
    };

    const validateDailyTimes = () => {
        const dates = Object.keys(dailyTimes);
        for (const date of dates) {
            const times = dailyTimes[date];
            if (!times.from || !times.to) return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        console.log("Submitting step...");
        e.preventDefault();
        if (!validateStep(TournamentSchema)) return;
        setIsLoading(true);

        try {
            const competitionData = {
                type: formData.type,
                matchDurationMinutes: timeToMinutes(formData.matchTime),
                availableCourts: Number(formData.courts),
                participantsLimit: Number(formData.participants),
                streetAddress: formData.streetAddress,
                city: formData.city,
                postalCode: formData.postalCode,
                registrationOpen: true
            };

            console.log("Sending:" + competitionData);

            const response = await fetch(`${HTTP_ADDRESS}/api/competitions`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(competitionData),
                credentials: "include"
            });

            if (!response.ok) throw new Error("Błąd przy tworzeniu turnieju");

            const { competitionId } = await response.json();

            const competitionDates = Object.entries(dailyTimes).map(([date, times]) => {
                const start = new Date(`${date}T${times.from}`);
                const end = new Date(`${date}T${times.to}`);
                return {
                    competitionId,
                    startTime: start.toISOString(),
                    endTime: end.toISOString(),
                };
            });

            console.log("Sending:" + competitionDates);

            await fetch(`${HTTP_ADDRESS}/api/competitions-dates`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(competitionDates),
                credentials: "include"
            });

            alert("Turniej został pomyślnie stworzony!");
            window.location.reload();
        } catch (error) {
            alert(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const renderStep1 = () => (
        <form className="create-tournament-form" onSubmit={e => { e.preventDefault(); if (validateStep(StepOneSchema)) setStep(2); }}>
            <div className="discipline-icons">
                {[{ name: "TENNIS_OUTDOOR", src: TennisIcon }, { name: "TABLE_TENNIS", src: PingPongIcon }, { name: "BADMINTON", src: BadmintonIcon }].map(d => (
                    <button key={d.name} type="button" onClick={() => handleDisciplineSelect(d.name)} className={`discipline-icon-button ${formData.type === d.name ? "selected" : ""}`}>
                        <img src={d.src} alt={d.name} />
                    </button>
                ))}
            </div>
            {["name", "fromDate", "toDate", "matchTime"].map(name => (
                <div className="create-tournament-input-group">
                <TextField
                    key={name}
                    name={name}
                    label={name === "name" ? "Nazwa" : name === "fromDate" ? "Od" : name === "toDate" ? "Do" : "Czas trwania meczu"}
                    type={name.includes("Date") ? "date" : name === "matchTime" ? "time" : "text"}
                    value={formData[name]}
                    onChange={handleChange}
                    error={!!errors[name]}
                    helperText={errors[name]}
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    required
                />
                </div>
            ))}
            <button type="submit" className="create-tournament-button">Dalej</button>
        </form>
    );

    const renderStep2 = () => {
        const days = [];
        const start = new Date(formData.fromDate);
        const end = new Date(formData.toDate);
        for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
            const key = d.toISOString().split("T")[0];
            days.push(key);
        }

        return (
            <div className="edit-tournament-scroll-pane">

            <form className="create-tournament-form" onSubmit={e => { e.preventDefault(); if (validateDailyTimes()) setStep(3); else alert("Uzupelnij godziny dla kazdego dnia") }}>
                {days.map(day => (
                    <div key={day}>
                        <label>{day}</label>
                        <div className="create-tournament-input-group">
                        <TextField type="time"
                                   label="Od"
                                   value={dailyTimes[day]?.from || ""}
                                   onChange={e => setDailyTimes(prev => ({ ...prev, [day]: { ...(prev[day] || {}), from: e.target.value } }))}
                                   InputLabelProps={{ shrink: true }}
                                   required />
                        </div>

                        <div className="create-tournament-input-group">

                        <TextField type="time"
                                   label="Do"
                                   value={dailyTimes[day]?.to || ""}
                                   onChange={e => setDailyTimes(prev => ({ ...prev, [day]: { ...(prev[day] || {}), to: e.target.value } }))}
                                   InputLabelProps={{ shrink: true }}
                                   required />
                        </div>
                        </div>
                ))}
                <div className="create-tournament-button-group">
                    <button className="create-tournament-button" onClick={() => setStep(1)}> Wróć</button>
                    <button type="submit" className="create-tournament-button">Dalej</button>
                </div>
            </form>
            </div>
        );
    };

    const renderStep3 = () => (

        <form className="create-tournament-form" onSubmit={handleSubmit}>
            <div className="create-tournament-input-group">
                <TextField
                    name="streetAddress"
                    label="Adres"
                    value={formData.streetAddress}
                    onChange={handleChange}
                    type="text"
                    fullWidth
                    required
                />
            </div>

            <div className="create-tournament-location">
                <TextField
                    name="postalCode"
                    label="Kod pocztowy"
                    value={formData.postalCode}
                    onChange={handleChange}
                    type="text"
                    error={!!errors.postalCode}
                    fullWidth
                    required
                />
                <TextField
                    name="city"
                    label="Miasto"
                    value={formData.city}
                    onChange={handleChange}
                    type="text"
                    error={!!errors.city}
                    fullWidth
                    required
                />
            </div>

            <div className="create-tournament-input-group">
                <TextField
                    name="courts"
                    label="Liczba boisk"
                    value={formData.courts}
                    onChange={handleChange}
                    type="number"
                    error={!!errors.courts}
                    fullWidth
                    required
                />
            </div>

            <div className="create-tournament-input-group">
                <TextField
                    name="participants"
                    label="Limit uczestników"
                    value={formData.participants}
                    onChange={handleChange}
                    type="number"
                    error={!!errors.participants}
                    fullWidth
                />
            </div>

            <div className="create-tournament-button-group">
                <button
                    className="create-tournament-button"
                    type="button"
                    onClick={() => setStep(2)}>
                    Wróć
                </button>
                <button type="submit" className="create-tournament-button">
                    Utwórz
                </button>
            </div>
        </form>
    );


    return (
        <div className="create-tournament-container">
            <div className="create-tournament-window">
                <h2 className="create-tournament-header">Utworz turniej</h2>
                {step === 1 && renderStep1()}
                {step === 2 && renderStep2()}
                {step === 3 && renderStep3()}
            </div>
        </div>
    );
};

export default CreateTournament;
