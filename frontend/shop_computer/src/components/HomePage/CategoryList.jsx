// CategoryList.js
import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";
import "./CategoryList.scss"; // Import file SCSS

const CategoryList = ({ categories, onSelectCategory }) => {
  return (
    <div className="category-list">
      <div className="header">
        <FontAwesomeIcon icon={faBars} />
        <h2>Categories</h2>
      </div>
      <ul>
        {categories.map((category) => (
          <li key={category.id}>
            <button onClick={() => onSelectCategory(category.id)}>
              {category.name}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CategoryList;
