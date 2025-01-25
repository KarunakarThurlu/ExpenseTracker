import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider } from '@mui/material/styles'
import './index.css'
import theme from './theme.js'
import App from './App.jsx'
import { ApolloClientProvider } from './Graphql/ApolloClientProvider.js'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ApolloClientProvider>
      <ThemeProvider theme={theme}>
        <App />
      </ThemeProvider>
    </ApolloClientProvider>
  </StrictMode>,
)
