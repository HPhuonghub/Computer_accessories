import axios from "../utils/axiosCustomze";

const postCreateNewProduct = async (formDT) => {
  let data = {
    name: formDT.name,
    price: formDT.price,
    stock: formDT.stock,
    thumbnail: formDT.thumbnail,
    discount: formDT.discount,
    category: {
      name: formDT.category.name,
    },
    supplier: {
      name: formDT.supplier.name,
    },
  };

  return await axios.post("api/v1/product/", data);
};

const getAllProducts = async () => {
  return await axios.get("api/v1/product/list");
};

const getProductId = async (productId) => {
  return await axios.get(`api/v1/product/${productId}`);
};

const putUpdateProducts = async (id, formDT) => {
  let data = {
    name: formDT.name,
    price: formDT.price,
    stock: formDT.stock,
    thumbnail: formDT.thumbnail,
    discount: formDT.discount,
    category: {
      name: formDT.category.name,
    },
    supplier: {
      name: formDT.supplier.name,
    },
  };
  console.log("check data", formDT);
  return axios.put(`api/v1/product/${id}`, data);
};

const deleteProduct = async (productId) => {
  return axios.delete(`api/v1/product/${productId}`);
};

const getAllProductsWithPaginate = async (pageNo, pageSize) => {
  return axios.get(`api/v1/product/list?pageNo=${pageNo}&pageSize=${pageSize}`);
};

const getCategories = async () => {
  return await axios.get("api/v1/category/list");
};

const getSuppliers = async () => {
  return await axios.get("api/v1/supplier/list");
};

export {
  postCreateNewProduct,
  getAllProducts,
  putUpdateProducts,
  getProductId,
  deleteProduct,
  getAllProductsWithPaginate,
  getCategories,
  getSuppliers,
};
