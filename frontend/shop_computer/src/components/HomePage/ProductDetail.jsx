import { useDispatch, useSelector } from "react-redux";
import {
  selectCartItems,
  increaseQuantity,
  addToCart,
} from "../../redux/slices/cartSlice";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShieldAlt,
  faHeadset,
  faMoneyBillWave,
  faBoxOpen,
  faExchangeAlt,
} from "@fortawesome/free-solid-svg-icons";
import { useEffect, useState } from "react";
import "./ProductDetail.scss";
import { useNavigate } from "react-router-dom";

const ProductDetail = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const cartItems = useSelector(selectCartItems);
  const product = useSelector((state) => state.product.productId);
  const [showPopup, setShowPopup] = useState(false);

  // Local state for selected quantity
  const [selectedQuantity, setSelectedQuantity] = useState(1);

  useEffect(() => {}, [dispatch, cartItems, product]);

  const filterProduct = cartItems.find((item) => item.id === product?.id);

  if (!product) {
    return <div>Loading...</div>;
  }

  const calculateDiscountedPrice = (price, discount) => {
    if (discount) {
      const discountedPrice = price * (1 - discount / 100);
      return discountedPrice.toFixed(0);
    }
    return price.toFixed(0);
  };

  const handleAddToCart = () => {
    if (filterProduct) {
      // If the product is already in the cart, increase the quantity by selectedQuantity
      dispatch(
        increaseQuantity({ id: filterProduct.id, quantity: selectedQuantity })
      );
    } else {
      // If the product is not in the cart, add it with the selected quantity
      const newProduct = { ...product, quantity: selectedQuantity };
      dispatch(addToCart(newProduct));
    }
    setShowPopup(true);

    // Hide popup after 5 seconds
    setTimeout(() => {
      setShowPopup(false);
    }, 5000);
  };

  const handleQuantityChange = (change) => {
    setSelectedQuantity((prev) => {
      const newQuantity = prev + change;
      // Ensure the quantity stays within bounds
      if (newQuantity >= 1 && newQuantity <= product.stock) {
        return newQuantity;
      }
      return prev;
    });
  };

  const handlePay = () => {
    const productId = product.id;

    // Xóa products trong localStorage nếu nó tồn tại
    localStorage.removeItem("products");

    // Tạo mảng chứa sản phẩm mới
    const productsArray = [product];

    // Lưu sản phẩm vào localStorage
    localStorage.setItem("products", JSON.stringify(productsArray));

    // Chuyển hướng đến trang thanh toán
    navigate(`/checkout/${productId}`);
  };

  const { name, thumbnail, price, stock, discount, category, supplier } =
    product;

  return (
    <div className="product">
      <>
        <div className="col-sm-2"></div>
        <div className="product-detail col-sm-10">
          <div className="product-image col-sm-4">
            <img src={thumbnail} alt={name} />
          </div>
          <div className="product-info col-sm-8">
            <h1 className="product-name">{name}</h1>
            <div className="d-flex">
              <div className="col-sm-7">
                <p className="product-price">
                  {discount > 0 ? (
                    <>
                      <span className="original-price">
                        {price.toLocaleString()} VND
                      </span>
                      <span className="discounted-price">
                        {(price * (1 - discount / 100)).toLocaleString()} VND
                      </span>
                    </>
                  ) : (
                    `${price.toLocaleString()} VND`
                  )}
                </p>
                <p className="product-stock">Số lượng trong kho: {stock}</p>
                <div className="product-category">
                  <strong>Danh mục:</strong> {category.name}
                </div>
                <div className="product-supplier">
                  <strong>Nhà cung cấp:</strong> {supplier.name}
                </div>
                <p className="supplier-info">
                  <strong>Thông tin nhà cung cấp:</strong>{" "}
                  {supplier.description}
                </p>
                <div className="supplier-contact">
                  <strong>Liên hệ:</strong> {supplier.phone} | {supplier.email}
                </div>
                <div className="quantity-controls d-flex">
                  <strong className="quantity-text">Số lượng:</strong>
                  <button
                    disabled={selectedQuantity <= 1}
                    onClick={() => handleQuantityChange(-1)}
                    className="black-text"
                  >
                    -
                  </button>
                  <button className="red-text">{selectedQuantity}</button>
                  <button
                    disabled={selectedQuantity >= stock}
                    onClick={() => handleQuantityChange(1)}
                    className="black-text"
                  >
                    +
                  </button>
                </div>
                <div className="action-buttons">
                  <button className="add-to-cart" onClick={handleAddToCart}>
                    <span>Thêm vào giỏ hàng</span>
                  </button>
                  <button className="buy-now" onClick={() => handlePay()}>
                    <span>Mua ngay</span>
                  </button>
                </div>
              </div>
              <div className="col-sm-1"></div>
              <div className="sales-policy col-sm-4">
                <h2>Chính sách bán hàng</h2>
                <ul>
                  <li>
                    <FontAwesomeIcon icon={faShieldAlt} />
                    <strong>Cam kết 100% chính hãng</strong>
                  </li>
                  <li>
                    <FontAwesomeIcon icon={faHeadset} />
                    <strong>Hỗ trợ 24/7</strong>
                  </li>
                </ul>
                <h3>Thông tin thêm</h3>
                <ul>
                  <li>
                    <FontAwesomeIcon icon={faMoneyBillWave} />
                    <strong>Hoàn tiền</strong>: 111% nếu hàng giả
                  </li>
                  <li>
                    <FontAwesomeIcon icon={faBoxOpen} />
                    <strong>Mở hộp</strong>: kiểm tra nhận hàng
                  </li>
                  <li>
                    <FontAwesomeIcon icon={faExchangeAlt} />
                    <strong>Đổi trả trong</strong>: 7 ngày
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div className="col-sm-2"></div>
      </>

      {/* Popup for successful add to cart */}
      {showPopup && (
        <div className="cart-popup">
          <div className="cart-popup-content">
            <img src={product.thumbnail} alt={product.name} />
            <div>
              <p>{product.name}</p>
              <p>
                ${calculateDiscountedPrice(product.price, product.discount)}
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductDetail;
