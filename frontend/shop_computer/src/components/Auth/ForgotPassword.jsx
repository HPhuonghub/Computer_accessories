import "./ForgotPassword.scss";
import { useState } from "react";
import { toast } from "react-toastify";
import { postForgotPassword } from "../../services/UserService";

const ForgetPassword = () => {
  const [email, setEmail] = useState("");

  const handleForgetPassword = async () => {
    if (!email) {
      toast.error("Please fill in the email field!");
      return;
    }

    const res = await postForgotPassword(email);
    console.log("check res forgot", res);
  };

  return (
    <div className="background">
      <div className="login-wrap">
        <div className="login-html">
          <label htmlFor="tab-1" className="tab">
            Forgot Password
          </label>
          <div className="login-form">
            <div className="sign-in-htm">
              <div className="group">
                <label htmlFor="user" className="label">
                  Email
                </label>
                <input
                  id="user"
                  type="email"
                  className="input"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                />
              </div>
              <div className="group">
                <div className="links-container">
                  <button
                    className="back-link"
                    onClick={() => {
                      // Handle button click, navigate to /login or perform other actions
                      window.location.href = "/login";
                    }}
                  >
                    Back to Login
                  </button>
                  <button
                    className="forgot-link"
                    onClick={() => handleForgetPassword()}
                  >
                    Forgot Password
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ForgetPassword;
