package com.dev.computer_accessories.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class PageResponse<T> {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private T items;
}
