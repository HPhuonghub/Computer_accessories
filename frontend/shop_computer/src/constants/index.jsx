const keys = require("../config/keys");

export const BASE_URL = "http://localhost:8888/";

export const API_BASE_URL = keys.apiBaseUrl;

export const ACCESS_TOKEN = "accessToken";

export const REFRESH_TOKEN = "refreshToken";

export const OAUTH2_REDIRECT_URI = keys.redirectUri;

export const GOOGLE_AUTH_URL =
  API_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;

export const FACEBOOK_AUTH_URL =
  API_BASE_URL +
  "/oauth2/authorize/facebook?redirect_uri=" +
  OAUTH2_REDIRECT_URI;
