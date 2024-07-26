import React, { useState } from "react";
import { toast } from "react-toastify";
import { postRegister } from "../../services/UserService";
import "./Register.scss";
import { useNavigate, Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";

const Register = () => {
  const navigate = useNavigate();
  const [fullnameRegister, setFullnameRegister] = useState("");
  const [emailRegister, setEmailRegister] = useState("");
  const [passwordRegister, setPasswordRegister] = useState("");
  const [repeatPasswordRegister, setRepeatPasswordRegister] = useState("");
  const [showPassword, setShowPassword] = useState(false); // State để điều khiển hiển thị password
  const [showRepeatPassword, setShowRepeatPassword] = useState(false); // State để điều khiển hiển thị repeat password

  const handleRegister = async () => {
    if (
      !fullnameRegister ||
      !emailRegister ||
      !passwordRegister ||
      !repeatPasswordRegister
    ) {
      toast.error("Please fill in all fields");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(emailRegister)) {
      toast.error("Invalid email format");
      return;
    }

    if (passwordRegister.length < 6) {
      toast.error("Password must be at least 6 characters long");
      return;
    }

    if (passwordRegister !== repeatPasswordRegister) {
      toast.error("Passwords do not match");
      return;
    }

    let res = await postRegister(
      fullnameRegister,
      emailRegister,
      passwordRegister
    );
    if (res && res.data.status !== 400) {
      toast.success(res.data.message);
      navigate("/login");
    }

    if (res && res.data.status === 400) {
      toast.error(res.data.message);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const toggleRepeatPasswordVisibility = () => {
    setShowRepeatPassword(!showRepeatPassword);
  };

  return (
    <div className="background">
      <div className="login-wrap">
        <div className="login-html">
          <label htmlFor="tab-2" className="tab">
            Sign Up
          </label>
          <div className="login-form">
            <div className="sign-up-htm">
              <div className="group">
                <label htmlFor="user" className="label">
                  Full Name
                </label>
                <input
                  id="user"
                  type="text"
                  className="input"
                  placeholder="Enter your Fullname"
                  value={fullnameRegister}
                  onChange={(e) => setFullnameRegister(e.target.value)}
                />
              </div>
              <div className="group">
                <label htmlFor="pass" className="label">
                  Email Address
                </label>
                <input
                  id="pass"
                  type="text"
                  className="input"
                  placeholder="Enter your email"
                  onChange={(e) => setEmailRegister(e.target.value)}
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
                    data-type="password"
                    placeholder="Enter your password"
                    onChange={(e) => setPasswordRegister(e.target.value)}
                  />
                  <FontAwesomeIcon
                    icon={showPassword ? faEyeSlash : faEye}
                    className="password-toggle-icon"
                    onClick={togglePasswordVisibility}
                  />
                </div>
              </div>
              <div className="group">
                <label htmlFor="pass" className="label">
                  Repeat Password
                </label>
                <div className="password-input-container">
                  <input
                    id="pass"
                    type={showRepeatPassword ? "text" : "password"}
                    className="input"
                    data-type="password"
                    placeholder="Enter your repeat password"
                    onChange={(e) => setRepeatPasswordRegister(e.target.value)}
                  />
                  <FontAwesomeIcon
                    icon={showRepeatPassword ? faEyeSlash : faEye}
                    className="password-toggle-icon"
                    onClick={toggleRepeatPasswordVisibility}
                  />
                </div>
              </div>

              <div className="group">
                <input
                  type="submit"
                  className="button"
                  value="Sign Up"
                  onClick={() => handleRegister()}
                />
              </div>
              <div className="hr"></div>
              <div className="foot-lnk d-flex justify-content-center">
                <label htmlFor="tab-1" />
                <Link to="/login">Already Member?</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
