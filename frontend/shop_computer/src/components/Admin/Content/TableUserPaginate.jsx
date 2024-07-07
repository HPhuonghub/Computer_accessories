import React, { useEffect, useState } from "react";
import ReactPaginate from "react-paginate";

const TableUserPaginate = (props) => {
  const {
    listUsers,
    handleUpdateUser,
    handleDeleteUser,
    handleViewUser,
    pageCount,
    currentPage,
    setcurrentPage,
  } = props;

  const handlePageClick = (event) => {
    props.fetchListUserPaginate(+event.selected + 1);
    setcurrentPage(+event.selected + 1);
    console.log(`User requested page number ${event.selected}`);
  };

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
            <th scope="col">Role</th>
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
                <td>{item.role.name}</td>
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
      <div className="d-flex justify-content-center">
        <ReactPaginate
          nextLabel="next >"
          onPageChange={handlePageClick}
          pageRangeDisplayed={3}
          marginPagesDisplayed={2}
          pageCount={pageCount}
          previousLabel="< previous"
          pageClassName="page-item"
          pageLinkClassName="page-link"
          previousClassName="page-item"
          previousLinkClassName="page-link"
          nextClassName="page-item"
          nextLinkClassName="page-link"
          breakLabel="..."
          breakClassName="page-item"
          breakLinkClassName="page-link"
          containerClassName="pagination"
          activeClassName="active"
          renderOnZeroPageCount={null}
          forcePage={currentPage - 1}
        />
      </div>
    </>
  );
};

export default TableUserPaginate;
