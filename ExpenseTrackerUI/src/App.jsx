import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import './App.css';
import Signin from './Pages/Signin';
import Signup from './Pages/Signup';
import AppHeader from './Components/AppHeader';
import Expenses from './Components/Expenses/Expenses';
import PrivateRoute from './Utils/PrivateRoute';
import { isLoggedIn } from './Utils/Auth';
import Tenants from './Components/Tenants/Tenants';
import Users from './Components/Users/Users';
import Dashboard from './Components/DashBoard/Dashboard';
import Profile from './Pages/Profile';


function App() {

  return (
    <Router>
      <Routes>
                {/* Root path redirect */}
        <Route path="/" element={<Navigate to={isLoggedIn() ? "/dashboard" : "/signin"} replace />} />

        {/* ✅ Redirect from /signin if already logged in */}
        <Route path="/signin" element={isLoggedIn() ? <Navigate to="/dashboard" replace /> : <Signin />} />
        <Route path="/signup" element={isLoggedIn() ? <Navigate to="/dashboard" replace /> : <Signup />} />

        <Route
          path="/*"
          element={
            <PrivateRoute>
              <>
                <AppHeader />
                <Routes>
                  <Route path="/dashboard" element={<Dashboard />} />
                  <Route path="/expenses" element={<Expenses />} />
                  <Route path="/tenants" element={<Tenants />} />
                  <Route path="/users" element={<Users />} />
                  <Route path="/profile" element={<Profile />} />
                  {/* Add more routes as needed */}
                </Routes>
              </>
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
