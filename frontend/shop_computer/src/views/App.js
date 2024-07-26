import "./App.scss";
import Header from "../components/Header/Header";
import { useEffect } from "react";
import { Outlet } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { selectIsLoggedIn, loginUser } from "../redux/slices/authSlice";

function App() {
  const dispatch = useDispatch();
  const isLoggedIn = useSelector(selectIsLoggedIn);

  useEffect(() => {
    // Kiểm tra nếu có thông tin đăng nhập trong Local Storage thì dispatch action để set lại state
    const loggedInUser = localStorage.getItem("loggedInUser");
    if (loggedInUser && !isLoggedIn) {
      const user = JSON.parse(loggedInUser);
      dispatch(loginUser(user.username, user.password)); // Thay username và password bằng thông tin cần thiết
    }
  }, [dispatch, isLoggedIn]);

  return (
    <div className="app-container">
      <div
        className="header-container"
        style={{
          position: "fixed",
          height: "80px",
          top: 0,
          width: "100%",

          zIndex: 2,
        }}
      >
        <Header />
      </div>
      <div className="main-container" style={{ paddingTop: "80px" }}>
        <div className="sidenav-container"></div>
        <div className="app-content">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

export default App;
