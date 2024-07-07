import axios from "../utils/axiosCustomze";

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

  return await axios.post("user/", data);
};

const getAllUsers = async () => {
  return await axios.get("user/list");
};

const getUserId = async (userId) => {
  return await axios.get(`user/${userId}`);
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
  return axios.put(`user/${id}`, data);
};

const deleteUser = async (userId) => {
  return axios.delete(`user/${userId}`);
};

const getAllUsersWithPaginate = async (pageNo, pageSize) => {
  return axios.get(`user/list?pageNo=${pageNo}&pageSize=${pageSize}`);
};

const postLogin = async (username, password) => {
  return axios.post(`auth/login`, { username, password });
};

const postRegister = async (fullname, username, password) => {
  return axios.post(`auth/register`, {
    fullname,
    username,
    password,
    role: { name: "USER" },
  });
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
};
