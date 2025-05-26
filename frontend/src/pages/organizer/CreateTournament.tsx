import { useState, useMemo } from "react";
import "../../styles/CreateTournament.css";
import TennisIcon from "../../assets/icons/tennis.svg";
import PingPongIcon from "../../assets/icons/pingpong.svg";
import BadmintonIcon from "../../assets/icons/badminton.svg";
import TextField from "@mui/material/TextField";
import CircularProgress from "@mui/material/CircularProgress";
import z from "zod";
import { HTTP_ADDRESS } from "../../config";

const BaseSchema = z.object({
    type: z.string(),
    name: z.string().min(1, "Nazwa jest wymagana"),
    fromDate: z.string().min(1, "Data rozpoczęcia jest wymagana"),
    toDate: z.string().min(1, "Data zakończenia jest wymagana"),
    courts: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, {
        message: "Liczba boisk musi być większa od 0"
    }),
    matchTime: z.string().min(1, "Czas trwania meczu jest wymagany"),
    streetAddress: z.string().min(2, "Limit uczestników musi wynosić conajmniej 2"),
    city: z.string(),
    postalCode: z.string().regex(/^[0-9]{2}-[0-9]{3}$/, "Kod pocztowy musi być w formacie xx-xxx")
});

const StepOneSchema = BaseSchema.pick({
    name: true,
    fromDate: true,
    toDate: true,
    matchTime: true
}).refine((data) => new Date(data.toDate) >= new Date(data.fromDate), {
    message: "Data zakończenia musi być po dacie rozpoczęcia",
    path: ["toDate"]
}).refine(
    (data) => new Date(data.fromDate) >= new Date(),{
        message:"Data rozpoczęcia musi być w przyszłości",
        path: ["fromDate"]
    }
);

const StepTwoSchema = z.record(
    z.string(),
    z.object({
        from: z.string().min(1, "Godzina rozpoczęcia jest wymagana"),
        to: z.string().min(1, "Godzina zakończenia jest wymagana")
    }).refine(data => data.from < data.to, {
        message: "Godzina zakończenia musi być po godzinie rozpoczęcia",
        path: ["to"]
    })
);

const StepThreeSchema = BaseSchema.pick({
    streetAddress: true,
    postalCode: true,
    city: true,
    courts: true
});


const timeToMinutes = (time) => {
    const [hours = 0, minutes = 0] = time.split(":").map(Number);
    return hours * 60 + minutes;
};

const disciplineIcons = [
    { name: "TENNIS_OUTDOOR", src: TennisIcon },
    { name: "TABLE_TENNIS", src: PingPongIcon },
    { name: "BADMINTON", src: BadmintonIcon }
];

const initialFormState = {
    type: "TENNIS_OUTDOOR",
    name: "",
    fromDate: "",
    toDate: "",
    location: "",
    courts: "",
    matchTime: "",
    streetAddress: "",
    city: "",
    postalCode: ""
};

const CreateTournament = () => {
    const [formData, setFormData] = useState(initialFormState);
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

    const validateStep = (schema, data) => {
        const result = schema.safeParse(data);
        if (result.success) {
            setErrors({});
            return true;
        }
        const newErrors = {};
        result.error.errors.forEach(err => {
            const [day, field] = err.path;
            if (field) {
                newErrors[day] = {
                    ...newErrors[day],
                    [field]: err.message
                };
            } else {
                newErrors[day] = err.message;
            }
        });

        setErrors(newErrors);
        return false;
    };


    const days = useMemo(() => {
        const result = [];
        const start = new Date(formData.fromDate);
        const end = new Date(formData.toDate);
        for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
            result.push(d.toISOString().split("T")[0]);
        }
        return result;
    }, [formData.fromDate, formData.toDate]);

    const handleSubmit = async (e) => {
        console.log("Submitting...");
        e.preventDefault();
        if (!validateStep(StepThreeSchema, formData)) return;

        setIsLoading(true);

        try {
            const competitionData = {
                name: formData.name,
                type: formData.type,
                matchDurationMinutes: timeToMinutes(formData.matchTime),
                availableCourts: Number(formData.courts),
                streetAddress: formData.streetAddress,
                city: formData.city,
                postalCode: formData.postalCode,
                registrationOpen: true
            };

            console.log(competitionData);

            const response = await fetch(`${HTTP_ADDRESS}/api/competitions`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(competitionData),
                credentials: "include"
            });

            if (!response.ok) throw new Error("Błąd przy tworzeniu turnieju");

            const { competitionId } = await response.json();

            const competitionDates = Object.entries(dailyTimes).map(([date, times]) => ({
                competitionId,
                startTime: new Date(`${date}T${times.from}`).toISOString(),
                endTime: new Date(`${date}T${times.to}`).toISOString()
            }));

            await fetch(`${HTTP_ADDRESS}/api/competitions-dates`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(competitionDates),
                credentials: "include"
            });

            alert("Turniej został pomyślnie stworzony!");
            setFormData(initialFormState);
            setStep(1);
        } catch (error) {
            alert(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const renderStep1 = () => (
        <form className="create-tournament-form" onSubmit={e => {
            e.preventDefault();
            if (validateStep(StepOneSchema, formData)) setStep(2);
        }}>
            <div className="discipline-icons">
                {disciplineIcons.map(d => (
                    <button key={d.name} type="button" onClick={() => handleDisciplineSelect(d.name)}
                            className={`discipline-icon-button ${formData.type === d.name ? "selected" : ""}`}>
                        <img src={d.src} alt={d.name} />
                    </button>
                ))}
            </div>
            {["name", "fromDate", "toDate", "matchTime"].map(name => (
                <div className="create-tournament-input-group" key={name}>
                    <TextField
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

    const renderStep2 = () => (
        <form className="create-tournament-form" onSubmit={e => {
            e.preventDefault();
            if (validateStep(StepTwoSchema, dailyTimes)) setStep(3);
        }}>
            {days.map(day => (
                <div key={day}>
                    <label>{day}</label>
                    <div className="create-tournament-input-group">
                        <TextField
                            type="time"
                            label="Od"
                            value={dailyTimes[day]?.from || ""}
                            onChange={e => setDailyTimes(prev => ({
                                ...prev,
                                [day]: { ...prev[day], from: e.target.value }
                            }))}
                            error={!!errors[day]?.from}
                            helperText={errors[day]?.from}
                            InputLabelProps={{ shrink: true }}
                            required
                        />
                    </div>
                    <div className="create-tournament-input-group">
                        <TextField
                            type="time"
                            label="Do"
                            value={dailyTimes[day]?.to || ""}
                            onChange={e => setDailyTimes(prev => ({
                                ...prev,
                                [day]: { ...prev[day], to: e.target.value }
                            }))}
                            error={!!errors[day]?.to}
                            helperText={errors[day]?.to}
                            InputLabelProps={{ shrink: true }}
                            required
                        />
                    </div>
                </div>
            ))}
            <div className="create-tournament-button-group">
                <button type="button" onClick={() => setStep(1)} className="create-tournament-button">Wróć</button>
                <button type="submit" className="create-tournament-button">Dalej</button>
            </div>
        </form>
    );

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
                    helperText={errors.postalCode}
                    fullWidth
                    required
                />
                <TextField
                    name="city"
                    label="Miasto"
                    value={formData.city}
                    onChange={handleChange}
                    type="text"
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
                    helperText={errors.courts}
                    fullWidth
                    required
                />
            </div>
            <div className="create-tournament-button-group">
                <button type="button" onClick={() => setStep(2)} className="create-tournament-button">Wróć</button>
                <button type="submit" className="create-tournament-button" disabled={isLoading}>
                    {isLoading ? <CircularProgress size={20} /> : "Utwórz"}
                </button>
            </div>
        </form>
    );

    return (
        <div className="create-tournament-container">
            <div className="create-tournament-window">
                <h2 className="create-tournament-header">Utwórz turniej</h2>
                {step === 1 && renderStep1()}
                {step === 2 && renderStep2()}
                {step === 3 && renderStep3()}
            </div>
        </div>
    );
};

export default CreateTournament;
