import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "./CheckoutPage.scss";
import { toast } from "react-toastify";
import {
  postPaymentCOD,
  postPaymentPayOS,
} from "../../services/PaymentService";
import { useParams } from "react-router-dom";
import { useSelector } from "react-redux";

const CheckoutPage = () => {
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState("");
  const [selectedDistrict, setSelectedDistrict] = useState("");
  const [selectedWard, setSelectedWard] = useState("");
  const [cartItems, setCartItems] = useState([]);
  const [paymentMethod, setPaymentMethod] = useState("cod");
  const products = localStorage.getItem("products");
  const [showModal, setShowModal] = useState(false);
  const [modalContent, setModalContent] = useState("");
  const { productId } = useParams();
  const navigate = useNavigate();
  const isLogin = useSelector((state) => state.auth.isLoggedIn);

  // Fetch provinces
  useEffect(() => {
    const fetchProvinces = async () => {
      try {
        const response = await axios.get(
          "https://provinces.open-api.vn/api/p/"
        );
        setProvinces(response.data);
      } catch (error) {
        console.error("Error fetching provinces:", error);
      }
    };
    fetchProvinces();
  }, []);

  useEffect(() => {
    if (products) {
      setCartItems(JSON.parse(products));
    } else {
      toast.error("No products found in localStorage.");
    }
  }, []);

  // Fetch districts based on selected province
  useEffect(() => {
    if (selectedProvince) {
      const fetchDistricts = async () => {
        try {
          const response = await axios.get(
            `https://provinces.open-api.vn/api/p/${selectedProvince}?depth=2`
          );
          setDistricts(response.data.districts);
        } catch (error) {
          console.error("Error fetching districts:", error);
        }
      };
      fetchDistricts();
    } else {
      setDistricts([]); // Reset districts when no province is selected
    }
  }, [selectedProvince]);

  // Fetch wards based on selected district
  useEffect(() => {
    if (selectedDistrict) {
      const fetchWards = async () => {
        try {
          const response = await axios.get(
            `https://provinces.open-api.vn/api/d/${selectedDistrict}?depth=2`
          );
          setWards(response.data.wards || []); // Handle case where wards might not be returned
        } catch (error) {
          console.error("Error fetching wards:", error);
        }
      };
      fetchWards();
    } else {
      setWards([]); // Reset wards when no district is selected
    }
  }, [selectedDistrict]);

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
      if (item.quantity === undefined)
        return totalPrice + parseFloat(itemPrice);
      return totalPrice + item.quantity * parseFloat(itemPrice);
    }, 0);
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  const resolveData = (arr) => {
    console.log("check arr:", arr);
    let list = [];
    for (let i = 0; i < arr.length; i++) {
      list.push({
        quantity: arr[i].quantity,
        product: arr[i],
      });
    }
    console.log("check list:", list);
    return list;
  };

  const handlePay = async () => {
    if (!isLogin) {
      navigate("/login");
      return;
    }

    const name = document.querySelector('input[placeholder="Họ và tên"]').value;
    const email = document.querySelector('input[placeholder="Email"]').value;
    const address = document.querySelector(
      'input[placeholder="Địa chỉ"]'
    ).value;
    const phone = document.querySelector(
      'input[placeholder="Số điện thoại"]'
    ).value;

    if (
      !name ||
      !email ||
      !address ||
      !phone ||
      !selectedProvince ||
      !selectedDistrict ||
      !selectedWard
    ) {
      toast.error("Vui lòng điền đầy đủ thông tin.");
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
      toast.error("Email không hợp lệ.");
      return;
    }

    const phonePattern = /^[0-9]{10,11}$/;
    if (!phonePattern.test(phone)) {
      toast.error("Số điện thoại không hợp lệ.");
      return;
    }

    // Kiểm tra và tìm tên tỉnh, quận, phường
    const province = provinces.find(
      (province) => province.code == selectedProvince
    );
    const district = districts.find(
      (district) => district.code == selectedDistrict
    );
    const ward = wards.find((ward) => ward.code == selectedWard);

    const provinceName = province ? province.name : "Không tìm thấy";
    const districtName = district ? district.name : "Không tìm thấy";
    const wardName = ward ? ward.name : "Không tìm thấy";

    const json = JSON.parse(products);
    const userString = localStorage.getItem("loggedInUser");

    // Chuyển đổi chuỗi JSON thành đối tượng
    const userId = userString ? JSON.parse(userString) : null;

    // Lấy email từ đối tượng user
    const userEmail = userId ? userId.email : null;

    // Format dữ liệu để gửi lên server
    const data1 = {
      fullname: name,
      email: email,
      phone: phone,
      address:
        address + " " + wardName + " " + districtName + " " + provinceName,
      status: "PENDING",
      user: {
        email: userEmail,
      },
      paymentMethod: {
        name: paymentMethod.toUpperCase(),
      },
      orderDetailsList: resolveData(json),
    };

    try {
      if (paymentMethod === "cod") {
        const res = await postPaymentCOD(data1);
        if (res.data.status === 200) {
          setModalContent("Đơn hàng đã được hoàn tất!");
          setShowModal(true);
        } else {
          toast.error(res.data.message);
        }
      } else {
        const res = await postPaymentPayOS(data1);
        window.location.href = res.data;
      }
    } catch (error) {
      // Kiểm tra xem lỗi có phản hồi từ server không
      if (error.response) {
        // Lỗi từ server (có thể là lỗi HTTP)
        toast.error(
          error.response.data.message || "Có lỗi xảy ra khi gửi yêu cầu."
        );
      } else if (error.request) {
        // Lỗi do không nhận được phản hồi từ server (có thể do mất kết nối, lỗi mạng...)
        toast.error("Không thể kết nối tới server. Vui lòng thử lại.");
      } else {
        // Lỗi khác (có thể là lỗi trong khi cấu hình hoặc khi gửi yêu cầu)
        toast.error("Có lỗi xảy ra: " + error.message);
      }
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  return (
    <div className="checkout-container">
      <div className="checkout-wrapper">
        <div className="checkout-info">
          <h1 className="shop-title">
            TTTT - Phụ kiện Game Thủ | Tư vấn Build PC giá rẻ
          </h1>
          {productId === "-1" ? (
            <nav className="nav">
              <Link to="/view-cart" className="nav-link">
                Giỏ hàng
              </Link>

              <span className="title-nav">
                &gt;&nbsp;&nbsp;Thông tin giao hàng
              </span>
            </nav>
          ) : (
            <nav className="nav">
              <Link to={`/product/${productId}`} className="nav-link">
                Sản phẩm
              </Link>

              <span className="title-nav">
                &gt;&nbsp;&nbsp;Thông tin giao hàng
              </span>
            </nav>
          )}

          <h2 className="section-title">Thông tin giao hàng</h2>
          <form>
            <div className="account-info">
              <label>
                Bạn đã có tài khoản ?&nbsp;
                <a href="/login" className="login-link">
                  {" "}
                  Đăng nhập
                </a>
              </label>
            </div>
            <div className="input-grid">
              <input
                placeholder="Họ và tên"
                type="text"
                className="input-field"
              />
              <input placeholder="Email" type="email" className="input-field" />
              <input
                placeholder="Địa chỉ"
                type="text"
                className="input-field col-span-2"
              />
              <input
                placeholder="Số điện thoại"
                type="text"
                className="input-field"
              />
            </div>
            <div className="select-grid">
              <select
                className="input-field"
                value={selectedProvince}
                onChange={(e) => {
                  setSelectedProvince(e.target.value);
                  setSelectedDistrict(""); // Reset district when province changes
                  setWards([]); // Reset wards when province changes
                }}
              >
                <option value="">Tỉnh / thành</option>
                {provinces.map((province) => (
                  <option key={province.code} value={province.code}>
                    {province.name}
                  </option>
                ))}
              </select>
              <select
                className="input-field"
                value={selectedDistrict}
                onChange={(e) => {
                  setSelectedDistrict(e.target.value);
                  setWards([]); // Reset wards when district changes
                }}
                disabled={!selectedProvince}
              >
                <option value="">Quận / huyện</option>
                {districts.map((district) => (
                  <option key={district.code} value={district.code}>
                    {district.name}
                  </option>
                ))}
              </select>
              <select
                className="input-field"
                value={selectedWard}
                onChange={(e) => setSelectedWard(e.target.value)}
                disabled={!selectedDistrict}
              >
                <option value="">Phường / xã</option>
                {wards.map((ward) => (
                  <option key={ward.code} value={ward.code}>
                    {ward.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <h2 className="section-title">Phương thức thanh toán</h2>
              <div className="payment-method">
                <div className="payment-option">
                  <input
                    id="cod"
                    name="payment"
                    type="radio"
                    value="cod"
                    checked={paymentMethod === "cod"}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  &nbsp;
                  <label htmlFor="cod">
                    &nbsp;
                    <img
                      src="https://cdn.iconscout.com/icon/free/png-256/free-cod-icon-download-in-svg-png-gif-file-formats--credit-debit-bank-transaction-payment-methods-vol-1-pack-business-icons-32259.png?f=webp&w=256"
                      alt="COD icon"
                    />
                    &nbsp;Thanh toán khi giao hàng (COD)
                  </label>
                </div>
                <div className="payment-option">
                  <input
                    id="bank"
                    name="payment"
                    type="radio"
                    value="bank"
                    checked={paymentMethod === "bank"}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  &nbsp;
                  <label htmlFor="bank">
                    &nbsp;
                    <img
                      src="https://static.thenounproject.com/png/384617-200.png"
                      alt="Bank transfer icon"
                    />
                    &nbsp;Chuyển khoản qua ngân hàng
                  </label>
                </div>
              </div>
            </div>

            <button className="submit-button" type="button" onClick={handlePay}>
              Hoàn tất đơn hàng
            </button>
          </form>
        </div>
        <div className="checkout-summary">
          {cartItems &&
            cartItems.map((item) => (
              <div className="product-info">
                <img src={item.thumbnail} alt="Product image" />
                <div className="d-flex">
                  <p className="product-description">{item.name}</p>
                  <div>
                    {item.discount > 0 ? (
                      <span className="product-price-text">
                        {item.price.toFixed(0)}vnđ
                      </span>
                    ) : (
                      <span className="product-price">
                        {item.price.toFixed(0)}vnđ
                      </span>
                    )}

                    {item.discount > 0 ? (
                      <div className="discount-badge">-{item.discount}%</div>
                    ) : (
                      <div />
                    )}

                    <p className="product-price">
                      {calculateDiscountedPrice(item.price, item.discount)}vnđ
                    </p>
                  </div>
                </div>
              </div>
            ))}

          <div className="discount-code d-flex justify-content-around align-items-center">
            <input
              placeholder="Mã giảm giá"
              type="text"
              className="input-field"
            />
            <button className="discount-button">Sử dụng</button>
          </div>

          <div className="price-summary">
            <div className="price-row">
              <span>Tạm tính</span>
              <span>{formatPrice(calculateTotalPrice())}</span>
            </div>
            <div className="price-row">
              <span>Phí vận chuyển</span>
              <span>-</span>
            </div>
            <div className="price-row">
              <span>Tổng cộng</span>
              <span className="total-amount">
                {formatPrice(calculateTotalPrice())}
              </span>
            </div>
          </div>
        </div>
      </div>
      <footer className="footer">Powered by HDP</footer>
      {/* Modal */}
      {showModal && (
        <div className="modal">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">Thông báo</h5>
              <button
                type="button"
                className="close"
                onClick={handleCloseModal}
              >
                &times;
              </button>
            </div>
            <div className="modal-body">
              <p>{modalContent}</p>
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={handleCloseModal}
              >
                Đóng
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CheckoutPage;
