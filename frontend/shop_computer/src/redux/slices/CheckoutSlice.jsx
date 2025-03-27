import { createSlice } from "@reduxjs/toolkit";
import { BASE_URL, ACCESS_TOKEN } from "../../constants/index";
import axios from "axios";

const initialState = {
  checkout: null,
};

export const CheckoutSlice = createSlice({
  name: "checkout",
  initialState,
  reducers: {
    getCheckoutStart: (state) => {
      state.isLoading = true;
      state.error = null;
    },
    getCheckoutSuccess: (state, action) => {
      state.isLoading = false;
    },
    getCheckoutFailure: (state, action) => {
      state.isLoading = false;
      state.error = action.payload;
    },
  },
});

export const { getCheckoutStart, getCheckoutSuccess, getCheckoutFailure } =
  CheckoutSlice.actions;

export const postCheckout = (orders, listOrderDetails) => async (dispatch) => {
  const data = {
    ordersDTO: orders,
    orderDetailsDTOS: listOrderDetails,
  };
  const { config } = await markToken();
  dispatch(getCheckoutStart());
  try {
    const res = await axios.post(
      `${BASE_URL}api/v1/order-details/`,
      data,
      config
    );
    dispatch(getCheckoutSuccess(res.data));
    console.log("check data", data);
  } catch (error) {
    dispatch(getCheckoutFailure(error.message));
  }
};

const markToken = () => {
  const token = localStorage.getItem(ACCESS_TOKEN); // Retrieve token from localStorage

  console.log("check ACCESS_TOKEN", token);

  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json", // Thêm header Content-Type nếu cần
    },
  };

  return { config }; // Return the config object
};

export const list = (state) => state;

export default CheckoutSlice.reducer;
