import { useState } from "react";
import { TextField, IconButton, Avatar } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import "../../styles/UserSite.css";

const ParticipantProfile = () => {
    const placeholderAvatar = "./assets/avatar-placeholder.jpg";

    const [profileData, setProfileData] = useState({
        firstName: "Piotr",
        lastName: "Budynek",
        idNumber: "4206920000",
        email: "piotr.budynek@gmail.com",
        phone: "Numer telefonu",
    });

    const [editField, setEditField] = useState<string | null>(null);

    const handleChange = (field: keyof typeof profileData, value: string) => {
        // Send it to the server
        setProfileData({ ...profileData, [field]: value });
    };

    const fields: { label: string; key: keyof typeof profileData }[] = [
        { label: "First Name", key: "firstName" },
        { label: "Last Name", key: "lastName" },
        { label: "ID Number", key: "idNumber" },
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
