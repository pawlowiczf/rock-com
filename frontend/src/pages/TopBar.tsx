
import React, { useState } from 'react';
import {
    AppBar, Toolbar, Typography, IconButton, Menu, MenuItem
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { useNavigate } from 'react-router-dom';
import "../styles/TopBar.css";
import Logo from '../assets/logo.png';

const TopBar: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const navigate = useNavigate();

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
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    sx={{ mr: 2 }}
                    onClick={handleMenuClick}
                >
                    <MenuIcon />
                </IconButton>
                <Menu
                    anchorEl={anchorEl}
                    open={open}
                    onClose={handleClose}
                >
                    <MenuItem onClick={() => handleNavigate('/login')}>Login</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/register')}>Register</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/register/account-creating')}>Account Creating</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/register/chose-user-type')}>Choose User Type</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/register/judge')}>Judge Licence</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/register/information')}>Register Information</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/tournaments/create')}>Create Tournament</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/tournaments/edit')}>Edit Tournament</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/matches/edit')}>Edit Match</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/profle')}>Profile</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/matches')}>Matches</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/judge/score')}>Judge Score</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/tournaments')}>Tournaments</MenuItem>
                    <MenuItem onClick={() => handleNavigate('/organizer/tournaments')}>Organizer Tournaments</MenuItem>
                </Menu>

                <Typography
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, cursor: 'pointer' }}
                    onClick={() => navigate('/login')}
                >
                    Rock-Com
                </Typography>

                <IconButton
                    edge="end"
                    color="inherit"
                    sx={{ ml: 2 }}
                    onClick={() => navigate('/')}
                >
                    <img src={Logo} alt="Logo" style={{ height: 40 }} />
                </IconButton>
            </Toolbar>
        </AppBar>
    );
};

export default TopBar;
