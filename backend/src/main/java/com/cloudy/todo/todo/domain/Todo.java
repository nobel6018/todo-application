package com.cloudy.todo.todo.domain;

import com.cloudy.todo.todo.dto.TodoDTO;
import com.cloudy.todo.todo.dto.TodoWithoutChildrenDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_todo_id")
    private Todo parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Todo> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Todo(String content) {
        this.content = content;
        this.status = TodoStatus.NOT_YET;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    public void setMyParent(Todo parent) {
        this.parent = parent;
        parent.getChildren().add(this);
    }

    public void addChildren(Todo child) {
        this.children.add(child);
        child.setParent(this);
    }

    public TodoDTO toDTO() {
        if (this.id == null) {
            // Todo: business exception
            throw new IllegalArgumentException("Id should not be null");
        }

        return new TodoDTO(
            this.id,
            this.content,
            this.children.stream().map(Todo::toBasicDTO).collect(Collectors.toList()),
            this.status,
            this.createdAt,
            this.updatedAt
        );
    }

    public TodoWithoutChildrenDTO toBasicDTO() {
        if (this.id == null) {
            // Todo: business exception
            throw new IllegalArgumentException("Id should not be null");
        }

        return new TodoWithoutChildrenDTO(
            this.id,
            this.content,
            this.status,
            this.createdAt,
            this.updatedAt
        );
    }
}
