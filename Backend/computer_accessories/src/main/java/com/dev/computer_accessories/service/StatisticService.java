package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.response.StatisticProductResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatisticService {
    List<StatisticProductResponse> getStatisticProducts(int pageNo, int pageSize, LocalDateTime from, LocalDateTime to);
}
