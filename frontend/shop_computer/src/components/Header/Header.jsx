import React, { useState } from "react";
import Container from "react-bootstrap/Container";
import "./Header.scss";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import logo from "../../assets/images/logo.png";
import NavDropdown from "react-bootstrap/NavDropdown";
import { NavLink, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  FaUserCircle,
  FaSignOutAlt,
  FaUserCog,
  FaSearch,
  FaShoppingCart,
} from "react-icons/fa"; // Import các icon từ Font Awesome
import {
  logout,
  selectUser,
  selectIsLoggedIn,
} from "../../redux/slices/authSlice";
import { selectCartItems } from "../../redux/slices/cartSlice";
import Cart from "./Cart";

const Header = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isLoggedIn = useSelector(selectIsLoggedIn);
  const user = useSelector(selectUser);
  const cartItemCount = useSelector(selectCartItems);
  const [showCartPopup, setShowCartPopup] = useState(false);

  const toggleCartPopup = () => {
    setShowCartPopup(!showCartPopup);
  };

  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
  };

  const handleLogin = () => {
    navigate("/login");
  };

  const handleAdmin = () => {
    navigate("/admin");
  };

  const calculateTotalQuantity = () => {
    let totalQuantity = 0;
    cartItemCount.forEach((item) => {
      const itemQuantity = item.quantity;
      totalQuantity += itemQuantity;
    });
    return totalQuantity;
  };

  console.log("check user", user);

  return (
    <Navbar
      expand="lg"
      variant="dark"
      style={{ backgroundColor: "rgb(96 93 93)", opacity: 1, color: "white" }}
    >
      <Container>
        <NavLink
          to="/"
          className="navbar-brand"
          style={{ display: "flex", alignItems: "center" }}
        >
          <img
            src={logo}
            width="30"
            height="30"
            className="d-inline-block align-top"
            alt="Shop Computer Logo"
          />
          <div style={{ marginLeft: "5px", color: "black" }}>Shop Computer</div>
        </NavLink>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mx-auto">
            <div className="row align-items-center" style={{ width: "450px" }}>
              <div className="col">
                <div className="input-group">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Search..."
                  />
                  <button
                    className="btn btn-outline-secondary"
                    type="button"
                    style={{ backgroundColor: "#f9bb01" }}
                  >
                    <FaSearch color="white" />
                  </button>
                </div>
              </div>
            </div>
          </Nav>
          <Nav>
            <div className="d-flex align-items-center ms-auto">
              <div
                className="position-relative"
                style={{
                  marginRight: "30px",
                  marginTop: "5px",
                  cursor: "pointer",
                }}
              >
                <FaShoppingCart
                  style={{
                    fontSize: "1.5rem",
                    marginRight: "15px",
                  }}
                  onClick={() => toggleCartPopup()}
                />
                <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                  <span className="visually-hidden">Cart items</span>

                  <span>{cartItemCount ? calculateTotalQuantity() : 0}</span>
                </span>
              </div>
            </div>
            {!isLoggedIn ? (
              <>
                <button
                  className="btn btn-light me-2"
                  onClick={() => handleLogin()}
                >
                  Sign in
                </button>
                <button className="btn btn-light">Sign up</button>
              </>
            ) : (
              <NavDropdown
                title={
                  <>
                    <FaUserCircle /> Welcome, {user.fullname}
                  </>
                }
                id="basic-nav-dropdown"
              >
                <NavDropdown.Item>
                  <FaUserCircle /> Profile
                </NavDropdown.Item>
                {user.role.name === "ADMIN" && (
                  <NavDropdown.Item onClick={handleAdmin}>
                    <FaUserCog /> Admin
                  </NavDropdown.Item>
                )}
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={handleLogout}>
                  <FaSignOutAlt /> Log out
                </NavDropdown.Item>
              </NavDropdown>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>

      {showCartPopup && <Cart setShowCartPopup={setShowCartPopup} />}
    </Navbar>
  );
};

export default Header;
