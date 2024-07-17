import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import NavDropdown from "react-bootstrap/NavDropdown";
import { NavLink, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { FaUserCircle, FaSignOutAlt, FaUserCog } from "react-icons/fa"; // Import các icon từ Font Awesome
import {
  logout,
  selectUser,
  selectIsLoggedIn,
} from "../../redux/slices/authSlice";

const Header = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isLoggedIn = useSelector(selectIsLoggedIn);
  const user = useSelector(selectUser);

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

  return (
    <Navbar expand="lg" bg="primary" variant="dark">
      <Container>
        <NavLink to="/" className="navbar-brand">
          Shop Computer
        </NavLink>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <NavLink to="/" className="nav-link">
              Home
            </NavLink>
            <NavLink to="/user" className="nav-link">
              Users
            </NavLink>
          </Nav>
          <Nav>
            {!isLoggedIn ? (
              <>
                <button
                  className="btn btn-light me-2"
                  onClick={() => handleLogin()}
                >
                  Log in
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
    </Navbar>
  );
};

export default Header;
