import React, { useRef } from "react";
import { FaShoppingCart } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  removeFromCart,
  increaseQuantity,
  decreaseQuantity,
  selectCartItems,
} from "../../redux/slices/cartSlice";
import "./Cart.scss";

const Cart = ({ setShowCartPopup }) => {
  const cartRef = useRef(null);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const cartItems = useSelector(selectCartItems);
  // const [cartItems, setCartItems] = useState();

  const handleClickOutside = (event) => {
    if (cartRef.current && !cartRef.current.contains(event.target)) {
      setShowCartPopup(false);
    }
  };

  // Attach and clean up event listener
  React.useEffect(() => {
    console.log("check cart", cartItems);
    document.addEventListener("mousedown", handleClickOutside);
    //setCartItems(cartItem);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const calculateDiscountedPrice = (price, discount) => {
    if (discount) {
      const discountedPrice = price * (1 - discount / 100);
      return discountedPrice.toFixed(0);
    }
    return price.toFixed(0);
  };

  const calculateTotalPrice = () => {
    return cartItems.reduce((totalPrice, item) => {
      const itemPrice = calculateDiscountedPrice(item.price, item.discount);
      return totalPrice + item.quantity * parseFloat(itemPrice);
    }, 0);
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
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

  const handleViewCart = () => {
    setShowCartPopup(false);
    navigate("/view-cart");
  };

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
                      {formatPrice(
                        calculateDiscountedPrice(item.price, item.discount)
                      )}
                    </p>
                    <div className="quantity-controls d-flex align-items-center">
                      <p>Quantity: </p>
                      <div className="d-flex justify-content-center align-items-center">
                        <button
                          disabled={item.quantity <= 1}
                          onClick={() => handleQuantityChange(item.id, -1)}
                        >
                          -
                        </button>
                        <p>{item.quantity}</p>
                        <button
                          onClick={() => handleQuantityChange(item.id, 1)}
                        >
                          +
                        </button>
                        <button
                          className="remove-btn"
                          onClick={() => handleRemoveItem(item.id)}
                        >
                          Remove
                        </button>
                      </div>
                    </div>
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
        <button className="close-btn btn btn-warning" onClick={handleViewCart}>
          View cart
        </button>
      </div>
    </div>
  );
};

export default Cart;
