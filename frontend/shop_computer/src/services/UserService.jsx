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

  return await axios.post("api/v1/user/", data);
};

const getAllUsers = async () => {
  return await axios.get("api/v1/user/list");
};

const getUserId = async (userId) => {
  return await axios.get(`api/v1/user/${userId}`);
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
  return axios.put(`api/v1/user/${id}`, data);
};

const deleteUser = async (userId) => {
  return axios.delete(`api/v1/user/${userId}`);
};

const getAllUsersWithPaginate = async (pageNo, pageSize) => {
  return axios.get(`api/v1/user/list?pageNo=${pageNo}&pageSize=${pageSize}`);
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
