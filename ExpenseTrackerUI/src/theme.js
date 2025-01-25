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
    },
});

export default theme;
