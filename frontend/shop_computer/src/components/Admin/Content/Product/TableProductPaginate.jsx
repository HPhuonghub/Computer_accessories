import React, { useEffect, useState } from "react";
import ReactPaginate from "react-paginate";

const TableProductPaginate = (props) => {
  const {
    listProducts,
    handleUpdateProduct,
    handleDeleteProduct,
    handleViewProduct,
    pageCount,
    currentPage,
    setcurrentPage,
  } = props;

  const handlePageClick = (event) => {
    props.fetchListProductPaginate(+event.selected + 1);
    setcurrentPage(+event.selected + 1);
    console.log(`Product requested page number ${event.selected}`);
  };

  return (
    <div className="table-responsive">
      <div>Table products</div>
      <table className="table table-hover table-bordered">
        <thead>
          <tr>
            <th scope="col">STT</th>
            <th scope="col">Name</th>
            <th scope="col">Thumbnail</th>
            <th scope="col">Stock</th>
            <th scope="col">Price</th>
            <th scope="col">Discount</th>
            <th scope="col">Category</th>
            <th scope="col">Supplier</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {listProducts &&
            listProducts.map((item, index) => (
              <tr key={index}>
                <td>{index + 1}</td>
                <td>{item.name}</td>
                <td>
                  <img
                    src={item.thumbnail} // Assuming item.thumbnail is the URL to the image
                    alt={item.name} // Provide an appropriate alt text for accessibility
                    style={{ maxWidth: "100px" }} // Adjust styling as needed
                  />
                </td>
                <td>{item.stock}</td>
                <td>{item.price}</td>
                <td>{item.discount}</td>
                <td>{item.category.name}</td>
                <td>{item.supplier.name}</td>
                <td>
                  <button
                    className="btn btn-secondary"
                    onClick={() => handleViewProduct(item.id)}
                  >
                    View
                  </button>
                  <button
                    className="btn btn-warning mx-3"
                    onClick={() => handleUpdateProduct(item.id)}
                  >
                    Update
                  </button>
                  <button
                    className="btn btn-danger"
                    onClick={() => handleDeleteProduct(item.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          {listProducts && listProducts.length === 0 && (
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
    </div>
  );
};

export default TableProductPaginate;
