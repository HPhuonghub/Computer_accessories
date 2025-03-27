import React from "react";

const TableCategory = (props) => {
  const {
    listCategorys,
    handleUpdateCategory,
    handleDeleteCategory,
    handleViewCategory,
  } = props;

  return (
    <>
      <div>Table categories</div>
      <table className="table table-hover table-bordered">
        <thead>
          <tr>
            <th scope="col">STT</th>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {listCategorys &&
            listCategorys.map((item, index) => (
              <tr key={index}>
                <td>{item.id}</td>
                <td>{item.name}</td>
                <td>{item.description}</td>
                <td>
                  <button
                    className="btn btn-secondary"
                    onClick={() => handleViewCategory(item.id)}
                  >
                    View
                  </button>
                  <button
                    className="btn btn-warning mx-3"
                    onClick={() => handleUpdateCategory(item.id)} // Pass item.id to handleUpdateCategory
                  >
                    Update
                  </button>
                  <button
                    className="btn btn-danger"
                    onClick={() => handleDeleteCategory(item.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          {listCategorys && listCategorys.length === 0 && (
            <tr>
              <td colSpan={4}>Not found data</td>
            </tr>
          )}
        </tbody>
      </table>
    </>
  );
};

export default TableCategory;
