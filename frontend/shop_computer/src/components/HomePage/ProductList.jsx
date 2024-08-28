import React, { useState } from "react";
import "./ProductList.scss";
import { FaShoppingCart } from "react-icons/fa";
import { useDispatch } from "react-redux";
import { addToCart } from "../../redux/slices/cartSlice";
import ReactPaginate from "react-paginate";

const ProductList = ({ products, pageCount, currentPage, onPageChange }) => {
  const dispatch = useDispatch();
  const [showPopup, setShowPopup] = useState(false);
  const [addedProduct, setAddedProduct] = useState(null);

  const addToCartAndShowPopup = (product) => {
    // Add product to cart in Redux
    dispatch(addToCart(product));

    // // Update localStorage
    // const cart = JSON.parse(localStorage.getItem("cart")) || [];
    // const existingProductIndex = cart.findIndex(
    //   (item) => item.id === product.id
    // );

    // if (existingProductIndex > -1) {
    //   // Update quantity if the product already exists
    //   cart[existingProductIndex].quantity += 1;
    // } else {
    //   // Add new product to cart
    //   cart.push({ ...product, quantity: 1 });
    // }

    // localStorage.setItem("cart", JSON.stringify(cart));

    // Show popup
    setAddedProduct(product);
    setShowPopup(true);

    // Hide popup after 5 seconds
    setTimeout(() => {
      setShowPopup(false);
    }, 5000);
  };

  const handlePageClick = (event) => {
    onPageChange(event.selected); // Call function from parent component
  };

  return (
    <div className="product-list">
      <h2>Products</h2>
      <ul className="product-grid">
        {products.map((product) => (
          <li className="li-product" key={product.id}>
            <img src={product.thumbnail} alt={product.name} />
            <div className="product-info">
              <h3 className="product-name">{product.name.toLowerCase()}</h3>
              <div
                className="price-container"
                style={product.discount === 0 ? { marginTop: "27px" } : {}}
              >
                <p className="price d-flex flex-column">
                  ${calculateDiscountedPrice(product.price, product.discount)}
                  {product.discount && (
                    <span className="original-price">
                      ${product.price.toFixed(0)}
                    </span>
                  )}
                </p>
                {product.discount > 0 ? (
                  <div className="discount-badge">{product.discount}%</div>
                ) : (
                  <div />
                )}
              </div>
              <p>{product.description}</p>
              <div
                className="add-to-cart"
                onClick={() => addToCartAndShowPopup(product)}
              >
                <div className="cart-icon">
                  <FaShoppingCart />
                </div>
                <span> Add to Cart</span>
              </div>
            </div>
          </li>
        ))}
        {products.length === 0 && (
          <tr>
            <td colSpan={9}>Not found data</td>
          </tr>
        )}
      </ul>

      {/* Popup for successful add to cart */}
      {showPopup && (
        <div className="cart-popup">
          <div className="cart-popup-content">
            <img src={addedProduct.thumbnail} alt={addedProduct.name} />
            <div>
              <p>{addedProduct.name}</p>
              <p>
                $
                {calculateDiscountedPrice(
                  addedProduct.price,
                  addedProduct.discount
                )}
              </p>
            </div>
          </div>
        </div>
      )}

      {/* Pagination */}
      <div className="d-flex justify-content-center">
        <ReactPaginate
          nextLabel="next >"
          onPageChange={handlePageClick}
          pageRangeDisplayed={1}
          marginPagesDisplayed={2}
          pageCount={pageCount}
          previousLabel="< previous"
          pageClassName="page-item"
          pageLinkClassName="page-link"
          previousClassName="page-item"
          previousLinkClassName="page-link"
          nextClassName="page-item"
          nextLinkClassName="page-link"
          breakLabel="..."
          breakClassName="page-item"
          breakLinkClassName="page-link"
          containerClassName="pagination"
          activeClassName="active"
          renderOnZeroPageCount={null}
          forcePage={currentPage - 1} // 0-indexed
        />
      </div>
    </div>
  );
};

const calculateDiscountedPrice = (price, discount) => {
  if (discount) {
    const discountedPrice = price * (1 - discount / 100);
    return discountedPrice.toFixed(0);
  }
  return price.toFixed(0);
};

export default ProductList;
