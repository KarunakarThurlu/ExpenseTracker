import React, { createContext, useContext, useState, useCallback } from 'react';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

const NotifierContext = createContext(null);

export const useNotifier = () => useContext(NotifierContext);

export const Notifyer = ({children }) => {

    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState('');
    const [severity, setSeverity] = useState('info'); // 'success' | 'error' | 'warning' | 'info'

    const showNotification = useCallback((msg, level = 'info') => {
        setMessage(msg);
        setSeverity(level);
        setOpen(true);
    }, []);

    const handleClose = (_, reason) => {
        if (reason === 'clickaway') return;
        setOpen(false);
    };

    return (
        <NotifierContext.Provider value={{ showNotification }}>
            {children}
            <Snackbar
                open={open}
                autoHideDuration={9000}
                // onClose={handleClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
                <Alert
                    onClose={handleClose}
                    severity={severity}
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    {message}
                </Alert>
            </Snackbar>
        </NotifierContext.Provider>
    )
}
