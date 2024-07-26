import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
  product: null,
  isLoading: false,
  error: null,
};

export const ProductSlice = createSlice({
  name: "product",
  initialState,
  reducers: {
    getAllProductStart: (state) => {
      state.isLoading = true;
      state.error = null;
    },
    getAllProductSuccess: (state, action) => {
      state.isLoading = false;
      state.product = action.payload;
    },
    getAllProductFailure: (state, action) => {
      state.isLoading = false;
      state.error = action.payload;
    },
  },
});

export const {
  getAllProductStart,
  getAllProductSuccess,
  getAllProductFailure,
} = ProductSlice.actions;

export const getAllProduct = () => async (dispatch) => {
  dispatch(getAllProductStart());
  try {
    const res = await axios.get("http://localhost:8888/api/v1/product/list");
    dispatch(getAllProductSuccess(res.data));
  } catch (error) {
    dispatch(getAllProductFailure(error.message));
  }
};

export const listProduct = (state) => state.product.product;

export default ProductSlice.reducer;
