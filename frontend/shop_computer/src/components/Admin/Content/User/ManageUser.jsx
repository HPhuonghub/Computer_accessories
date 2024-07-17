import ModalForm from "../ModalForm";
import { FcPlus } from "react-icons/fc";
import "./ManageUser.scss";
import TableUser from "./TableUser";
import { useEffect, useState } from "react";
import {
  getAllUsers,
  deleteUser,
  getAllUsersWithPaginate,
} from "../../../../services/UserService";
import ModalConfirm from "../ModalConfirm";
import { toast } from "react-toastify";
import TableUserPaginate from "./TableUserPaginate";

const ManageUser = () => {
  const LIMIT_USER = 5;
  const [pageCount, setPageCount] = useState(0);
  const [currentPage, setcurrentPage] = useState(1);

  const [showModal, setShowModal] = useState(false);
  const [listUsers, setListUsers] = useState([]);
  const [id, setId] = useState(0);
  const [userID, setUserID] = useState(0);
  const [viewId, setViewId] = useState(false);
  const [modalDeteleUser, setModalDeleteUser] = useState(false);
  const [reloadUsers, setReloadUsers] = useState(false);

  useEffect(() => {
    //fetchListUser();
    fetchListUserPaginate(1);
  }, [reloadUsers]);

  const fetchListUser = async () => {
    let res = await getAllUsers();
    setListUsers(res.data.data.items);
    console.log(res);
  };

  const fetchListUserPaginate = async (page) => {
    let res = await getAllUsersWithPaginate(page, LIMIT_USER);
    if (res.data.status === 200) {
      setListUsers(res.data.data.items);
      setPageCount(res.data.data.totalPage);
      console.log(res);
    }
  };

  const handleUpdateUser = (userId) => {
    setShowModal(true);
    setId(userId);
  };

  const handleDeleteUser = (userId) => {
    console.log("check handleDeleteUser");
    setUserID(userId);
    setModalDeleteUser(true);
  };

  const handleModalDeleteUser = async () => {
    let res = await deleteUser(userID);
    if (res) {
      toast.success(res.data.message);
      setcurrentPage(1);
    }
    setReloadUsers(!reloadUsers);
  };

  const handleViewUser = (userId) => {
    console.log("check handleViewUser");
    setShowModal(true);
    setId(userId);
    setViewId(true);
  };

  return (
    <div className="manage-user-container">
      <div className="title">Manage user</div>
      <div className="users-content">
        <div>
          <button
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            <FcPlus /> Add new users
          </button>
          {showModal && (
            <ModalForm
              show={true} // Show modal when id !== 0
              setShow={setShowModal}
              fetchListUser={fetchListUser}
              id={id}
              setId={setId}
              viewId={viewId}
              setViewId={setViewId}
              fetchListUserPaginate={fetchListUserPaginate}
              currentPage={currentPage}
            />
          )}
        </div>
        <div className="table-container">
          {/* <TableUser
            listUsers={listUsers}
            handleUpdateUser={handleUpdateUser}
            handleDeleteUser={handleDeleteUser}
            handleViewUser={handleViewUser}
          /> */}
          <TableUserPaginate
            listUsers={listUsers}
            handleUpdateUser={handleUpdateUser}
            handleDeleteUser={handleDeleteUser}
            handleViewUser={handleViewUser}
            fetchListUserPaginate={fetchListUserPaginate}
            pageCount={pageCount}
            currentPage={currentPage}
            setcurrentPage={setcurrentPage}
          />
        </div>
        {modalDeteleUser && (
          <ModalConfirm
            show={true}
            setShow={setModalDeleteUser}
            handleModalDeleteUser={handleModalDeleteUser}
          />
        )}
      </div>
    </div>
  );
};

export default ManageUser;
