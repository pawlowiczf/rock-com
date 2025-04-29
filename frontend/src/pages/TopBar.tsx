import React from 'react';
import { AppBar, Toolbar, Typography, IconButton } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import "../styles/TopBar.css";


const TopBar: React.FC = () => {
    return (
        <AppBar position="static" color='default'>
            <Toolbar >
                <IconButton edge="start" color="inherit" aria-label="menu" sx={{ mr: 2 }}>
                    <MenuIcon />
                </IconButton>
                <Typography 
                    variant="h6" 
                    component="div" 
                    sx={{ flexGrow: 1, cursor: 'pointer' }} 
                    onClick={() => window.location.href = '/login'}
                >
                    Rock-Com
                </Typography>
            </Toolbar>
        </AppBar>
    );
};

export default TopBar;