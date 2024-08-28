import Button from "react-bootstrap/Button";
import { useEffect, useState } from "react";
import Modal from "react-bootstrap/Modal";
import { toast } from "react-toastify";
import { FaAsterisk } from "react-icons/fa";
import {
  postCreateNewUser,
  putUpdateUsers,
  getUserId,
} from "../../../services/UserService";

const ModalForm = (props) => {
  const {
    show,
    setShow,
    id,
    setId,
    fetchListUser,
    viewId,
    setViewId,
    fetchListUserPaginate,
    currentPage,
  } = props;

  const [email, setEmail] = useState("");
  const [fullName, setFullName] = useState("");
  const [password, setPassword] = useState("");
  const [againPassword, setAgainPassword] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
  const [role, setRole] = useState("USER");
  const [state, setState] = useState("ACTIVE");

  const [userId, setUserId] = useState(null);

  const handleClose = () => {
    setShow(false);
    setEmail("");
    setFullName("");
    setPassword("");
    setAddress("");
    setAgainPassword("");
    setPhone("");
    setRole("USER");
    setState("ACTIVE");
    setId(0);
    setUserId(null);
    setViewId(false);
  };

  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );
  };

  const handleCreateUser = async () => {
    //validate
    if (!fullName) {
      toast.error("Full name field is empty");
      return;
    }

    if (!email) {
      toast.error("Email field is empty");
      return;
    }

    const isValidEmail = validateEmail(email);
    if (!isValidEmail) {
      toast.error("Invalid email");
      return;
    }

    if (!password) {
      toast.error("Password field is empty");
      return;
    }

    if (!againPassword) {
      toast.error("Again password field is empty");
      return;
    }

    if (!phone) {
      toast.error("Phone field is empty");
      return;
    }

    if (!address) {
      toast.error("Address field is empty");
      return;
    }

    if (password !== againPassword) {
      toast.error("Passwords must match");
      return;
    }

    // Call API to create new user

    const response = await postCreateNewUser(
      email,
      fullName,
      password,
      address,
      phone,
      role,
      state
    );

    if (response.data && response.data.status === 201) {
      toast.success(response.data.message);
      handleClose();
      await fetchListUserPaginate(currentPage);
    } else if (response.data && response.data.status !== 201) {
      toast.error(response.data.message);
      handleClose();
    }
  };

  const handleUpdateUser = async () => {
    //validate
    if (!fullName) {
      toast.error("Full name field is empty");
      return;
    }
    if (!phone) {
      toast.error("Phone field is empty");
      return;
    }
    if (!address) {
      toast.error("Address field is empty");
      return;
    }

    // Call API to update user
    const response = await putUpdateUsers(
      id,
      email,
      fullName,
      address,
      phone,
      role,
      state
    );

    if (response.data && response.data.status === 202) {
      toast.success(response.data.message);
      handleClose();
      await fetchListUserPaginate(currentPage);
    } else if (response.data && response.data.status !== 202) {
      toast.error(response.data.message);
      handleClose();
    }
  };

  useEffect(() => {
    // Fetch user details if id is not 0
    if (id !== 0) {
      fetchUserId();
    }
  }, [id]); // Trigger useEffect when id changes

  const fetchUserId = async () => {
    try {
      const res = await getUserId(id);
      setUserId(res.data.data); // Set userId state with fetched user data

      // Fill form fields with user data
      setEmail(res.data.data.email);
      setFullName(res.data.data.fullName);
      setAddress(res.data.data.address);
      setPhone(res.data.data.phone);
      setRole("USER");
      setState("ACTIVE");
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  };

  return (
    <>
      <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
        <Modal.Header closeButton>
          {id === 0 ? (
            <Modal.Title>Add new a user</Modal.Title>
          ) : !viewId ? (
            <Modal.Title>Update user</Modal.Title>
          ) : (
            <Modal.Title>View a user</Modal.Title>
          )}
        </Modal.Header>

        <form className="row g-3" style={{ margin: "auto", paddingBottom: 10 }}>
          <div className="col-md-6">
            <label htmlFor="inputFullName4" className="form-label">
              <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
              FullName
            </label>
            <input
              type="text"
              className="form-control"
              id="inputFullName4"
              placeholder="Enter your full name"
              value={fullName}
              onChange={(event) => setFullName(event.target.value)}
              readOnly={!viewId ? false : true}
            />
          </div>

          {id === 0 ? (
            <>
              <div className="col-md-6">
                <label htmlFor="inputEmail4" className="form-label">
                  <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
                  Email
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="inputEmail4"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  readOnly={!viewId ? false : true}
                />
              </div>
              <div className="col-md-6">
                <label htmlFor="inputPassword4" className="form-label">
                  <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
                  Password
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="inputPassword4"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                />
              </div>

              <div className="col-md-6">
                <label htmlFor="inputAgainPassword4" className="form-label">
                  <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
                  Again Password
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="inputAgainPassword4"
                  placeholder="Enter your password again"
                  value={againPassword}
                  onChange={(event) => setAgainPassword(event.target.value)}
                />
              </div>
            </>
          ) : (
            <>
              {viewId ? (
                <div className="col-md-6">
                  <label htmlFor="inputEmail4" className="form-label">
                    <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
                    Email
                  </label>
                  <input
                    type="email"
                    className="form-control"
                    id="inputEmail4"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    readOnly={!viewId ? false : true}
                  />
                </div>
              ) : (
                <></>
              )}
            </>
          )}

          <div className="col-md-6">
            <label htmlFor="inputPhone" className="form-label">
              <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
              Phone
            </label>
            <input
              type="text"
              className="form-control"
              id="inputPhone"
              placeholder="Enter your phone"
              value={phone}
              onChange={(event) => setPhone(event.target.value)}
              readOnly={!viewId ? false : true}
            />
          </div>

          <div className="col-md-6">
            <label htmlFor="inputAddress" className="form-label">
              Address
            </label>
            <input
              type="text"
              className="form-control"
              id="inputAddress"
              placeholder="Enter your address"
              value={address}
              onChange={(event) => setAddress(event.target.value)}
              readOnly={!viewId ? false : true}
            />
          </div>

          <div className="col-md-4">
            <label htmlFor="inputRole" className="form-label">
              Role
            </label>
            <select
              id="inputRole"
              className="form-select"
              defaultValue={"DEFAULT"}
              onChange={(event) => setRole(event.target.value)}
              disabled={viewId}
            >
              <option value="DEFAULT">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </div>

          <div className="col-md-4">
            <label htmlFor="inputState" className="form-label">
              State
            </label>
            <select
              id="inputState"
              className="form-select"
              defaultValue={"DEFAULT"}
              onChange={(event) => setState(event.target.value)}
              disabled={viewId}
            >
              <option value="DEFAULT">ACTIVE</option>
              <option value="NONE">NONE</option>
            </select>
          </div>
        </form>

        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          {id === 0 ? (
            <Button variant="primary" onClick={handleCreateUser}>
              Save
            </Button>
          ) : !viewId ? (
            <Button variant="primary" onClick={handleUpdateUser}>
              Update
            </Button>
          ) : (
            <></>
          )}
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ModalForm;
