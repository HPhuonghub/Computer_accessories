package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.response.StatisticProductResponse;
import com.dev.computer_accessories.repository.OrderDetailsRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<StatisticProductResponse> getStatisticProducts(int pageNo, int pageSize, LocalDateTime startTime, LocalDateTime endTime) {
        int p = pageNo > 0 ? pageNo - 1 : 0;
        Pageable pageable = PageRequest.of(p, pageSize);

        Page<Object[]> list = orderDetailsRepository.getStatisticProduct(startTime, endTime, pageable);

        return list.stream().map(obj -> new StatisticProductResponse(
                ((Number) obj[0]).longValue(),   // id
                (String) obj[1],                 // name
                ((Number) obj[2]).intValue(),     // quantity
                ((Number) obj[3]).doubleValue()   // totalPrice
        )).toList();
    }
}
