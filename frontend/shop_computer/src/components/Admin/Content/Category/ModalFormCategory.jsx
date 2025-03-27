import React, { useState, useEffect } from "react";
import { Modal, Button } from "react-bootstrap";
import { toast } from "react-toastify";
import { FaAsterisk } from "react-icons/fa";
import {
  postCreateNewCategory,
  putUpdateCategorys,
  getCategoryId,
  getCategories,
} from "../../../../services/CategoryService";

const ModalFormCategory = (props) => {
  const {
    show,
    setShow,
    id,
    setId,
    viewId,
    setViewId,
    fetchListCategoryPaginate,
    currentPage,
  } = props;

  const [categories, setCategories] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
  });

  const transformFormDataToData = (formData) => {
    return {
      name: formData.name,
      description: formData.description,
    };
  };

  const handleClose = () => {
    setShow(false);
    setViewId(false);
    resetForm();
  };

  const resetForm = () => {
    setFormData({
      name: "",
      description: "",
    });
    setId(0);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    // Update formData based on input name
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleCreateCategory = async () => {
    if (!validateForm()) return;

    try {
      const formDT = transformFormDataToData(formData);
      const response = await postCreateNewCategory(formDT);
      handleApiResponse(response);
    } catch (error) {
      toast.error("Failed to create category");
      console.error("Error creating category:", error);
    }
  };

  const handleUpdateCategory = async () => {
    if (!validateForm()) return;

    try {
      const formDT = transformFormDataToData(formData);
      const response = await putUpdateCategorys(id, formDT);
      handleApiResponse(response);
    } catch (error) {
      toast.error("Failed to update category");
      console.error("Error updating category:", error);
    }
  };

  const handleApiResponse = (response) => {
    if (response && response.data && response.data.status === 200) {
      toast.success(response.data.message);
      handleClose();
      fetchListCategoryPaginate(currentPage);
    } else if (response && response.data && response.data.status !== 200) {
      toast.error(response.data.message);
      handleClose();
    }
  };

  const validateForm = () => {
    const { name, description } = formData;

    // Validate required fields
    if (!name || !description) {
      toast.error("Please fill in all required fields correctly");
      return false;
    }

    return true;
  };

  useEffect(() => {
    // Fetch categories on component mount
    getCategoriesData();
    // Fetch category details if id is provided
    if (id !== 0) {
      fetchCategoryId();
    }
  }, [id]);

  const fetchCategoryId = async () => {
    try {
      const res = await getCategoryId(id);
      const { name, description } = res.data.data;
      setFormData({
        name,
        description,
      });
    } catch (error) {
      console.error("Error fetching category:", error);
    }
  };

  const getCategoriesData = async () => {
    try {
      const res = await getCategories();
      setCategories(res.data.data); // Assuming `res.data.data` is an array of categories
    } catch (error) {
      console.error("Error fetching categories:", error);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>
          {id === 0 ? (
            <Modal.Title>Add New Category</Modal.Title>
          ) : !viewId ? (
            <Modal.Title>Update Category</Modal.Title>
          ) : (
            <Modal.Title>View Category</Modal.Title>
          )}
        </Modal.Title>
      </Modal.Header>

      <form className="row g-3" style={{ margin: "auto", paddingBottom: 10 }}>
        <div className="col-md-6">
          <label htmlFor="inputName" className="form-label">
            <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
            Name
          </label>
          <input
            type="text"
            className="form-control"
            id="inputName"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            readOnly={viewId}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="inputDescription" className="form-label">
            <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
            Description
          </label>
          <input
            type="text"
            className="form-control"
            id="inputDescription"
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            readOnly={viewId}
          />
        </div>
      </form>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        {id === 0 ? (
          <Button variant="primary" onClick={handleCreateCategory}>
            Save
          </Button>
        ) : !viewId ? (
          <Button variant="primary" onClick={handleUpdateCategory}>
            Update
          </Button>
        ) : (
          <></>
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default ModalFormCategory;
