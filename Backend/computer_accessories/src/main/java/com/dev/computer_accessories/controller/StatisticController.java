package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.service.StatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/statistic")
@Tag(name = "Statistic Controller")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/product")
    public ResponseData<?> getStatisticProduct(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                               @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                               @RequestParam(required = false) LocalDateTime startTime,
                                               @RequestParam(required = false) LocalDateTime endTime)
            {

        return new ResponseData<>(HttpStatus.OK.value(), "Get statistic product successful", statisticService.getStatisticProducts(pageNo, pageSize, startTime, endTime));
    }

}
