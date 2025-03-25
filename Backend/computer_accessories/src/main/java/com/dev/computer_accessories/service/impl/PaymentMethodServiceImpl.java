package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.PaymentMethodDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.PaymentMethod;
import com.dev.computer_accessories.repository.PaymentMethodRepository;
import com.dev.computer_accessories.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
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
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void save(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .name(paymentMethodDTO.getName())
                .type(paymentMethodDTO.getType())
                .description(paymentMethodDTO.getDescription())
                .build();

        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void update(int paymentMethodId, PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = getPaymentMethodById(paymentMethodId);

        paymentMethod.setName(paymentMethodDTO.getName());
        paymentMethod.setType(paymentMethodDTO.getType());
        paymentMethod.setDescription(paymentMethodDTO.getDescription());

        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void delete(int paymentMethodId) {
        paymentMethodRepository.deleteById(paymentMethodId);
    }

    @Override
    public PaymentMethodDTO getPaymentMethodDTOById(int paymentMethodId) {
        PaymentMethod paymentMethod = getPaymentMethodById(paymentMethodId);

        return PaymentMethodDTO.builder()
                .name(paymentMethod.getName())
                .type(paymentMethod.getType())
                .description(paymentMethod.getDescription())
                .build();
    }

    @Override
    public PageResponse<?> getAllPaymentMethodsWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            //name:asc||desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add((new Sort.Order(Sort.Direction.ASC, matcher.group(1))));
                } else {
                    sorts.add((new Sort.Order(Sort.Direction.DESC, matcher.group(1))));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findAll(pageable);

        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethods.stream().map((paymentMethod -> PaymentMethodDTO.builder()
                        .id(paymentMethod.getId())
                        .name(paymentMethod.getName())
                        .type(paymentMethod.getType())
                        .description(paymentMethod.getDescription())
                        .build()))
                .toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(paymentMethods.getTotalPages())
                .items(paymentMethodDTOList)
                .build();
    }

    private PaymentMethod getPaymentMethodById(int paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new ResourceNotFoundException("Not found payment method"));
    }
}
