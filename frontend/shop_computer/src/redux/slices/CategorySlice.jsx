import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
  category: null,
  isLoading: false,
  error: null,
};

export const CategorySlice = createSlice({
  name: "category",
  initialState,
  reducers: {
    getAllCategoryStart: (state) => {
      state.isLoading = true;
      state.error = null;
    },
    getAllCategorySuccess: (state, action) => {
      state.isLoading = false;
      state.category = action.payload;
    },
    getAllCategoryFailure: (state, action) => {
      state.isLoading = false;
      state.error = action.payload;
    },
  },
});

export const {
  getAllCategoryStart,
  getAllCategorySuccess,
  getAllCategoryFailure,
} = CategorySlice.actions;

export const getAllCategory = () => async (dispatch) => {
  dispatch(getAllCategoryStart());
  try {
    const res = await axios.get("http://localhost:8888/api/v1/category/list");
    dispatch(getAllCategorySuccess(res.data));
  } catch (error) {
    dispatch(getAllCategoryFailure(error.message));
  }
};

export const listCategory = (state) => state.category.category;

export default CategorySlice.reducer;
