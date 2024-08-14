package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.FeedbackDTO;
import com.dev.computer_accessories.dto.response.FeedBackDetailResponse;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@Tag(name = "Feedback Controller")
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Add feedback", description = "Api create new feedback")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.user.email == authentication.name")
    @PostMapping("/")
    public ResponseData<FeedBackDetailResponse> saveFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        feedbackService.saveFeedback(feedbackDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Feedback added successfully");
    }


    @Operation(summary = "Delete feedback", description = "Api delete a feedback")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.user.email == authentication.name")
    @DeleteMapping("/{feedbackId}")
    public ResponseData<FeedBackDetailResponse> deleteFeedback(@PathVariable int feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return new ResponseData<>(HttpStatus.OK.value(), "Feedback deleted successfully");
    }

    @Operation(summary = "Update feedback", description = "Api update a feedback")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.user.email == authentication.name")
    @PutMapping("/{feedbackId}")
    public ResponseData<FeedBackDetailResponse> updateFeedback(@Min(value = 1, message = "feedbackId must be greater than 0") @PathVariable int feedbackId, @RequestBody FeedbackDTO feedbackDTO) {
        feedbackService.updateFeedback(feedbackId, feedbackDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Feedback updated successfully");
    }

    @Operation(summary = "Get list of feedbacks per page", description = "Send a request via this API to get feedback list by pageNo and pageSize")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/list")
    public ResponseData<?> getAllFeedback(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                          @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                          @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all feedback successful", feedbackService.getAllFeedbacksWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get feedback by id", description = "Api get a feedback by id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.user.email == authentication.name")
    @GetMapping("/{feedbackId}")
    public ResponseData<FeedBackDetailResponse> getFeedbackById(@Min(value = 1, message = "feedbackId must be greater than 0") @PathVariable int feedbackId) {
        feedbackService.getFeedback(feedbackId);
        return new ResponseData<>(HttpStatus.OK.value(), "Get feedback by id successfully");
    }
}
