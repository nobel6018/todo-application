package com.cloudy.todo.todo.repository;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends PagingAndSortingRepository<Todo, Long> {
    List<Todo> findAllByOrderByIdDesc();

    List<Todo> findAllByContentContainingOrderByIdDesc(String content);

    List<Todo> findAllByStatusOrderByIdDesc(TodoStatus status);

    List<Todo> findAllByCreatedAtBetweenOrderByIdDesc(LocalDateTime from, LocalDateTime to);

    Page<Todo> findAllByContentContaining(String content, Pageable pageable);

    Page<Todo> findAllByStatus(TodoStatus status, Pageable pageable);

    Page<Todo> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
