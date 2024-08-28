import React, { useState, useEffect } from "react";
import CategoryList from "./CategoryList";
import ProductList from "./ProductList";
import { Container, Row, Col } from "react-bootstrap";
import {
  listProductSearch,
  wordSearch,
  getAllProductSearch,
} from "../../redux/slices/ProductSlice";
import { listCategory, getAllCategory } from "../../redux/slices/CategorySlice";
import { useDispatch, useSelector } from "react-redux";

const HomePage = () => {
  const LIMIT_PRODUCT = 8;
  const dispatch = useDispatch();
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [selectedCategoryName, setSelectedCategoryName] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const productSearch = useSelector(listProductSearch);
  const keySearch = useSelector(wordSearch);
  const [search, setSearch] = useState();
  const categories = useSelector(listCategory);
  const [pageCount, setPageCount] = useState(0);

  useEffect(() => {
    // Chỉ gọi khi keySearch thay đổi
    if (keySearch !== search) {
      setSearch(keySearch);
      setCurrentPage(1);
    }
  }, [keySearch]);

  useEffect(() => {
    dispatch(getAllCategory());
    dispatch(getAllProductSearch(currentPage, LIMIT_PRODUCT, search));
  }, [dispatch]);

  useEffect(() => {
    // Chỉ khi selectedCategoryName thay đổi hoặc currentPage thay đổi
    dispatch(getAllProductSearch(currentPage, LIMIT_PRODUCT, search));
  }, [selectedCategoryName, currentPage, search, dispatch]);

  useEffect(() => {
    if (productSearch?.data?.items) {
      const filtered = productSearch.data.items;
      setPageCount(productSearch.data.totalPage);
      setFilteredProducts(filtered);
    }
  }, [productSearch]);

  const handleSelectCategory = (categoryName) => {
    setSelectedCategoryName(categoryName);
    setCurrentPage(1);
    setSearch(categoryName); // Giả định categoryName là giá trị tìm kiếm
  };

  const handlePageChange = (selectedPage) => {
    setCurrentPage(selectedPage + 1);
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
            <ProductList
              products={filteredProducts}
              pageCount={pageCount}
              currentPage={currentPage}
              onPageChange={handlePageChange}
            />
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default HomePage;
