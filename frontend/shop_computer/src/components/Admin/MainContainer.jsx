import React from "react";
import "./MainContainer.scss"; // Assuming you have a CSS file for styling

const itemOrders = [
  {
    productName: 'Apple MacBook Pro 17"',
    productNumber: "98631",
    payment: "Due",
    status: "Pending",
    statusClass: "success",
  },
  {
    productName: 'Apple MacBook Pro 17"',
    productNumber: "98631",
    payment: "Due",
    status: "Pending",
    statusClass: "danger",
  },
  {
    productName: 'Apple MacBook Pro 17"',
    productNumber: "98631",
    payment: "Due",
    status: "Pending",
    statusClass: "warning",
  },
  {
    productName: 'Apple MacBook Pro 17"',
    productNumber: "98631",
    payment: "Due",
    status: "Pending",
    statusClass: "primary",
  },
];

const MainContainer = () => (
  <main className="main-container">
    <h1>Dashboard</h1>
    <div className="date">
      <input type="date" />
    </div>

    <div className="insights">
      <div className="sales">
        <span className="material-icons-sharp">analytics</span>
        <div className="middle">
          <div className="left">
            <h3>Total sales</h3>
            <h1>$25,024</h1>
          </div>
          <div className="progress-insights">
            <svg>
              <circle cx="38" cy="38" r="36" />
            </svg>
            <div className="number">
              <p>81%</p>
            </div>
          </div>
        </div>
        <small className="text-muted">Last 24 Hours</small>
      </div>

      <div className="expenses">
        <span className="material-icons-sharp">bar_chart</span>
        <div className="middle">
          <div className="left">
            <h3>Total sales</h3>
            <h1>$14160</h1>
          </div>
          <div className="progress-insights">
            <svg>
              <circle cx="38" cy="38" r="36"></circle>
            </svg>
            <div className="number">
              <p>62%</p>
            </div>
          </div>
        </div>
        <small className="text-muted">Last 24 Hours</small>
      </div>

      <div className="income">
        <span className="material-icons-sharp">stacked_line_chart</span>
        <div className="middle">
          <div className="left">
            <h3>Total sales</h3>
            <h1>$12,203</h1>
          </div>
          <div className="progress-insights">
            <svg>
              <circle cx="38" cy="38" r="36"></circle>
            </svg>
            <div className="number">
              <p>44%</p>
            </div>
          </div>
        </div>
        <small className="text-muted">Last 24 Hours</small>
      </div>
    </div>

    <div className="recent-orders">
      <h2>Recent Orders</h2>
      <table className="table-order">
        <thead>
          <tr>
            <th>Product Name</th>
            <th>Product Number</th>
            <th>Payment</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {itemOrders.map((item, index) => (
            <tr key={index}>
              <td>{item.productName}</td>
              <td>{item.productNumber}</td>
              <td>{item.payment}</td>
              <td className={item.statusClass}>{item.status}</td>
              <td className="primary">Details</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div>
        <a href="#" className="btn">
          Show All
        </a>
      </div>
    </div>
  </main>
);

export default MainContainer;
