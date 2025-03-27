import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  items: JSON.parse(localStorage.getItem("cart")) || [], // Initialize from local storage
};

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addToCart: (state, action) => {
      const existingItem = state.items.find(
        (item) => item.id === action.payload.id
      );

      if (existingItem) {
        existingItem.quantity += action.payload.quantity; // Increment by the quantity added
      } else if (action.payload.quantity) {
        state.items.push({
          ...action.payload,
          quantity: action.payload.quantity,
        });
      } else {
        state.items.push({
          ...action.payload,
          quantity: 1,
        });
      }

      // Save to local storage
      localStorage.setItem("cart", JSON.stringify(state.items));
    },
    removeFromCart: (state, action) => {
      state.items = state.items.filter((item) => item.id !== action.payload.id);

      // Save to local storage
      localStorage.setItem("cart", JSON.stringify(state.items));
    },
    increaseQuantity: (state, action) => {
      const item = state.items.find((item) => item.id === action.payload.id);
      if (item) {
        item.quantity += action.payload.quantity; // Increment by specified quantity
        // Save to local storage
        localStorage.setItem("cart", JSON.stringify(state.items));
      }
    },
    decreaseQuantity: (state, action) => {
      const item = state.items.find((item) => item.id === action.payload.id);
      if (item && item.quantity > 1) {
        item.quantity--;
        // Save to local storage
        localStorage.setItem("cart", JSON.stringify(state.items));
      }
    },
    clearCart: (state) => {
      state.items = [];
      // Save to local storage
      localStorage.removeItem("cart");
    },
  },
});

export const selectCartItems = (state) => state.cart.items;
export const selectCartTotalItems = (state) =>
  state.cart.items.reduce((total, item) => total + item.quantity, 0);

export const {
  addToCart,
  removeFromCart,
  increaseQuantity,
  decreaseQuantity,
  clearCart,
} = cartSlice.actions;

export default cartSlice.reducer;
