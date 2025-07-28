// src/theme.js
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    components: {
        MuiOutlinedInput: {
            styleOverrides: {
                root: {
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#fbc600', // Set the yellow outline on focus
                    },
                    '&:hover .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#fbc600', // Optional: yellow outline on hover
                    }
                },
            },
        },
        MuiInputLabel: {
            styleOverrides: {
                root: {
                    '&.Mui-focused': {
                        color: '#3b3d49', // Optional: Set label color to yellow on focus
                    },
                },
            },
        },
        //I want to add Buttons color to yellow
        MuiButton: {    
            styleOverrides: {
                root: {
                    '&.MuiButton-outlined': {
                        borderColor: '#fbc600', // Set the yellow border for outlined buttons
                        color: '#fbc600', // Set the text color to yellow
                    },
                    '&.MuiButton-outlined:hover': {
                        borderColor: '#fbc600', // Keep the border color on hover
                        backgroundColor: 'rgba(251, 198, 0, 0.1)', // Optional: light yellow background on hover
                    },
                    '&.MuiButton-contained': {
                        backgroundColor: '#fbc600', // Set the background color for contained buttons
                        color: '#fff', // Set the text color to white
                    },
                    '&.MuiButton-text': {
                        backgroundColor: '#fbc600', // Set the background color for contained buttons
                        color: '#fff', // Set the text color to white
                    },
                    '&.MuiButton-contained:hover': {
                        backgroundColor: '#e6b800', // Darker yellow on hover
                    },
                },
            },
        },
    },
});

export default theme;
