import SideBar from "./SideBar";
import MainContainer from "./MainContainer";
import RightBar from "./RightBar";
import "./Admin.scss";
import { useState, useEffect } from "react";

const Admin = () => {
  const [showSidebar, setShowSidebar] = useState(window.innerWidth > 768);
  const [activeMenu, setActiveMenu] = useState("dashboard");

  // Tự động hiện sidebar khi kéo rộng màn hình
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 768) setShowSidebar(true);
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // Thay đổi chế độ sáng tối
  const handleThemeToggle = () => {
    const themeToggler = document.querySelector(".theme-toggler");
    themeToggler.querySelector("span:nth-child(1)").classList.toggle("active");
    themeToggler.querySelector("span:nth-child(2)").classList.toggle("active");
    document.body.classList.toggle("dark-theme-variables");
  };

  return (
    <div className="admin-container">
      <SideBar
        showSidebar={showSidebar}
        setShowSidebar={setShowSidebar}
        activeMenu={activeMenu}
        setActiveMenu={setActiveMenu}
      />
      <MainContainer />
      <RightBar
        setShowSidebar={setShowSidebar}
        handleThemeToggle={handleThemeToggle}
      />
    </div>
  );
};

export default Admin;
