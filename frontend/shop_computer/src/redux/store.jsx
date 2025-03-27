import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./slices/authSlice";
import CategorySlice from "./slices/CategorySlice";
import cartSlice from "./slices/cartSlice";
import ProductSlice from "./slices/ProductSlice";
import CheckoutSlice from "./slices/CheckoutSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    product: ProductSlice,
    category: CategorySlice,
    cart: cartSlice,
    checkout: CheckoutSlice,
  },
});
