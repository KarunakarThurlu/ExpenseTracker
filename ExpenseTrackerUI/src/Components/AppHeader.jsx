import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import LightModeIcon from '@mui/icons-material/LightMode';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Drawer, Menu, MenuItem} from '@mui/material';
import { AccountCircle } from '@mui/icons-material';
import SideMenu from './SideMenu';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ChangePassword from './PopUpModels/ChangePassword';
import { useThemeMode } from '../Utils/ThemeModeProvider';


export default function AppHeader() {

const { mode, toggleTheme } = useThemeMode();
    const navigate = useNavigate();

    const [state, setState] = useState({
        drawerOpen: false,
        showChangePassword: false
    });

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
        setAnchorEl(null);
        localStorage.removeItem('token');
        navigate('/signin');
    };

    const handleChangePassword = () => {
        setAnchorEl(null);
        setState(prevState => ({ ...prevState, showChangePassword: true }))
    }
    const hideChangePassword = () => {
        setState(prevState => ({ ...prevState, showChangePassword: false }))
    }
    const toggleDarkMode = () => {
        toggleTheme()
        setAnchorEl(null);
    };


    return (
        <>
            {state.showChangePassword && <ChangePassword open={state.showChangePassword} onClose={hideChangePassword}  />}
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
                                <MenuItem onClick={toggleDarkMode}>
                                    <IconButton color="inherit">
                                        {mode==='dark' ? <LightModeIcon /> : <DarkModeIcon />}
                                    </IconButton>
                                    {mode==='light' ? "Light Mode" : "Dark Mode"}
                                </MenuItem>
                                <MenuItem onClick={handleChangePassword}>Change Password</MenuItem>
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
