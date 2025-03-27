import React, { useState, useEffect } from "react";
import Carousel from "react-multi-carousel";
import "./Promotion.scss";
import "react-multi-carousel/lib/styles.css";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getProductId } from "../../redux/slices/ProductSlice";
import { addToCart } from "../../redux/slices/cartSlice";

const responsive = {
  superLargeDesktop: {
    // the naming can be any, depends on you.
    breakpoint: { max: 4000, min: 3000 },
    items: 5,
  },
  desktop: {
    breakpoint: { max: 3000, min: 1024 },
    items: 5,
  },
  tablet: {
    breakpoint: { max: 1024, min: 464 },
    items: 3,
  },
  mobile: {
    breakpoint: { max: 464, min: 0 },
    items: 1,
  },
};

const Promotion = ({ products }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [showPopup, setShowPopup] = useState(false);
  const [addedProduct, setAddedProduct] = useState(null);

  useEffect(() => {}, [products]);

  const addToCartAndShowPopup = (product) => {
    // Add product to cart in Redux
    const newProduct = { ...product, quantity: 1 };
    dispatch(addToCart(newProduct));

    setAddedProduct(product);
    setShowPopup(true);

    // Hide popup after 5 seconds
    setTimeout(() => {
      setShowPopup(false);
    }, 5000);
  };

  const handleProductDetail = (productId) => {
    navigate(`/product/${productId}`);
    dispatch(getProductId(productId));
  };

  return (
    <div className="promotion-container">
      {/* Header và Navigation */}
      <div className="header">
        <h1>
          <i className="fas fa-bolt"></i> &nbsp;&nbsp;&nbsp;KHUYẾN MẠI SHOCK
          NHẤT&nbsp;&nbsp; <i className="fas fa-fire"></i>
        </h1>
      </div>

      <div className="product-grid">
        {products.length > 0 ? (
          <Carousel
            responsive={responsive}
            itemClass="carousel-item-padding-40-px"
          >
            {products.map((product, index) => (
              <div key={index} className="product-card">
                <img
                  src={product.thumbnail}
                  alt={product.name}
                  onClick={() => handleProductDetail(product.id)}
                />
                <div className="product-info">
                  <h2
                    className="product-title"
                    onClick={() => handleProductDetail(product.id)}
                  >
                    {product.name}
                  </h2>
                  <div className="price-info">
                    <div>
                      <span className="price d-flex flex-column">
                        $
                        {calculateDiscountedPrice(
                          product.price,
                          product.discount
                        )}
                        {product.discount && (
                          <span className="original-price">
                            ${product.price.toFixed(0)}
                          </span>
                        )}
                      </span>
                      <span className="old-price">{product.oldPrice}</span>
                    </div>
                    <span className="discount">{product.discount}%</span>
                  </div>
                  <button
                    className="add-to-cart"
                    onClick={() => addToCartAndShowPopup(product)}
                  >
                    <i className="fas fa-shopping-bag"></i> THÊM VÀO GIỎ
                  </button>
                </div>
              </div>
            ))}
          </Carousel>
        ) : (
          <p>Không có sản phẩm nào để hiển thị.</p>
        )}
      </div>

      <div className="view-all">
        <button>
          Xem tất cả &nbsp;&nbsp; <i className="fas fa-bolt"></i>&nbsp;&nbsp;
          KHUYẾN MẠI SHOCK NHẤT &nbsp;&nbsp;<i className="fas fa-fire"></i>
        </button>
      </div>

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

export default Promotion;
