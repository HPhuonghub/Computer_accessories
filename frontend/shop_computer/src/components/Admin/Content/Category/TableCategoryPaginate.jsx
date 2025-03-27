import React from "react";
import ReactPaginate from "react-paginate";

const TableCategoryPaginate = (props) => {
  const {
    listCategorys,
    handleUpdateCategory,
    handleDeleteCategory,
    handleViewCategory,
    pageCount,
    currentPage,
    setcurrentPage,
  } = props;

  const handlePageClick = (event) => {
    props.fetchListCategoryPaginate(+event.selected + 1);
    setcurrentPage(+event.selected + 1);
    console.log(`Category requested page number ${event.selected}`);
  };

  return (
    <div className="table-responsive">
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
          {listCategorys.length > 0 &&
            listCategorys?.map((item, index) => (
              <tr key={index}>
                <td>{index + 1}</td>
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
                    onClick={() => handleUpdateCategory(item.id)}
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
      <div className="d-flex justify-content-center">
        <ReactPaginate
          nextLabel="next >"
          onPageChange={handlePageClick}
          pageRangeDisplayed={1}
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
    </div>
  );
};

export default TableCategoryPaginate;
