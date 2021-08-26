package com.cloudy.todo.todo.repository;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
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
}
