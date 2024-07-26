import { useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import { ACCESS_TOKEN, REFRESH_TOKEN } from "../../constants";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setUser } from "../../redux/slices/authSlice"; // Import action

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch(); // Initialize dispatch

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const accessToken = params.get("accessToken");
    const refreshToken = params.get("refreshToken");

    if (accessToken && refreshToken) {
      localStorage.setItem(ACCESS_TOKEN, accessToken);
      localStorage.setItem(REFRESH_TOKEN, refreshToken);

      try {
        // Decode the access token to extract user information
        const decodedToken = jwtDecode(accessToken);
        console.log("Decoded token:", decodedToken);

        // Extract user information from the decoded token
        const user = {
          fullname: decodedToken.sub,
          role: decodedToken.role,
          // Add other fields as needed
        };

        // Dispatch setUser action to save user info in Redux
        dispatch(setUser(user));

        // Navigate to the desired page
        navigate("/");
      } catch (error) {
        console.error("Error decoding AccessToken:", error);
        navigate("/login");
      }
    } else {
      navigate("/login");
    }
  }, [navigate, location.search, dispatch]);

  return null; // or loading indicator if needed
};

export default OAuth2RedirectHandler;
