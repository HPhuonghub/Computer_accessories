package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.FeedbackDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.FeedBackDetailResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Feedback;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.FeedbackRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.FeedbackService;
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
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
   private final FeedbackRepository feedbackRepository;
   private final UserRepository userRepository;
   private final ProductRepository productRepository;

    @Override
    public void saveFeedback(FeedbackDTO feedbackDTO) {
        User user = getUserByEmail(feedbackDTO.getUser().getEmail());
        Product product = getProductByName(feedbackDTO.getProduct().getName());

        Feedback feedback = Feedback.builder()
                .rating(feedbackDTO.getRating())
                .comment(feedbackDTO.getComment())
                .user(user)
                .product(product)
                .build();

        feedbackRepository.save(feedback);
    }

    @Override
    public void updateFeedback(int id, FeedbackDTO feedbackDTO) {
        Feedback feedback = getFeedBackById(id);
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());

        feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(int id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public PageResponse<?> getAllFeedbacksWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize,Sort.by(sorts));

        Page<Feedback> feedbacks = feedbackRepository.findAll(pageable);

        List<FeedBackDetailResponse> responses = feedbacks.stream().map(feedback -> FeedBackDetailResponse.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .user(feedback.getUser())
                .product(feedback.getProduct())
                .build()
        ).toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(feedbacks.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public FeedBackDetailResponse getFeedback(int id) {
        Feedback feedback = getFeedBackById(id);
        return FeedBackDetailResponse.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .user(feedback.getUser())
                .product(feedback.getProduct())
                .build();
    }

    private Feedback getFeedBackById(int id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("FeedBack not found"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Product getProductByName(String name) {
        return  productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
