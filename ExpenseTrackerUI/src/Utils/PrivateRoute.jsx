import { Navigate } from 'react-router-dom';
import { isLoggedIn } from '../Utils/Auth'; // Adjust the import path as necessary

const PrivateRoute = ({ children }) => {
  return isLoggedIn() ? children : <Navigate to="/signin" replace />;
};

export default PrivateRoute;
