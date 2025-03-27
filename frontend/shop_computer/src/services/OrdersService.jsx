import axios from "../utils/axiosCustomze";
import { ACCESS_TOKEN } from "../constants/index";

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

const getOrderByUserEmail = async (email) => {
  const { config } = await markToken();
  return axios.get(`api/v1/orders/lists?email=${email}`, config);
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
  getAllOrderDetailsWithPaginate,
  getOrderDetailByUserId,
  deleteOrderDetailByUserId,
  getOrderByUserEmail,
};
