import "./Login.scss";
import { postLogin } from "../../services/UserService";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";

const Login = () => {
  const navigate = useNavigate();
  const [emailLogin, setEmailLogin] = useState("");
  const [passwordLogin, setPasswordLogin] = useState("");
  const [keepSignedIn, setKeepSignedIn] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = async () => {
    let res = await postLogin(emailLogin, passwordLogin);
    if (res && res.data.status !== 400) {
      toast.success(res.data.message);
      navigate("/");
    }

    if (res && res.data.status === 400) {
      toast.error(res.data.message);
    }
  };

  const handleKeepSignedInChange = () => {
    setKeepSignedIn(!keepSignedIn); // Đảo ngược giá trị của keepSignedIn
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="login-wrap">
      <div className="login-html">
        <label htmlFor="tab-1" className="tab">
          Sign In
        </label>
        <div className="login-form">
          <div className="sign-in-htm">
            <div className="group">
              <label htmlFor="user" className="label">
                USERNAME
              </label>
              <input
                id="user"
                type="text"
                className="input"
                placeholder="Enter your username"
                onChange={(event) => setEmailLogin(event.target.value)}
              />
            </div>
            <div className="group">
              <label htmlFor="pass" className="label">
                PASSWORD
              </label>
              <div className="password-input-container">
                <input
                  id="pass"
                  type={showPassword ? "text" : "password"}
                  className="input"
                  placeholder="Enter your password"
                  data-type="password"
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
                id="check"
                type="checkbox"
                className="check"
                checked={keepSignedIn}
                onChange={handleKeepSignedInChange}
              />
              <label htmlFor="check">
                <span className="icon"></span> Keep me Signed in
              </label>
            </div>
            <div className="group">
              <input
                type="submit"
                className="button"
                value="Sign In"
                onClick={handleLogin}
              />
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
  );
};

export default Login;
