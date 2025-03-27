import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";
import "./CategoryList.scss"; // Import file SCSS

const CategoryList = ({ categories, onSelectCategory }) => {
  const [showMore, setShowMore] = useState(false);
  const [activeCategory, setActiveCategory] = useState(null);

  const visibleCategories = showMore ? categories : categories.slice(0, 5);

  const handleToggle = () => {
    setShowMore((prev) => !prev);
  };

  const handleCategoryClick = (category) => {
    setActiveCategory(category.name);
    onSelectCategory(category.name);
  };

  return (
    <div className="category-list">
      <div className="header">
        <FontAwesomeIcon icon={faBars} />
        <h2>Categories</h2>
      </div>
      <ul className={showMore ? "show" : ""}>
        {visibleCategories.map((category) => (
          <li key={category.id}>
            <button
              className={activeCategory === category.name ? "active" : ""}
              onClick={() => handleCategoryClick(category)}
            >
              {category.name}
            </button>
          </li>
        ))}
      </ul>
      {categories.length > 5 && (
        <button className="toggle-button" onClick={handleToggle}>
          {showMore ? "- Ẩn bớt" : "+ Xem thêm"}
        </button>
      )}
    </div>
  );
};

export default CategoryList;
