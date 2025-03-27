import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  selectCartItems,
  increaseQuantity,
  decreaseQuantity,
  removeFromCart,
} from "../../redux/slices/cartSlice";
import "./ViewCart.scss";
import { useNavigate } from "react-router-dom";

const ViewCart = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const cartItems = useSelector(selectCartItems);

  useEffect(() => {
    // Sync local storage with Redux store on component mount
    const savedCartItems = JSON.parse(localStorage.getItem("cart")) || [];
    const savedCartItemMap = new Map(
      savedCartItems.map((item) => [item.id, item])
    );

    cartItems.forEach((item) => {
      const savedItem = savedCartItemMap.get(item.id);
      if (savedItem && savedItem.quantity !== item.quantity) {
        const quantityChange = savedItem.quantity - item.quantity;
        if (quantityChange > 0) {
          dispatch(increaseQuantity({ id: item.id, quantity: quantityChange }));
        } else {
          dispatch(
            decreaseQuantity({
              id: item.id,
              quantity: Math.abs(quantityChange),
            })
          );
        }
      }
    });
  }, [dispatch, cartItems]);

  const calculateDiscountedPrice = (price, discount) => {
    if (discount) {
      const discountedPrice = price * (1 - discount / 100);
      return discountedPrice.toFixed(0);
    }
    return price.toFixed(0);
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  const calculateTotalPrice = () => {
    return cartItems.reduce((totalPrice, item) => {
      const itemPrice = calculateDiscountedPrice(item.price, item.discount);
      return totalPrice + item.quantity * parseFloat(itemPrice);
    }, 0);
  };

  const handleQuantityChange = (id, change) => {
    if (change > 0) {
      dispatch(increaseQuantity({ id, quantity: 1 }));
    } else {
      dispatch(decreaseQuantity({ id, quantity: 1 }));
    }
  };

  const handleRemoveItem = (id) => {
    dispatch(removeFromCart({ id }));
  };

  const handlePay = () => {
    localStorage.setItem("products", JSON.stringify(cartItems));
    navigate(`/checkout/-1`);
  };

  return (
    <div className="view-cart">
      <div className="container">
        <div className="cart-items col-md-8">
          <h1>Your Cart</h1>
          {cartItems.length > 0 ? (
            <ul>
              {cartItems.map((item) => (
                <li key={item.id} className="cart-item">
                  <img src={item.thumbnail} alt={item.name} />
                  <div className="d-flex justify-content-between content">
                    <div className="flex-column align-items-center name-content">
                      <h2 className="item-name">{item.name}</h2>
                      <div className="d-flex justify-content-between align-items-center">
                        <p>
                          {formatPrice(
                            calculateDiscountedPrice(item.price, item.discount)
                          )}
                        </p>
                        <div className="quantity-controls d-flex">
                          <button
                            onClick={() => handleQuantityChange(item.id, -1)}
                            disabled={item.quantity <= 1}
                          >
                            -
                          </button>
                          <span>{item.quantity}</span>
                          <button
                            onClick={() => handleQuantityChange(item.id, 1)}
                          >
                            +
                          </button>
                        </div>
                      </div>
                    </div>
                    <button
                      className="btn-danger"
                      onClick={() => handleRemoveItem(item.id)}
                    >
                      Remove
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <p>Your cart is empty.</p>
          )}
        </div>
        <div className="payment-info col-md-4">
          <div>
            <h2>Payment Information</h2>

            <div class="d-flex justify-content-between align-items-center">
              <span>Tổng tiền:</span>
              <span class="order-total">
                <p>{formatPrice(calculateTotalPrice())}</p>
              </span>
            </div>
            <div class="mt-3">
              <ul class="list-unstyled">
                <li>Phí vận chuyển sẽ được tính ở trang thanh toán.</li>
                <li>Bạn cũng có thể nhập mã giảm giá ở trang thanh toán.</li>
                <li>Bạn cũng có thể nhập mã giảm giá ở trang thanh toán.</li>
              </ul>
            </div>
            <button
              class="btn btn-checkout w-100 mt-3"
              onClick={() => handlePay()}
            >
              THANH TOÁN
            </button>
          </div>
          <div class="policy-note mt-3">
            <div class="fw-bold">Chính sách mua hàng:</div>
            Hiện chúng tôi chỉ áp dụng thanh toán với đơn hàng có giá trị tối
            thiểu 40.000₫ trở lên.
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewCart;
