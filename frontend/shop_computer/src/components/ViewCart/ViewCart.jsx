import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  selectCartItems,
  increaseQuantity,
  decreaseQuantity,
  removeFromCart,
} from "../../redux/slices/cartSlice";
import "./ViewCart.scss";

const ViewCart = () => {
  const dispatch = useDispatch();
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
      dispatch(increaseQuantity({ id }));
    } else {
      dispatch(decreaseQuantity({ id }));
    }
  };

  const handleRemoveItem = (id) => {
    dispatch(removeFromCart({ id }));
  };

  return (
    <div className="view-cart">
      <div className="container">
        <div className="cart-items">
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
        <div className="payment-info">
          <h2>Payment Information</h2>
          <div className="total">
            <h3>Total:</h3>
            <p>{formatPrice(calculateTotalPrice())}</p>
          </div>
          <div className="checkout-btn">
            <button>Proceed to Checkout</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewCart;
