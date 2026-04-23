package com.betolara1.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {
    private PaginationUtils(){} // Impede instanciação

    public static Pageable createPageable(int page, int size, String sortBy, String direction){
        int safeSize = Math.min(size, 50);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        return PageRequest.of(page, safeSize, sort);
    }
}
