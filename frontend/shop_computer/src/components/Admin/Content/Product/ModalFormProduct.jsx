import React, { useState, useEffect } from "react";
import { Modal, Button } from "react-bootstrap";
import { toast } from "react-toastify";
import { FaAsterisk } from "react-icons/fa";
import {
  postCreateNewProduct,
  putUpdateProducts,
  getProductId,
  getCategories,
  getSuppliers,
} from "../../../../services/ProductService";

const ModalFormProduct = (props) => {
  const {
    show,
    setShow,
    id,
    setId,
    viewId,
    setViewId,
    fetchListProductPaginate,
    currentPage,
  } = props;

  const [categories, setCategories] = useState([]);
  const [suppliers, setSuppliers] = useState([]);

  const [formData, setFormData] = useState({
    name: "",
    thumbnail: "",
    stock: 0,
    price: 0,
    discount: 0,
    category: {
      name: "", // Default category name
    },
    supplier: {
      name: "", // Default supplier name
    },
  });

  const transformFormDataToData = (formData) => {
    let data = {
      name: formData.name,
      price: formData.price,
      stock: formData.stock,
      thumbnail: formData.thumbnail,
      discount: formData.discount,
      category: {
        name: formData.category.name,
      },
      supplier: {
        name: formData.supplier.name,
      },
    };

    return data;
  };

  const handleClose = () => {
    setShow(false);
    setViewId(false);
    resetForm();
  };

  const resetForm = () => {
    setFormData({
      name: "",
      thumbnail: "",
      stock: 0,
      price: 0,
      discount: 0,
      category: {
        name: categories.length > 0 ? categories[0].name : "", // Reset to first category name if available
      },
      supplier: {
        name: suppliers.length > 0 ? suppliers[0].name : "", // Reset to first supplier name if available
      },
    });
    setId(0);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    // Validate numeric fields
    if (name === "stock" || name === "price" || name === "discount") {
      if (isNaN(value) || value < 0) {
        toast.error("Please enter a valid positive number");
        return;
      }
    }

    // Cập nhật giá trị cho category và supplier
    if (name === "category" || name === "supplier") {
      setFormData((prevData) => ({
        ...prevData,
        [name]: {
          name: value,
        },
      }));
    } else {
      setFormData((prevData) => ({
        ...prevData,
        [name]:
          name === "stock" || name === "price" || name === "discount"
            ? parseFloat(value)
            : value,
      }));
    }
  };

  const handleCreateProduct = async () => {
    if (!validateForm()) return;

    try {
      const formDT = transformFormDataToData(formData);
      const response = await postCreateNewProduct(formDT);
      handleApiResponse(response);
    } catch (error) {
      toast.error("Failed to create product");
      console.error("Error creating product:", error);
    }
  };

  const handleUpdateProduct = async () => {
    if (!validateForm()) return;

    try {
      const formDT = transformFormDataToData(formData);
      const response = await putUpdateProducts(id, formDT);
      handleApiResponse(response);
    } catch (error) {
      toast.error("Failed to update product");
      console.error("Error updating product:", error);
    }
  };

  const handleApiResponse = (response) => {
    if (response && response.data && response.data.status === 200) {
      toast.success(response.data.message);
      handleClose();
      fetchListProductPaginate(currentPage);
    } else if (response && response.data && response.data.status !== 200) {
      toast.error(response.data.message);
      handleClose();
    }
  };

  const validateForm = () => {
    const { name, thumbnail, stock, price, discount, category, supplier } =
      formData;

    // Kiểm tra các trường bắt buộc
    if (!name || !thumbnail) {
      toast.error("Please fill in all required fields correctly");
      return false;
    }

    // Kiểm tra category và supplier phải được chọn
    if (!category.name) {
      toast.error("Please select a category");
      return false;
    }

    if (!supplier.name) {
      toast.error("Please select a supplier");
      return false;
    }

    // Kiểm tra giá trị số
    if (isNaN(stock) || stock < 0) {
      toast.error("Stock must be a valid positive number");
      return false;
    }

    if (isNaN(price) || price < 0) {
      toast.error("Price must be a valid positive number");
      return false;
    }

    if (isNaN(discount) || discount < 0) {
      toast.error("Discount must be a valid positive number");
      return false;
    }

    return true;
  };

  useEffect(() => {
    // Fetch categories and suppliers on component mount
    getCategoriesData();
    getSuppliersData();
    // Fetch product details if id is provided
    if (id !== 0) {
      fetchProductId();
    }
  }, [id]);

  const fetchProductId = async () => {
    try {
      const res = await getProductId(id);
      const { name, thumbnail, stock, price, discount, category, supplier } =
        res.data.data;
      setFormData({
        name,
        thumbnail,
        stock,
        price,
        discount,
        category: {
          name: category ? category.name : "",
        },
        supplier: {
          name: supplier ? supplier.name : "",
        },
      });
    } catch (error) {
      console.error("Error fetching product:", error);
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

  const getSuppliersData = async () => {
    try {
      const res = await getSuppliers();
      setSuppliers(res.data.data); // Assuming `res.data.data` is an array of suppliers
    } catch (error) {
      console.error("Error fetching suppliers:", error);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>
          {id === 0 ? (
            <Modal.Title>Add new a product</Modal.Title>
          ) : !viewId ? (
            <Modal.Title>Update product</Modal.Title>
          ) : (
            <Modal.Title>View a product</Modal.Title>
          )}
        </Modal.Title>
      </Modal.Header>

      <form className="row g-3" style={{ margin: "auto", paddingBottom: 10 }}>
        <div className="col-md-6">
          <label htmlFor="inputName4" className="form-label">
            <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
            Name
          </label>
          <input
            type="text"
            className="form-control"
            id="inputName4"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            readOnly={!viewId ? false : true}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="inputThumbnail" className="form-label">
            <FaAsterisk size={6} color="red" style={{ margin: 2 }} />
            Thumbnail
          </label>
          <input
            type="text"
            className="form-control"
            id="inputThumbnail"
            name="thumbnail"
            value={formData.thumbnail}
            onChange={handleInputChange}
            readOnly={!viewId ? false : true}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="inputStock" className="form-label">
            Stock
          </label>
          <input
            type="number"
            className="form-control"
            id="inputStock"
            name="stock"
            value={formData.stock}
            onChange={handleInputChange}
            readOnly={!viewId ? false : true}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="inputPrice" className="form-label">
            Price
          </label>
          <input
            type="number"
            className="form-control"
            id="inputPrice"
            name="price"
            value={formData.price}
            onChange={handleInputChange}
            readOnly={!viewId ? false : true}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="inputDiscount" className="form-label">
            Discount
          </label>
          <input
            type="number"
            className="form-control"
            id="inputDiscount"
            name="discount"
            value={formData.discount}
            onChange={handleInputChange}
            readOnly={!viewId ? false : true}
          />
        </div>

        <div className="col-md-4">
          <label htmlFor="inputCategory" className="form-label">
            Category <span className="text-danger">*</span>
          </label>
          <select
            id="inputCategory"
            className="form-select"
            name="category"
            value={formData.category.name}
            onChange={handleInputChange}
            disabled={viewId}
          >
            <option value="">Select category...</option>
            {categories.map((cat) => (
              <option key={cat._id} value={cat.name}>
                {cat.name}
              </option>
            ))}
          </select>
        </div>

        <div className="col-md-4">
          <label htmlFor="inputSupplier" className="form-label">
            Supplier <span className="text-danger">*</span>
          </label>
          <select
            id="inputSupplier"
            className="form-select"
            name="supplier"
            value={formData.supplier.name}
            onChange={handleInputChange}
            disabled={viewId}
          >
            <option value="">Select supplier...</option>
            {suppliers.map((sup) => (
              <option key={sup._id} value={sup.name}>
                {sup.name}
              </option>
            ))}
          </select>
        </div>
      </form>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        {id === 0 ? (
          <Button variant="primary" onClick={handleCreateProduct}>
            Save
          </Button>
        ) : !viewId ? (
          <Button variant="primary" onClick={handleUpdateProduct}>
            Update
          </Button>
        ) : (
          <></>
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default ModalFormProduct;
