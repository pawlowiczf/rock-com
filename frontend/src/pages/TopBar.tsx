import React, { useState, useEffect } from "react";
import {
    AppBar,
    Toolbar,
    Typography,
    IconButton,
    Menu,
    MenuItem,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { useNavigate } from "react-router-dom";
import "../styles/TopBar.css";
import Logo from "../assets/logo.png";
import pages from "./Guard/Guard";

const TopBar: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const navigate = useNavigate();
    const [filteredPages, setFilteredPages] = useState<string[]>([]);
    const [permissions, setPermissions] = useState<string>("");

    useEffect(() => {
        console.log(sessionStorage);
        const registrationData = sessionStorage
            .getItem("permissions")
            ?.toLowerCase();
        console.log("Permissions:", registrationData);
        if (registrationData) {
            setPermissions(registrationData.toLowerCase());
            let paths: string[] = [];
            const location = window.location.pathname;
            if (location === "/login" || location.startsWith("/register")) {
                paths = [];
            }
            else{
                const userPages = pages
                    .filter((page) => page.permissions.includes(registrationData))
                    .filter((page) => page.constraints === "normal");
                console.log("User Pages:", userPages);
                paths = userPages.flatMap((page) => page.path);
            }
            setFilteredPages(paths);
            console.log("Filtered Pages:", filteredPages);
        } else {
            setFilteredPages([]);
        }
        if (
            !registrationData &&
            !window.location.pathname.startsWith("/register")
        ) {
            console.log(window.location.pathname);
            console.log("No permissions found, redirecting to login.");
            navigate("/login");
        }
    }, [navigate]);

    const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleNavigate = (path: string) => {
        navigate(path);
        handleClose();
    };

    return (
        <AppBar position="static" color="default">
            <Toolbar>
                {window.location.pathname !== "/login" && !window.location.pathname.startsWith("/register") && (
                    <>
                        <IconButton
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{ mr: 2 }}
                            onClick={handleMenuClick}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Menu anchorEl={anchorEl} open={open} onClose={handleClose}>
                            {filteredPages.map((path) => (
                                <MenuItem
                                    key={path}
                                    onClick={() => handleNavigate(path)}
                                >
                                    {path === "/login"
                                        ? "Logout"
                                        : path
                                            .split("/")
                                            .filter(Boolean)
                                            .map(
                                                (char) =>
                                                    char.charAt(0).toUpperCase() +
                                                    char.slice(1),
                                            )
                                            .join(" ")}
                                </MenuItem>
                            ))}
                        </Menu>
                    </>
                )}
                <Typography
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, cursor: "pointer" }}
                    onClick={() => navigate("/login")}
                >
                    Rock-Com
                </Typography>

                <IconButton
                    edge="end"
                    color="inherit"
                    sx={{ ml: 2 }}
                    onClick={() => navigate("/")}
                >
                    <img src={Logo} alt="Logo" style={{ height: 40 }} />
                </IconButton>
            </Toolbar>
        </AppBar>
    );
};

export default TopBar;
