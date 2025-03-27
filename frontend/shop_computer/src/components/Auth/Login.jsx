import "./Login.scss";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import { faGoogle, faFacebook } from "@fortawesome/free-brands-svg-icons";
import { useDispatch } from "react-redux";
import { loginUser } from "../../redux/slices/authSlice";
import { toast } from "react-toastify";
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from "../../constants";

const Login = () => {
  const navigate = useNavigate();
  const [emailLogin, setEmailLogin] = useState("");
  const [passwordLogin, setPasswordLogin] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const dispatch = useDispatch();

  const handleLogin = () => {
    if (!emailLogin) {
      toast.error("Please fill in the email field!");
      return;
    }
    if (!passwordLogin) {
      toast.error("Please fill in the password field!");
      return;
    }
    dispatch(loginUser(emailLogin, passwordLogin));
    localStorage.setItem("loggedInUser", JSON.stringify({ email: emailLogin }));
    setTimeout(() => {
      navigate("/");
    }, 1500);
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleLoginGoogle = () => {
    window.location.href = GOOGLE_AUTH_URL;
  };

  const handleLoginFacebook = () => {
    window.location.href = FACEBOOK_AUTH_URL;
  };

  return (
    <div className="background">
      <div className="login-wrap">
        <div className="login-html">
          <label htmlFor="tab-1" className="tab">
            Sign In
          </label>
          <div className="login-form">
            <div className="sign-in-htm">
              <div className="group">
                <label htmlFor="user" className="label">
                  Username
                </label>
                <input
                  id="user"
                  type="text"
                  className="input"
                  placeholder="Enter your username"
                  value={emailLogin}
                  onChange={(event) => setEmailLogin(event.target.value)}
                />
              </div>
              <div className="group">
                <label htmlFor="pass" className="label">
                  Password
                </label>
                <div className="password-input-container">
                  <input
                    id="pass"
                    type={showPassword ? "text" : "password"}
                    className="input"
                    placeholder="Enter your password"
                    value={passwordLogin}
                    onChange={(event) => setPasswordLogin(event.target.value)}
                  />
                  <FontAwesomeIcon
                    icon={showPassword ? faEyeSlash : faEye}
                    className="password-toggle-icon"
                    onClick={togglePasswordVisibility}
                  />
                </div>
              </div>
              <div className="group">
                <input
                  type="submit"
                  className="button"
                  value="Sign In"
                  onClick={handleLogin}
                />
              </div>
              <div className="login-icons">
                <button className="google-login" onClick={handleLoginGoogle}>
                  <FontAwesomeIcon icon={faGoogle} /> Sign In with Google
                </button>
                <button
                  className="facebook-login"
                  onClick={handleLoginFacebook}
                >
                  <FontAwesomeIcon icon={faFacebook} /> Sign In with Facebook
                </button>
              </div>
              <div className="hr"></div>
              <div className="foot-lnk d-flex justify-content-between">
                <Link to="/forgot-password">Forgot Password?</Link>
                <Link to="/register">Create an account</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
