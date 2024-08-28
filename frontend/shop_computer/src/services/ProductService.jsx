import axios from "../utils/axiosCustomze";
import { ACCESS_TOKEN } from "../constants/index";

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
  const { config } = await markToken();
  return await axios.post("api/v1/product/", data, config);
};

const getAllProducts = async () => {
  const { config } = await markToken();
  return await axios.get("api/v1/product/list?sortBy=name:asc", config);
};

const getProductId = async (productId) => {
  const { config } = await markToken();
  return await axios.get(`api/v1/product/${productId}`, config);
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
  const { config } = await markToken();
  return axios.put(`api/v1/product/${id}`, data, config);
};

const deleteProduct = async (productId) => {
  const { config } = await markToken();
  return axios.delete(`api/v1/product/${productId}`, config);
};

const getAllProductsWithPaginate = async (pageNo, pageSize) => {
  const { config } = await markToken();
  return axios.get(
    `api/v1/product/list?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=name:asc`,
    config
  );
};

const getCategories = async () => {
  const { config } = await markToken();
  return await axios.get("api/v1/category/list", config);
};

const getSuppliers = async () => {
  const { config } = await markToken();
  return await axios.get("api/v1/supplier/list", config);
};

const markToken = () => {
  const token = localStorage.getItem(ACCESS_TOKEN); // Retrieve token from localStorage

  const config = {
    headers: {
      Authorization: `Bearer ${token}`, // Add the token to the Authorization header
    },
  };

  return { config }; // Return the config object
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
