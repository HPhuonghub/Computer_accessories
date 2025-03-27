import { createSlice } from "@reduxjs/toolkit";
import axios from "../../utils/axiosCustomze";

const initialState = {
  product: null,
  productId: null,
  isLoading: false,
  error: null,
  productSearch: null,
  search: "",
  promotionalProduct: null,
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
    getSearchSuccess: (state, action) => {
      state.isLoading = false;
      state.search = action.payload;
    },
    getAllProductSearchSuccess: (state, action) => {
      state.isLoading = false;
      state.productSearch = action.payload;
    },
    getAllPromotionalProductSuccess: (state, action) => {
      state.isLoading = false;
      state.promotionalProduct = action.payload;
    },
    getProductIdSuccess: (state, action) => {
      state.isLoading = false;
      state.productId = action.payload;
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
  getSearchSuccess,
  getAllProductSearchSuccess,
  getProductIdSuccess,
  getAllProductFailure,
  getAllPromotionalProductSuccess,
} = ProductSlice.actions;

export const getAllProduct = (pageNo, pageSize) => async (dispatch) => {
  dispatch(getAllProductStart());
  try {
    const res = await axios.get(
      `api/v1/product/list?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=name:asc`
    );
    dispatch(getAllProductSuccess(res.data));
  } catch (error) {
    dispatch(getAllProductFailure(error.message));
  }
};

export const getAllProductSearch =
  (pageNo, pageSize, search) => async (dispatch) => {
    dispatch(getAllProductStart());
    try {
      const res = await axios.get(
        `api/v1/product/list-search?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=name:asc&search=${search}`
      );
      dispatch(getAllProductSearchSuccess(res.data));
    } catch (error) {
      dispatch(getAllProductFailure(error.message));
    }
  };

export const getAllPromotionalProduct =
  (pageNo, pageSize) => async (dispatch) => {
    dispatch(getAllProductStart());
    try {
      const res = await axios.get(
        `api/v1/product/list-promotion?pageNo=${pageNo}&pageSize=${pageSize}&sortBy=name:asc`
      );
      dispatch(getAllPromotionalProductSuccess(res.data.data.items));
    } catch (error) {
      dispatch(getAllProductFailure(error.message));
    }
  };

export const getSearch = (search) => async (dispatch) => {
  dispatch(getAllProductStart());
  try {
    dispatch(getSearchSuccess(search));
  } catch (error) {
    dispatch(getAllProductFailure(error.message));
  }
};

export const getProductId = (productId) => async (dispatch) => {
  dispatch(getAllProductStart());
  try {
    const res = await axios.get(`api/v1/product/${productId}`);
    dispatch(getProductIdSuccess(res.data.data));
  } catch (error) {
    dispatch(getAllProductFailure(error.message));
  }
};

export const listProduct = (state) => state.product.product;
export const listProductSearch = (state) => state.product.productSearch;
export const wordSearch = (state) => state.product.search;
export const productId = (state) => state.product.productId;
export const promotionalProduct = (state) => state.product.promotionalProduct;

export default ProductSlice.reducer;
