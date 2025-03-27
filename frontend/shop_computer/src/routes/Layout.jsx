import { Route, Routes } from "react-router-dom";
import Home from "../components/HomePage/HomePage";
import Admin from "../components/Admin/Admin";
import User from "../components/User/User";
import Dashboard from "../components/Admin/Content/Dashboard";
import ManageUser from "../components/Admin/Content/User/ManageUser";
import ManageProduct from "../components/Admin/Content/Product/ManageProduct";
import Login from "../components/Auth/Login";
import ForgotPassword from "../components/Auth/ForgotPassword";
import App from "../views/App";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Register from "../components/Auth/Register";
import OAuth2RedirectHandler from "../components/Auth/OAuth2RedirectHandler";
import ViewCart from "../components/ViewCart/ViewCart";
import ProductDetail from "../components/HomePage/ProductDetail";
import CheckoutPage from "../components/ViewCart/CheckoutPage";
import ViewCheckout from "../components/HomePage/ViewCheckout";
import ManageCategory from "../components/Admin/Content/Category/ManageCategory";

const Layout = () => {
  return (
    <>
      <Routes>
        {/* 👈 Renders at /app/ */}
        <Route path="/" element={<App />}>
          <Route index element={<Home />} />
          <Route path="/user" element={<User />} />
          <Route path="/view-cart" element={<ViewCart />} />
          <Route path="/product/:productId" element={<ProductDetail />} />
          <Route path="/view-checkout/:id" element={<ViewCheckout />} />
        </Route>

        <Route path="/admin" element={<Admin />}>
          <Route index element={<Dashboard />} />
          <Route path="manage-users" element={<ManageUser />} />
          <Route path="manage-products" element={<ManageProduct />} />
          <Route path="manage-categories" element={<ManageCategory />} />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/register" element={<Register />} />
        <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
        <Route path="/checkout/:productId" element={<CheckoutPage />} />
      </Routes>

      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </>
  );
};

export default Layout;
