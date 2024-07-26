import React, { useState } from "react";
import "./ProductList.scss";
import { FaShoppingCart } from "react-icons/fa";
import { useDispatch } from "react-redux";
import { addToCart } from "../../redux/slices/cartSlice";

const ProductList = ({ products }) => {
  const dispatch = useDispatch();
  const [showPopup, setShowPopup] = useState(false);
  const [addedProduct, setAddedProduct] = useState(null);

  const addToCartAndShowPopup = (product) => {
    dispatch(addToCart(product));
    setAddedProduct(product);
    setShowPopup(true);

    setTimeout(() => {
      setShowPopup(false);
    }, 5000);
  };

  return (
    <div className="product-list">
      <h2>Products</h2>
      <ul className="product-grid">
        {products.map((product) => (
          <li key={product.id}>
            <img src={product.thumbnail} alt={product.name} />
            <div className="product-info">
              <h3 className="product-name">{product.name.toLowerCase()}</h3>
              <div className="price-container">
                <p className="price d-flex flex-column">
                  ${calculateDiscountedPrice(product.price, product.discount)}
                  {product.discount && (
                    <span className="original-price">
                      ${product.price.toFixed(0)}
                    </span>
                  )}
                </p>
                {product.discount && (
                  <div className="discount-badge">{product.discount}%</div>
                )}
              </div>
              <p>{product.description}</p>
              <div
                className="add-to-cart"
                onClick={() => addToCartAndShowPopup(product)}
              >
                <div className="cart-icon">
                  <FaShoppingCart />
                </div>
                <span> Add to Cart</span>
              </div>
            </div>
          </li>
        ))}
      </ul>

      {/* Popup for successful add to cart */}
      {showPopup && (
        <div className="cart-popup">
          <div className="cart-popup-content">
            <img src={addedProduct.thumbnail} alt={addedProduct.name} />
            <div>
              <p>{addedProduct.name}</p>
              <p>
                $
                {calculateDiscountedPrice(
                  addedProduct.price,
                  addedProduct.discount
                )}
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

const calculateDiscountedPrice = (price, discount) => {
  if (discount) {
    const discountedPrice = price * (1 - discount / 100);
    return discountedPrice.toFixed(0);
  }
  return price.toFixed(0);
};

export default ProductList;
