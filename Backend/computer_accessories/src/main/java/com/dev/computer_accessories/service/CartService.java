package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.CartDTO;
import com.dev.computer_accessories.dto.response.CartDetailResponse;
import com.dev.computer_accessories.dto.response.PageResponse;

import java.util.List;

public interface CartService {
        void saveCart(CartDTO categoryDTO);

        void updateCart(int id, CartDTO categoryDTO);

        void deleteCart(int id);

        PageResponse<?> getAllCartsWithSortBy(int pageNo, int pageSize, String sortBy);

        CartDetailResponse getCart(int id);

        List<CartDetailResponse> getCarts();
}
