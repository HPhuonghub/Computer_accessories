package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.FeedbackDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.FeedBackDetailResponse;
import org.springframework.stereotype.Service;

@Service
public interface FeedbackService {
    void saveFeedback(FeedbackDTO feedBackDTO);

    void updateFeedback(int id, FeedbackDTO feedBackDTO);

    void deleteFeedback(int id);

    PageResponse<?> getAllFeedbacksWithSortBy(int pageNo, int pageSize, String sortBy);

    FeedBackDetailResponse getFeedback(int id);
}
