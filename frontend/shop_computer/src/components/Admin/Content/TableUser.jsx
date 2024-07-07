import React from "react";

const TableUser = (props) => {
  const { listUsers, handleUpdateUser, handleDeleteUser, handleViewUser } =
    props;

  return (
    <>
      <div> Table users</div>
      <table className="table table-hover table-bordered">
        <thead>
          <tr>
            <th scope="col">STT</th>
            <th scope="col">Fullname</th>
            <th scope="col">Email</th>
            <th scope="col">Phone</th>
            <th scope="col">Address</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {listUsers &&
            listUsers.map((item, index) => (
              <tr key={index}>
                <td>{index + 1}</td>
                <td>{item.fullName}</td>
                <td>{item.email}</td>
                <td>{item.phone}</td>
                <td>{item.address}</td>
                <td>
                  <button
                    className="btn btn-secondary"
                    onClick={() => handleViewUser(item.id)}
                  >
                    View
                  </button>
                  <button
                    className="btn btn-warning mx-3"
                    onClick={() => handleUpdateUser(item.id)} // Pass item.id to setId
                  >
                    Update
                  </button>
                  <button
                    className="btn btn-danger"
                    onClick={() => handleDeleteUser(item.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          {listUsers && listUsers.length === 0 && (
            <tr>
              <td colSpan={6}>Not found data</td>
            </tr>
          )}
        </tbody>
      </table>
    </>
  );
};

export default TableUser;
