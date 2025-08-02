import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider, CssBaseline, IconButton } from '@mui/material';
import './index.css'
import App from './App.jsx'
import { ApolloClientProvider } from './Graphql/ApolloClientProvider.jsx'
import { Notifyer } from './Utils/Notifyer';
import { ThemeModeProvider } from './Utils/ThemeModeProvider.jsx';


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ApolloClientProvider>
      <ThemeModeProvider>
        <CssBaseline/>
        <Notifyer>
          <App />
        </Notifyer>
      </ThemeModeProvider>
    </ApolloClientProvider>
  </StrictMode>,
)
