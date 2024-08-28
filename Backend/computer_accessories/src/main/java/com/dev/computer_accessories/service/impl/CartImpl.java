package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.CartDTO;
import com.dev.computer_accessories.dto.response.CartDetailResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.model.Cart;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.CartRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public void saveCart(CartDTO cartDTO) {

        User user = userRepository.findByEmail(cartDTO.getUser().getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Product> products = cartDTO.getProducts().stream()
                .map(product -> productRepository.findById(product.getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                .toList();

        Cart cart = Cart.builder()
                .user(user)
                .products(products)
                .build();

        cartRepository.save(cart);

        log.info("Cart saved successfully");
    }

    @Override
    public void updateCart(int id, CartDTO cartDTO) {
        Cart cart = getCartById(id);

        cart.setProducts(cartDTO.getProducts());
        cart.setUser(cartDTO.getUser());
        cartRepository.save(cart);

        log.info("Cart updated successfully");
    }

    @Override
    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }

    @Override
    public PageResponse<?> getAllCartsWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, "name"));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, "name"));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<Cart> carts = cartRepository.findAll(pageable);

        List<CartDetailResponse> res = carts.stream().map(cart -> CartDetailResponse.builder()
                .products(cart.getProducts())
                .user(cart.getUser())
                .build()).toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(carts.getTotalPages())
                .items(res)
                .build();
    }

    @Override
    public CartDetailResponse getCart(int id) {
        Cart cart = getCartById(id);

        return CartDetailResponse.builder()
                .products(cart.getProducts())
                .user(cart.getUser())
                .build();
    }


    @Override
    public List<CartDetailResponse> getCarts() {
        List<Cart> cart = cartRepository.findAll();

        List<CartDetailResponse> responses = cart.stream().map(cart1 -> CartDetailResponse.builder()
                .products(cart1.getProducts())
                .user(cart1.getUser())
                .build()).toList();
        return responses;
    }

    private Cart getCartById(int id) {
        return cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }

    private boolean existCart(int id) {
        return cartRepository.findById(id).isPresent();
    }
}
