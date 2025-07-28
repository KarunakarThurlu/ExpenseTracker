
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Drawer, Menu, MenuItem } from '@mui/material';
import { AccountCircle } from '@mui/icons-material';
import SideMenu from './SideMenu';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function AppHeader() {

    const navigate = useNavigate();

    const [state, setState] = useState({ drawerOpen: false });
    const [auth, setAuth] = useState(true);
    const [anchorEl, setAnchorEl] = useState(null);


    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };
    const toggleDrawer = (open) => (event) => {
        setState(prevState => ({ ...prevState, drawerOpen: open }))
    };
    const handleLogout = () => {
        localStorage.removeItem('token');
       //setAuth(false);
       navigate('/signin');
    };

    // useEffect(() => {
    //     if (auth) {
    //         navigate('/signin');
    //     }
    // }, [auth]);

    return (
        <>
            <Box sx={{ flexGrow: 1 }}>
                <AppBar position="sticky" sx={{ backgroundColor: '#fbc600', color: 'black' }}>
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{ mr: 2 }}
                            onClick={toggleDrawer(true)}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                            Expense Tracker
                        </Typography>
                        <div>
                            <IconButton
                                size="large"
                                aria-label="account of current user"
                                aria-controls="menu-appbar"
                                aria-haspopup="true"
                                onClick={handleMenu}
                                color="inherit"
                            >
                                <AccountCircle />
                            </IconButton>
                            <Menu
                                id="menu-appbar"
                                anchorEl={anchorEl}
                                anchorOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                keepMounted
                                transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'right',
                                }}
                                open={Boolean(anchorEl)}
                                onClose={handleClose}
                            >
                                {/* <MenuItem onClick={handleClose}>Profile</MenuItem>
                                <MenuItem onClick={handleClose}>My account</MenuItem> */}
                                <MenuItem onClick={handleLogout}>Logout</MenuItem>
                            </Menu>
                        </div>
                    </Toolbar>
                </AppBar>
            </Box>
            <Drawer anchor="left" open={state.drawerOpen} onClose={toggleDrawer(false)}  >
                <SideMenu onClose={toggleDrawer(false)} sx={{ backgroundColor: '#fbc600', color: 'black' }} />
            </Drawer>
        </>

    );
}
