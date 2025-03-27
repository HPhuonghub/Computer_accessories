import axios from "../utils/axiosCustomze";
import { ACCESS_TOKEN } from "../constants/index";

const postCreateNewCategory = async (formDT) => {
  let data = {
    name: formDT.name,
    description: formDT.description,
  };
  const { config } = await markToken();
  return await axios.post("api/v1/category", data, config);
};

const getAllCategorys = async () => {
  const { config } = await markToken();
  return await axios.get("api/v1/category/list?sortBy=name:asc", config);
};

const getCategoryId = async (categoryId) => {
  const { config } = await markToken();
  return await axios.get(`api/v1/category/${categoryId}`, config);
};

const putUpdateCategorys = async (id, formDT) => {
  let data = {
    name: formDT.name,
    description: formDT.description,
  };
  console.log("check data", formDT);
  const { config } = await markToken();
  return axios.put(`api/v1/category/${id}`, data, config);
};

const deleteCategory = async (categoryId) => {
  const { config } = await markToken();
  return axios.delete(`api/v1/category/${categoryId}`, config);
};

const getAllCategorysWithPaginate = async (pageNo, pageSize) => {
  const { config } = await markToken();
  return axios.get(
    `api/v1/category/lists?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=name:asc`,
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
  postCreateNewCategory,
  getAllCategorys,
  putUpdateCategorys,
  getCategoryId,
  deleteCategory,
  getAllCategorysWithPaginate,
  getCategories,
  getSuppliers,
};
