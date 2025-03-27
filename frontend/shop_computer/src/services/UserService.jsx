import axios from "../utils/axiosCustomze";
import { ACCESS_TOKEN } from "../constants/index";

const postCreateNewUser = async (
  email,
  fullName,
  password,
  address,
  phone,
  role,
  state
) => {
  let data = {
    email: email,
    fullName: fullName,
    password: password,
    address: address,
    phone: phone,
    role: {
      name: role,
    },
    state: state.toLowerCase(),
  };
  const { config } = await markToken();
  return await axios.post("api/v1/user/", data, config);
};

const getAllUsers = async () => {
  const { config } = await markToken();
  return await axios.get("api/v1/user/list", config);
};

const getUserId = async (userId) => {
  const { config } = await markToken();
  return await axios.get(`api/v1/user/${userId}`, config);
};

const putUpdateUsers = async (
  id,
  email,
  fullName,
  address,
  phone,
  role,
  state
) => {
  let data = {
    email: email,
    fullName: fullName,
    phone: phone,
    address: address,
    role: {
      name: role,
    },
    state: state.toLowerCase(),
  };
  const { config } = await markToken();
  return axios.put(`api/v1/user/${id}`, data, config);
};

const deleteUser = async (userId) => {
  const { config } = await markToken();
  return axios.delete(`api/v1/user/${userId}`, config);
};

const getAllUsersWithPaginate = async (pageNo, pageSize) => {
  const { config } = await markToken();

  return axios.get(
    `api/v1/user/list?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=fullName:asc`,
    config
  );
};

const postLogin = async (username, password) => {
  return axios.post(`api/v1/auth/login`, { username, password });
};

const postRegister = async (fullname, username, password) => {
  return axios.post(`api/v1/auth/register`, {
    fullname,
    username,
    password,
    role: { name: "USER" },
  });
};

const postForgotPassword = async (username) => {
  return axios.post(`api/v1/password/forgot`, { email: username });
};

const postChangePassword = async (email, oldPassword, newPassword) => {
  return axios.post(`api/v1/password/change`, {
    email,
    oldPassword,
    newPassword,
  });
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

const logOut = async () => {
  const { config } = markToken(); // Gọi hàm markToken không cần await
  try {
    const response = await axios.post(
      "/api/v1/auth/logout",
      {},
      { ...config, withCredentials: true } // Kết hợp config với withCredentials
    );
    return response; // Trả về phản hồi để sử dụng nếu cần
  } catch (error) {
    console.error("Có lỗi xảy ra khi đăng xuất:", error);
  }
};

export {
  postCreateNewUser,
  getAllUsers,
  putUpdateUsers,
  getUserId,
  deleteUser,
  getAllUsersWithPaginate,
  postLogin,
  postRegister,
  postForgotPassword,
  postChangePassword,
  logOut,
};
