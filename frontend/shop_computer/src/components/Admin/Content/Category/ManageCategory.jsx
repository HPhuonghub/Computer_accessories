import ModalFormCategory from "../Category/ModalFormCategory";
import { FcPlus } from "react-icons/fc";
import "./ManageCategory.scss";
import TableCategory from "./TableCategory";
import { useEffect, useState } from "react";
import {
  getAllCategorys,
  deleteCategory,
  getAllCategorysWithPaginate,
} from "../../../../services/CategoryService";
import ModalConfirmCategory from "./ModalConfirmCategory";
import { toast } from "react-toastify";
import TableCategoryPaginate from "./TableCategoryPaginate";

const ManageCategory = () => {
  const LIMIT_PRODUCT = 5;
  const [pageCount, setPageCount] = useState(0);
  const [currentPage, setcurrentPage] = useState(1);

  const [showModal, setShowModal] = useState(false);
  const [listCategorys, setListCategorys] = useState([]);
  const [id, setId] = useState(0);
  const [categoryID, setCategoryID] = useState(0);
  const [viewId, setViewId] = useState(false);
  const [modalDeteleCategory, setModalDeleteCategory] = useState(false);
  const [reloadCategorys, setReloadCategorys] = useState(false);

  useEffect(() => {
    //fetchListCategory();
    fetchListCategoryPaginate(1);
  }, [reloadCategorys]);

  const fetchListCategory = async () => {
    let res = await getAllCategorys();
    setListCategorys(res.data.data.items);
    console.log("check res.data.data:", res.data.data);
  };

  const fetchListCategoryPaginate = async (page) => {
    let res = await getAllCategorysWithPaginate(page, LIMIT_PRODUCT);
    if (res.data.status === 200) {
      setListCategorys(res.data.data.items);
      setPageCount(res.data.data.totalPage);
      console.log("check res.data.data:", res.data.data);
    }
  };

  const handleUpdateCategory = (categoryId) => {
    setShowModal(true);
    setId(categoryId);
  };

  const handleDeleteCategory = (categoryId) => {
    console.log("check handleDeleteCategory");
    setCategoryID(categoryId);
    setModalDeleteCategory(true);
  };

  const handleModalDeleteCategory = async () => {
    let res = await deleteCategory(categoryID);
    if (res) {
      toast.success(res.data.message);
      setcurrentPage(1);
    }
    setReloadCategorys(!reloadCategorys);
  };

  const handleViewCategory = (categoryId) => {
    console.log("check handleViewCategory");
    setShowModal(true);
    setId(categoryId);
    setViewId(true);
  };

  return (
    <div className="manage-category-container">
      <div className="title">Manage category</div>
      <div className="categorys-content">
        <div>
          <button
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            <FcPlus /> Add new categorys
          </button>
          {showModal && (
            <ModalFormCategory
              show={true} // Show modal when id !== 0
              setShow={setShowModal}
              fetchListCategory={fetchListCategory}
              id={id}
              setId={setId}
              viewId={viewId}
              setViewId={setViewId}
              fetchListCategoryPaginate={fetchListCategoryPaginate}
              currentPage={currentPage}
            />
          )}
        </div>
        <div className="table-container">
          {/* <TableCategory
            listCategorys={listCategorys}
            handleUpdateCategory={handleUpdateCategory}
            handleDeleteCategory={handleDeleteCategory}
            handleViewCategory={handleViewCategory}
          /> */}
          <TableCategoryPaginate
            listCategorys={listCategorys}
            handleUpdateCategory={handleUpdateCategory}
            handleDeleteCategory={handleDeleteCategory}
            handleViewCategory={handleViewCategory}
            fetchListCategoryPaginate={fetchListCategoryPaginate}
            pageCount={pageCount}
            currentPage={currentPage}
            setcurrentPage={setcurrentPage}
          />
        </div>
        {modalDeteleCategory && (
          <ModalConfirmCategory
            show={true}
            setShow={setModalDeleteCategory}
            handleModalDeleteCategory={handleModalDeleteCategory}
          />
        )}
      </div>
    </div>
  );
};

export default ManageCategory;
