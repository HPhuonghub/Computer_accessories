// HomePage.js
import React, { useState, useEffect } from "react";
import CategoryList from "./CategoryList";
import ProductList from "./ProductList";
import { Container, Row, Col } from "react-bootstrap";
import { listProduct, getAllProduct } from "../../redux/slices/ProductSlice";
import { listCategory, getAllCategory } from "../../redux/slices/CategorySlice";
import { useDispatch, useSelector } from "react-redux";

const HomePage = () => {
  const dispatch = useDispatch();

  // Dùng useSelector để lấy danh sách sản phẩm từ Redux store
  let products = useSelector(listProduct);
  let categories = useSelector(listCategory);

  console.log(products);
  console.log(categories);

  // Khởi tạo selectedCategoryId ban đầu là id của danh mục đầu tiên
  const [selectedCategoryId, setSelectedCategoryId] = useState(1);

  // Hàm lấy danh sách sản phẩm từ Redux
  const fetchProducts = () => {
    dispatch(getAllProduct());
  };

  const fetchCategory = () => {
    dispatch(getAllCategory());
  };

  // Effect để gọi fetchProducts khi component được render lần đầu
  useEffect(() => {
    fetchProducts();
    fetchCategory();
  }, []);

  // Lọc sản phẩm theo danh mục được chọn
  const filteredProducts = products
    ? products.data.items.filter(
        (product) => product.category.id === selectedCategoryId
      )
    : [];

  // Hàm xử lý khi chọn danh mục mới
  const handleSelectCategory = (categoryId) => {
    setSelectedCategoryId(categoryId);
  };

  return (
    <div className="home">
      <Container>
        <Row>
          <Col xs={9} md={3}>
            <CategoryList
              categories={categories ? categories.data : []}
              onSelectCategory={handleSelectCategory}
            />
          </Col>
          <Col xs={10} md={8}>
            <ProductList products={filteredProducts} />
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default HomePage;
