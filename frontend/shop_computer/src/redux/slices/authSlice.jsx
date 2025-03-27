import { createSlice } from "@reduxjs/toolkit";
import { jwtDecode } from "jwt-decode";
import { ACCESS_TOKEN, REFRESH_TOKEN } from "../../constants/index";
import axios from "axios";

const initialState = {
  isLoggedIn: false,
  user: null,
  isLoading: false,
  error: null,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    loginStart: (state) => {
      state.isLoading = true;
      state.error = null;
    },
    loginSuccess: (state, action) => {
      state.isLoggedIn = true;
      state.isLoading = false;
      state.user = action.payload.user;
    },
    loginFailure: (state, action) => {
      state.isLoading = false;
      state.error = action.payload;
    },
    logout: (state) => {
      state.isLoggedIn = false;
      state.user = null;
    },
    setUser: (state, action) => {
      state.isLoggedIn = true; // Set isLoggedIn to true
      state.user = action.payload;
    },
  },
});

export const { loginStart, loginSuccess, loginFailure, logout, setUser } =
  authSlice.actions;

export const selectIsLoggedIn = (state) => state.auth.isLoggedIn;
export const selectUser = (state) => state.auth.user;

export const loginUser = (username, password) => async (dispatch) => {
  dispatch(loginStart());
  try {
    const response = await axios.post(
      "http://localhost:8888/api/v1/auth/login",
      { username, password }
    );
    console.log("check response.data", response.data);
    const accessToken = response.data.accessToken;
    const refreshToken = response.data.refreshToken;

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
          role: decodedToken.role[0],
          email: decodedToken.sub,
          // Add other fields as needed
        };
        localStorage.setItem("email", user.email);
        dispatch(loginSuccess({ user }));
      } catch (error) {
        console.error("Error decoding AccessToken:", error);
      }
    }
  } catch (error) {
    dispatch(loginFailure(error.message));
  }
};

export default authSlice.reducer;
