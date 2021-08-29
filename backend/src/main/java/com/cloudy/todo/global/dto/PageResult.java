package com.cloudy.todo.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResult<T> {

    private List<T> data;
    private int size;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
}