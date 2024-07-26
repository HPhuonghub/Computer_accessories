import { NavLink, useNavigate } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  return (
    <>
      <div>dash board</div>
      <button className="primary" onClick={() => handleGoHome()}>
        Go home
      </button>
    </>
  );
};

export default Dashboard;
