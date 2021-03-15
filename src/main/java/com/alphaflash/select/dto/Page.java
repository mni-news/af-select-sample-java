package com.alphaflash.select.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * {
 *     "number": 1,
 *     "totalElements": 520,
 *     "totalPages": 53,
 *     "content": [  ]
 * }
 * @param <T>
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {

    private long number;
    private long totalElements;
    private long totalPages;

    private List<T> content;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
