package com.cloudy.todo.todo.repository;

import com.cloudy.todo.todo.domain.Todo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends PagingAndSortingRepository<Todo, Long> {
    List<Todo> findAllByOrderByIdDesc();

    List<Todo> findAllByContent(String content);
}
