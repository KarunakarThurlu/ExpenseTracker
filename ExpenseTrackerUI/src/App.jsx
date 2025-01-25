import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css'
import Dashboard from './Pages/Dashboard';
import Signin from './Pages/Signin';
import AppHeader from './Components/AppHeader';
import Signup from './Pages/Signup';
import Expenses from './Components/Expenses';

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/signin" element={<Signin />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/*" element={
          <>
            <AppHeader />
            <Routes>
              <Route path='/dashboard' element={<Dashboard />} />
              <Route path='/expenses' element={<Expenses />} />
            </Routes>
          </>
        } />
      </Routes>
    </Router>
  )
}

export default App
