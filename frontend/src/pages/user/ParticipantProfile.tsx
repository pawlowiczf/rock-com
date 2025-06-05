import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { TextField, IconButton, Avatar } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import "../../styles/UserSite.css";
import pages from "../Guard/Guard";
import { HTTP_ADDRESS } from "../../config";
const ParticipantProfile = () => {
    const navigate = useNavigate();
    const [profileData, setProfileData] = useState({
        firstName: "",
        lastName: "",
        city: "",
        email: "",
        phone: "",
    });

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
                    .includes("/profile")
            ) {
                navigate("/login");
            }
        }
        if (!registrationData) {
            navigate("/login");
        }
    }, [navigate]);

    const placeholderAvatar = "./assets/avatar-placeholder.jpg";

    const getProfileDataFromSession = async () => {
        try {
            const response = await fetch(`${HTTP_ADDRESS}/api/users/profile`, {
                method: "GET",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                throw new Error("Failed to fetch profile data");
            }
            console.log("response: ", response);
            const data = await response.json();
            console.log("data: ", data);
            if (!data) {
                throw new Error("No profile data found");
            }
            return {
                firstName: data.firstName || "",
                lastName: data.lastName || "",
                city: data.city || "",
                email: data.email || "",
                phone: data.phoneNumber || "",
            };
        } catch (error) {
            return {
                firstName: "",
                lastName: "",
                idNumber: "",
                email: "",
                phone: "",
            };
        }
    };

    useEffect(() => {
        getProfileDataFromSession().then(setProfileData);
    }, []);

    const [editField, setEditField] = useState<string | null>(null);

    const handleChange = (field: keyof typeof profileData, value: string) => {
        setProfileData({ ...profileData, [field]: value });
        sessionStorage.setItem(field, value);
    };

    const fields: { label: string; key: keyof typeof profileData }[] = [
        { label: "First Name", key: "firstName" },
        { label: "Last Name", key: "lastName" },
        { label: "City", key: "city" },
        { label: "Email", key: "email" },
        { label: "Phone", key: "phone" },
    ];

    return (
        <div className="user-site-container">
            <div className="user-site-window">
                <Avatar className="user-avatar" src={placeholderAvatar} />
                {fields.map(({ label, key }) => (
                    <div className="user-field-edit" key={key}>
                        {editField === key ? (
                            <TextField
                                fullWidth
                                value={profileData[key]}
                                onChange={(e) =>
                                    handleChange(key, e.target.value)
                                }
                                variant="outlined"
                                label={label}
                            />
                        ) : (
                            <TextField
                                fullWidth
                                value={profileData[key]}
                                disabled
                                variant="outlined"
                                label={label}
                            />
                        )}
                        <IconButton
                            onClick={() =>
                                setEditField(editField === key ? null : key)
                            }
                        >
                            {editField === key ? <CheckIcon /> : <EditIcon />}
                        </IconButton>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ParticipantProfile;
