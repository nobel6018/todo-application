package com.cloudy.todo.todo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id
    @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_todo_id")
    private Todo parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Todo> children;

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
