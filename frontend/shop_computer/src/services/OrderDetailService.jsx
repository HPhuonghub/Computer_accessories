import axios from "../utils/axiosCustomze";
import { ACCESS_TOKEN } from "../constants/index";

const postCreateNewOrderDetail = async (formDT) => {
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

const getAllOrderDetails = async () => {
  const { config } = await markToken();
  return await axios.get("api/v1/product/list?sortBy=name:asc", config);
};

const getOrderDetailId = async (productId) => {
  const { config } = await markToken();
  return await axios.get(`api/v1/product/${productId}`, config);
};

const putUpdateOrderDetails = async (id, formDT) => {
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

const deleteOrderDetail = async (productId) => {
  const { config } = await markToken();
  return axios.delete(`api/v1/product/${productId}`, config);
};

const getAllOrderDetailsWithPaginate = async (pageNo, pageSize) => {
  const { config } = await markToken();
  return axios.get(
    `api/v1/product/list?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=name:asc`,
    config
  );
};

const getOrderDetailByUserId = async (userId) => {
  const { config } = await markToken();
  return axios.get(`api/v1/order-details/list/${userId}`, config);
};

const deleteOrderDetailByUserId = async (productId) => {
  const { config } = await markToken();
  return axios.delete(`api/v1/order-details/${productId}`, config);
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
  postCreateNewOrderDetail,
  getAllOrderDetails,
  putUpdateOrderDetails,
  getOrderDetailId,
  deleteOrderDetail,
  getAllOrderDetailsWithPaginate,
  getOrderDetailByUserId,
  deleteOrderDetailByUserId,
};
