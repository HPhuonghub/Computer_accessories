import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  items: [], // Mảng các sản phẩm trong giỏ hàng
};

export const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addToCart: (state, action) => {
      // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
      const existingItem = state.items.find(
        (item) => item.id === action.payload.id
      );

      if (existingItem) {
        // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng lên 1
        existingItem.quantity++;
      } else {
        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào giỏ hàng
        state.items.push({ ...action.payload, quantity: 1 });
      }
    },
    removeFromCart: (state, action) => {
      // Xoá sản phẩm khỏi giỏ hàng
      state.items = state.items.filter((item) => item.id !== action.payload.id);
    },
    increaseQuantity: (state, action) => {
      // Tăng số lượng sản phẩm trong giỏ hàng
      const item = state.items.find((item) => item.id === action.payload.id);
      if (item) {
        item.quantity++;
      }
    },
    decreaseQuantity: (state, action) => {
      // Giảm số lượng sản phẩm trong giỏ hàng
      const item = state.items.find((item) => item.id === action.payload.id);
      if (item && item.quantity > 1) {
        item.quantity--;
      }
    },
    clearCart: (state) => {
      // Xoá toàn bộ sản phẩm trong giỏ hàng
      state.items = [];
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
