import React, { useEffect, useRef } from "react";
import { useSelector } from "react-redux";
import { selectCartItems } from "../../redux/slices/cartSlice";
import "./Cart.scss";
import { FaShoppingCart } from "react-icons/fa";

const Cart = ({ setShowCartPopup }) => {
  const cartItems = useSelector(selectCartItems);
  const cartRef = useRef(null);

  const calculateDiscountedPrice = (price, discount) => {
    if (discount) {
      const discountedPrice = price * (1 - discount / 100);
      return discountedPrice.toFixed(0);
    }
    return price.toFixed(0);
  };

  const calculateTotalPrice = () => {
    let totalPrice = 0;
    cartItems.forEach((item) => {
      const itemPrice = calculateDiscountedPrice(item.price, item.discount);
      totalPrice += item.quantity * parseFloat(itemPrice); // Sử dụng item.quantity thay vì cartItems.quantity
    });
    return totalPrice;
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (cartRef.current && !cartRef.current.contains(event.target)) {
        setShowCartPopup(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [setShowCartPopup]);

  return (
    <div ref={cartRef} className="cart-popup">
      <div className="cart-popup-content">
        <p className="text-center">Cart</p>
        <div className="cart-items-list">
          {cartItems.length > 0 ? (
            <ul className={cartItems.length >= 4 ? "scrollable-list" : ""}>
              {cartItems.map((item) => (
                <li key={item.id}>
                  <img src={item.thumbnail} alt={item.name} />
                  <div>
                    <p>{item.name}</p>
                    <p>
                      ${calculateDiscountedPrice(item.price, item.discount)}
                    </p>
                    <p>Quantity: {item.quantity}</p>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <div className="empty-cart d-flex flex-column justify-content-center align-items-center">
              <FaShoppingCart
                className="empty-cart-icon"
                color="orange"
                size={25}
              />
              <p className="empty-cart-text m-0 ms-2">
                Currently no items in cart
              </p>
            </div>
          )}
        </div>
        <div className="total d-flex justify-content-between">
          <h3>Total:</h3>
          <p>{formatPrice(calculateTotalPrice())}</p>
        </div>
        <button className="close-btn btn btn-warning">View cart</button>
      </div>
    </div>
  );
};

export default Cart;
