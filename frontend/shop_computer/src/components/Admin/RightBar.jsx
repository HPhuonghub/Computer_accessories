import React from "react";
import imageAdmin from "../../assets/images/logo.png";
import logo from "../../assets/images/icon-logo.png";
import "./RightBar.scss"; // Assuming you have a CSS file for styling

const recentUpdates = [
  {
    image: logo,
    name: "Daniel",
    note: " received his order of ",
    product: `Apple MacBook Pro 17`,
    time: "2 Days Ago",
  },
  {
    image: logo,
    name: "Daniel",
    note: " received his order of ",
    product: `Apple MacBook Pro 17`,
    time: "2 Days Ago",
  },
  {
    image: logo,
    name: "Daniel",
    note: " received his order of ",
    product: `Apple MacBook Pro 17`,
    time: "2 Days Ago",
  },
];

const RightBar = ({ setShowSidebar, handleThemeToggle }) => (
  <div className="right">
    <div className="top">
      <button id="menu-btn" onClick={() => setShowSidebar(true)}>
        <span className="material-icons-sharp">menu</span>
      </button>
      <div className="theme-toggler" onClick={handleThemeToggle}>
        <span className="material-icons-sharp active">light_mode</span>
        <span className="material-icons-sharp">dark_mode</span>
      </div>
      <div className="profile">
        <div className="info">
          <p>
            Hey,
            <b>Daniel</b>
          </p>
          <small className="text-muted">Admin</small>
        </div>
        <div className="profile-photo">
          <img src={imageAdmin} alt="" />
        </div>
      </div>
    </div>

    <div className="recent-updates">
      <h2>Recent Updates</h2>
      <div className="updates">
        {recentUpdates.map((item, idx) => (
          <div className="update" key={idx}>
            <div className="profile-photo">
              <img src={item.image} alt="" />
            </div>
            <div className="message">
              <p>
                <b>{item.name}</b>
                {item.note}
                {item.product}
              </p>
              <small className="text-muted">{item.time}</small>
            </div>
          </div>
        ))}
      </div>
    </div>
    <div className="sales-analytics">
      <h2>Sales Analytics</h2>
      <div className="item online">
        <div className="icon">
          <span className="material-icons-sharp">shopping_cart</span>
        </div>
        <div className="right">
          <div className="info">
            <h3>ONLINE ORDERS</h3>
            <small className="text-muted">Last 24 Hours</small>
          </div>
          <h5 className="success">+39%</h5>
          <h3>3849</h3>
        </div>
      </div>
      <div className="item offline">
        <div className="icon">
          <span className="material-icons-sharp">local_mall</span>
        </div>
        <div className="right">
          <div className="info">
            <h3>OFFLINE ORDERS</h3>
            <small className="text-muted">Last 24 Hours</small>
          </div>
          <h5 className="success">-17%</h5>
          <h3>1100</h3>
        </div>
      </div>
      <div className="item customers">
        <div className="icon">
          <span className="material-icons-sharp">people</span>
        </div>
        <div className="right">
          <div className="info">
            <h3>NEW CUSTOMERS</h3>
            <small className="text-muted">Last 24 Hours</small>
          </div>
          <h5 className="danger">+25%</h5>
          <h3>803</h3>
        </div>
      </div>
      <div className="item add-product">
        <div>
          <span className="material-icons-sharp">add</span>
          <h3>Add Product</h3>
        </div>
      </div>
    </div>
  </div>
);

export default RightBar;
