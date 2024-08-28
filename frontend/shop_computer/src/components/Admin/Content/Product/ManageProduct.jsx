import ModalFormProduct from "../Product/ModalFormProduct";
import { FcPlus } from "react-icons/fc";
import "./ManageProduct.scss";
import TableProduct from "./TableProduct";
import { useEffect, useState } from "react";
import {
  getAllProducts,
  deleteProduct,
  getAllProductsWithPaginate,
} from "../../../../services/ProductService";
import ModalConfirmProduct from "./ModalConfirmProduct";
import { toast } from "react-toastify";
import TableProductPaginate from "./TableProductPaginate";

const ManageProduct = () => {
  const LIMIT_PRODUCT = 5;
  const [pageCount, setPageCount] = useState(0);
  const [currentPage, setcurrentPage] = useState(1);

  const [showModal, setShowModal] = useState(false);
  const [listProducts, setListProducts] = useState([]);
  const [id, setId] = useState(0);
  const [productID, setProductID] = useState(0);
  const [viewId, setViewId] = useState(false);
  const [modalDeteleProduct, setModalDeleteProduct] = useState(false);
  const [reloadProducts, setReloadProducts] = useState(false);

  useEffect(() => {
    //fetchListProduct();
    fetchListProductPaginate(1);
  }, [reloadProducts]);

  const fetchListProduct = async () => {
    let res = await getAllProducts();
    setListProducts(res.data.data.items);
    console.log(res);
  };

  const fetchListProductPaginate = async (page) => {
    let res = await getAllProductsWithPaginate(page, LIMIT_PRODUCT);
    if (res.data.status === 200) {
      setListProducts(res.data.data.items);
      setPageCount(res.data.data.totalPage);
      console.log(res);
    }
  };

  const handleUpdateProduct = (productId) => {
    setShowModal(true);
    setId(productId);
  };

  const handleDeleteProduct = (productId) => {
    console.log("check handleDeleteProduct");
    setProductID(productId);
    setModalDeleteProduct(true);
  };

  const handleModalDeleteProduct = async () => {
    let res = await deleteProduct(productID);
    if (res) {
      toast.success(res.data.message);
      setcurrentPage(1);
    }
    setReloadProducts(!reloadProducts);
  };

  const handleViewProduct = (productId) => {
    console.log("check handleViewProduct");
    setShowModal(true);
    setId(productId);
    setViewId(true);
  };

  return (
    <div className="manage-product-container">
      <div className="title">Manage product</div>
      <div className="products-content">
        <div>
          <button
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            <FcPlus /> Add new products
          </button>
          {showModal && (
            <ModalFormProduct
              show={true} // Show modal when id !== 0
              setShow={setShowModal}
              fetchListProduct={fetchListProduct}
              id={id}
              setId={setId}
              viewId={viewId}
              setViewId={setViewId}
              fetchListProductPaginate={fetchListProductPaginate}
              currentPage={currentPage}
            />
          )}
        </div>
        <div className="table-container">
          {/* <TableProduct
            listProducts={listProducts}
            handleUpdateProduct={handleUpdateProduct}
            handleDeleteProduct={handleDeleteProduct}
            handleViewProduct={handleViewProduct}
          /> */}
          <TableProductPaginate
            listProducts={listProducts}
            handleUpdateProduct={handleUpdateProduct}
            handleDeleteProduct={handleDeleteProduct}
            handleViewProduct={handleViewProduct}
            fetchListProductPaginate={fetchListProductPaginate}
            pageCount={pageCount}
            currentPage={currentPage}
            setcurrentPage={setcurrentPage}
          />
        </div>
        {modalDeteleProduct && (
          <ModalConfirmProduct
            show={true}
            setShow={setModalDeleteProduct}
            handleModalDeleteProduct={handleModalDeleteProduct}
          />
        )}
      </div>
    </div>
  );
};

export default ManageProduct;
