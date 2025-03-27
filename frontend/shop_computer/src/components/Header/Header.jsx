import React, { useEffect, useState } from "react";
import Container from "react-bootstrap/Container";
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
} from "react-icons/fa";
import {
  logout,
  selectUser,
  selectIsLoggedIn,
} from "../../redux/slices/authSlice";
import {
  getSearch,
  getAllProductSearch,
} from "../../redux/slices/ProductSlice";
import { selectCartTotalItems } from "../../redux/slices/cartSlice";
import { ACCESS_TOKEN } from "../../constants/index";
import { jwtDecode } from "jwt-decode";
import Cart from "./Cart";
import "./Header.scss";
import { logOut } from "../../services/UserService";
import { toast } from "react-toastify";

const Header = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isLoggedIn = useSelector(selectIsLoggedIn);
  const user = useSelector(selectUser);
  const totalQuantity = useSelector(selectCartTotalItems);
  const [showCartPopup, setShowCartPopup] = useState(false);
  const [search, setSearch] = useState("");
  const [userHeader, setUserHeader] = useState();
  const [userId, setUserId] = useState();
  const [logout, setLogout] = useState(false);

  const toggleCartPopup = () => {
    setShowCartPopup(!showCartPopup);
  };

  const handleLogout = async () => {
    const res = await logOut();
    if (res.data.status === 200) {
      setLogout(true);
      setUserHeader();
      localStorage.removeItem(ACCESS_TOKEN);
      setTimeout(() => {
        toast.success("Logout user successfully");
        navigate("/");
      }, 1500);
    }
  };

  const handleLogin = () => {
    navigate("/login");
  };

  const handleAdmin = () => {
    navigate("/admin");
  };

  const handleSearch = () => {
    dispatch(getSearch(search));
    setTimeout(() => {
      navigate("/");
    }, 500);
    setSearch("");
  };

  const handleHome = () => {
    dispatch(getAllProductSearch(1, 8, ""));
  };

  const handleOrder = () => {
    navigate(`/view-checkout/${userId}`);
  };

  const isTokenValid = (token) => {
    const currentTime = Math.floor(Date.now() / 1000); // Thời gian hiện tại tính bằng giây
    return token.exp > currentTime; // So sánh thời gian hết hạn với thời gian hiện tại
  };

  useEffect(() => {
    const accessToken = localStorage.getItem(ACCESS_TOKEN);
    if (accessToken) {
      const decodedToken = jwtDecode(accessToken);
      if (isTokenValid(decodedToken)) {
        setUserId(decodedToken.id);
        const valid = {
          fullname: decodedToken.sub,
          role: decodedToken.role[0],
        };
        setUserHeader(valid);
      } else {
        setUserHeader();
      }
    } else {
      setUserHeader();
    }
  }, []);

  useEffect(() => {
    if (logOut) {
      setLogout(false);
    }
  }, [userHeader, logout]);

  return (
    <Navbar expand="lg" variant="dark" className="header-navbar">
      <Container className="justify-content-around">
        {/* <NavLink to="/" className="navbar-brand"> */}
        <NavLink to="/" className="navbar-brand" onClick={handleHome}>
          <img src={logo} width="30" height="30" alt="Shop Computer Logo" />
          <div className="navbar-brand-title">Shop Computer</div>
        </NavLink>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mx-auto">
            <div className="search-container">
              <input
                type="text"
                className="form-control"
                placeholder="Search..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
              <button
                className="btn btn-outline-secondary btn-search"
                type="button"
                onClick={() => handleSearch()}
              >
                <FaSearch color="white" />
              </button>
            </div>
          </Nav>
          <Nav>
            <div className="cart-container">
              <FaShoppingCart
                className="icon-shopping-cart"
                onClick={() => toggleCartPopup()}
              />
              <span className="badge rounded-pill bg-danger">
                {totalQuantity || 0}
              </span>
            </div>
            {!userHeader ? (
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
                    <FaUserCircle /> Welcome,{" "}
                    {userHeader ? userHeader.fullname : ""}
                  </>
                }
                id="basic-nav-dropdown"
              >
                <NavDropdown.Item>
                  <FaUserCircle /> Profile
                </NavDropdown.Item>
                {userHeader && userHeader.role === "ADMIN" && (
                  <NavDropdown.Item onClick={handleAdmin}>
                    <FaUserCog /> Admin
                  </NavDropdown.Item>
                )}
                <NavDropdown.Item onClick={handleOrder}>
                  <FaUserCircle /> Order
                </NavDropdown.Item>
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
