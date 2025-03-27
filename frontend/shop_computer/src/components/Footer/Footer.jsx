import React from "react";
import {
  FaFacebook,
  FaTwitter,
  FaGooglePlusG,
  FaYoutube,
} from "react-icons/fa";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMapMarkerAlt,
  faPhone,
  faEnvelope,
} from "@fortawesome/free-solid-svg-icons";

import "./Footer.scss";

const Footer = () => {
  return (
    <div>
      <div className="newsletter-section text-center d-flex justify-content-center align-items-center">
        <div>
          <h2>Đăng ký nhận bản tin</h2>
          <p>
            Để nhận các thông tin mới từ Techgo cũng như các chương trình khuyến
            mãi
          </p>
        </div>
        <div className="input-group">
          <input placeholder="Vui lòng nhập email của bạn..." type="email" />
          <button>ĐĂNG KÝ</button>
        </div>
      </div>

      <div className="footer-section container">
        <div className="row">
          <div className="col-md-3">
            <h5>Về TTTT</h5>
            <p>
              Trang thương mại chính thức của HDP - Shopping. Luôn tìm kiếm
              những sản phẩm vì công nghệ.
            </p>
            <div className="social-icons mt-3">
              <a
                href="#"
                aria-label="Facebook"
                target="_blank"
                rel="noopener noreferrer"
              >
                <FaFacebook className="icon" />
              </a>
              <a
                href="#"
                aria-label="Twitter"
                target="_blank"
                rel="noopener noreferrer"
              >
                <FaTwitter className="icon" />
              </a>
              <a
                href="#"
                aria-label="Google Plus"
                target="_blank"
                rel="noopener noreferrer"
              >
                <FaGooglePlusG className="icon" />
              </a>
              <a
                href="#"
                aria-label="YouTube"
                target="_blank"
                rel="noopener noreferrer"
              >
                <FaYoutube className="icon" />
              </a>
            </div>
          </div>
          <div className="information col-md-3">
            <h5>Thông tin liên hệ</h5>
            <p>
              <FontAwesomeIcon icon={faMapMarkerAlt} /> CS1: Thái Thịnh - Đống
              Đa - HN
            </p>
            <p>
              <FontAwesomeIcon icon={faMapMarkerAlt} /> CS2: Thái Hà - Đống Đa -
              HN
            </p>
            <p>
              <FontAwesomeIcon icon={faMapMarkerAlt} /> CS3: đường Vĩnh Viễn -
              Q10 - TP.HCM
            </p>
            <p>
              <FontAwesomeIcon icon={faPhone} /> 0999 999 999 - 091 111 1111
            </p>
            <p>
              <FontAwesomeIcon icon={faEnvelope} /> shoponline@gmail.com
            </p>
          </div>

          <div className="col-md-3">
            <h5>Tài Khoản Ngân Hàng</h5>
            <ul>
              <li type="disc">Tài Khoản Ngân Hàng</li>
              <li type="disc">Tìm kiếm</li>
              <li type="disc">Phương thức thanh toán</li>
            </ul>
          </div>

          <div className="col-md-3">
            <h5>Chính sách</h5>
            <ul>
              <li type="disc">Chính Sách Bảo Mật</li>
              <li type="disc">Qui Định Bảo Hành</li>
              <li type="disc">Chính Sách Đổi Trả</li>
              <li type="disc">Điều khoản sử dụng</li>
              <li type="disc">Chính sách vận chuyển & kiểm hàng</li>
              <li type="disc">
                Phân định trách nhiệm của tổ chức cung ứng dịch vụ logistics
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div className="footer-bottom">
        Copyright © 2024 Bản quyền của Công ty cổ phần
      </div>
    </div>
  );
};

export default Footer;
