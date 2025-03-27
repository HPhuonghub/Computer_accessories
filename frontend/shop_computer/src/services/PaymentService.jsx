import axios from "../utils/axiosCustomze";
import { ACCESS_TOKEN } from "../constants/index";

const postPaymentCOD = async (orders) => {
  const { config } = await markToken();
  return await axios.post("api/v1/payment/payment-cod", orders, config);
};

const postPaymentPayOS = async (orders) => {
  const { config } = await markToken();
  return await axios.post("api/v1/payment/create-payment-link", orders, config);
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

export { postPaymentCOD, postPaymentPayOS };
