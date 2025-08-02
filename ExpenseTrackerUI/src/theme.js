import { createTheme } from '@mui/material/styles';

const getDesignTokens = (mode) => ({
  palette: {
    mode,
    ...(mode === 'dark' && {
      background: {
        default: '#121212',
      },
    }),
  },
  components: {
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
            borderColor: '#fbc600',
          },
          '&:hover .MuiOutlinedInput-notchedOutline': {
            borderColor: '#fbc600',
          },
        },
        input: {
          ...(mode === 'dark' && {
            '&::-webkit-calendar-picker-indicator': {
              filter: 'invert(1)',
            },
          }),
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          '&.MuiButton-outlined': {
            borderColor: '#fbc600',
            color: '#fbc600',
          },
          '&.MuiButton-outlined:hover': {
            borderColor: '#fbc600',
            backgroundColor: 'rgba(251, 198, 0, 0.1)',
          },
          '&.MuiButton-contained': {
            backgroundColor: '#fbc600',
            color: '#fff',
          },
          '&.MuiButton-contained:hover': {
            backgroundColor: '#e6b800',
          },
        },
      },
    },
  },
});

export const getTheme = (mode) => createTheme(getDesignTokens(mode));
