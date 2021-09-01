package com.cloudy.todo.todo.domain;

import com.cloudy.todo.todo.dto.request.TodoWithoutChildrenDTO;
import com.cloudy.todo.todo.dto.response.TodoDTO;
import com.cloudy.todo.todo.exception.ChildIsDoingException;
import com.cloudy.todo.todo.exception.ParentIsDoneException;
import com.cloudy.todo.todo.exception.TodoAlreadyDoingException;
import com.cloudy.todo.todo.exception.TodoAlreadyDoneException;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
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

        this.setUpdatedAt(LocalDateTime.now());
        parent.setUpdatedAt(LocalDateTime.now());
    }

    public void addChildren(Todo child) {
        this.children.add(child);
        child.setParent(this);

        this.setUpdatedAt(LocalDateTime.now());
        child.setUpdatedAt(LocalDateTime.now());
    }

    public void finish() {
        if (this.status == TodoStatus.DONE) {
            throw new TodoAlreadyDoneException("The todo is already done");
        }
        for (Todo child : this.children) {
            if (child.getStatus() == TodoStatus.NOT_YET) {
                throw new ChildIsDoingException("Child (id: " + child.getId() + ") is doing");
            }
        }

        this.status = TodoStatus.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    public void doing() {
        if (this.status == TodoStatus.NOT_YET) {
            throw new TodoAlreadyDoingException("The todo is already doing");
        }
        if (this.parent != null && this.parent.getStatus() == TodoStatus.DONE) {
            throw new ParentIsDoneException("Parent (id: " + this.parent.getId() + ") is done");
        }

        this.status = TodoStatus.NOT_YET;
        this.updatedAt = LocalDateTime.now();
    }

    public TodoDTO toDTO() {
        if (this.id == null) {
            throw new IllegalArgumentException("Todo id should not be null");
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
            throw new IllegalArgumentException("Todo id should not be null");
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
