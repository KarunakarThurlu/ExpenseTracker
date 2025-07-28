export const getToken = () => localStorage.getItem("token");

export const isLoggedIn = () => {
  const token = getToken();
  if (!token) return false;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp;
    // Check if token is expired
    return Date.now() < exp * 1000;
  } catch (e) {
    console.error("Error parsing token or token is invalid:", e);
    // If parsing fails, the token is likely malformed or invalid
    localStorage.removeItem('token'); // Clean up invalid token
    localStorage.removeItem('user'); // Clean up associated user data
    return false;
  }
};
