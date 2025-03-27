import "./ViewCheckout.scss";
import React, { useEffect, useState } from "react";
import { ACCESS_TOKEN } from "../../constants/index";
import { jwtDecode } from "jwt-decode";
import {
  getOrderDetailByUserId,
  deleteOrderDetailByUserId,
} from "../../services/OrderDetailService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { toast } from "react-toastify";
import { getOrderByUserEmail } from "../../services/OrdersService";

const ViewCheckout = () => {
  const [orders, setOrders] = useState([]);
  const [userId, setUserId] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [orderId, setOrderId] = useState(0);
  const [email, setEmail] = useState("");

  useEffect(() => {
    getUserId();
  }, []);

  const getUserId = () => {
    const accessToken = localStorage.getItem(ACCESS_TOKEN);
    if (accessToken) {
      const decodedToken = jwtDecode(accessToken);
      const id = decodedToken.id;
      const emailUser = decodedToken.sub;
      setEmail(emailUser);
      setUserId(id);
    }
  };

  const getOrder = async () => {
    const res = await getOrderByUserEmail(email);
    if (res.data.status === 200) {
      setOrders(res.data.data); // Setting orders based on the API response
    } else {
      setOrders([]);
    }
  };

  useEffect(() => {
    if (userId) {
      getOrder();
    }
  }, [userId]);

  const handleModal = async (orderId) => {
    setOrderId(orderId);
    setShowModal(true);
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(price);
  };

  const calculateDiscountedPrice = (price, discount) => {
    return price * (1 - discount / 100);
  };

  const calculateTotalPrice = () => {
    return orders.reduce((totalPrice, order) => {
      const orderTotal = order.orderDetails.reduce(
        (orderDetailPrice, detail) => {
          const itemPrice = calculateDiscountedPrice(
            detail.product.price,
            detail.product.discount || 0
          );
          return orderDetailPrice + itemPrice * detail.quantity;
        },
        0
      );
      return totalPrice + orderTotal;
    }, 0);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleDeleteOrder = async () => {
    const res = await deleteOrderDetailByUserId(orderId);
    if (res.data.status === 200) {
      toast.success("Bill deleted successfully");
      getOrder();
      setShowModal(false);
    } else {
      toast.error("Invoice was not deleted successfully");
    }
  };

  return (
    <div className="order-details">
      <h2>Chi Tiết Đơn Hàng</h2>
      <table>
        <thead>
          <tr>
            <th>Mã Đơn Hàng</th>
            <th>Tên Người Nhận</th>
            <th>Email</th>
            <th>Số Điện Thoại</th>
            <th>Địa Chỉ Giao Hàng</th>
            <th>Ngày Đặt Hàng</th>
            <th>Trạng Thái</th>
            <th>Tên Sản Phẩm</th>
            <th>Số Lượng</th>
            <th>Giá</th>
            <th>Giảm Giá</th>
            <th>Thành Tiền</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => {
            return order.orderDetails.map((item) => {
              const discount = item.product?.discount || 0;
              const discountedPrice = calculateDiscountedPrice(
                item.product?.price,
                discount
              );
              const totalPrice = (discountedPrice * item.quantity).toFixed(0);

              return (
                <tr key={item.id}>
                  <td>{order.id}</td>
                  <td>{order.fullname}</td>
                  <td>{order.email}</td>
                  <td>{order.phone}</td>
                  <td>{order.address}</td>
                  <td>{new Date(item.createdAt).toLocaleString()}</td>
                  <td>{order.status}</td>
                  <td>{item.product.name}</td>
                  <td>{item.quantity}</td>
                  <td>{formatPrice(item.product.price)}</td>
                  <td>{discount}%</td>
                  <td>{formatPrice(totalPrice)}</td>
                  <td>
                    <button
                      onClick={() => handleModal(order.id)}
                      className="delete-btn"
                    >
                      <FontAwesomeIcon icon={faTrash} />
                    </button>
                  </td>
                </tr>
              );
            });
          })}
          {orders.length > 0 && (
            <tr>
              <td colSpan={10} className="total">
                Tổng: {formatPrice(calculateTotalPrice())}
              </td>
            </tr>
          )}
        </tbody>
      </table>

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
              <p>Are you sure you want to delete this invoice?</p>
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-primary"
                onClick={handleDeleteOrder}
              >
                Đồng ý
              </button>
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

export default ViewCheckout;
