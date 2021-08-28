import React, { useEffect, useState } from "react";
import api from "../../api";
import { Todo } from "../domain/Todo";

function TodoMain() {
    const [todos, setTodos] = useState<Todo[]>([]);

    function loadAllTodos() {
        api.getTodos().then((res) => {
            console.log(res.data);
            setTodos(
                res.data.data.map((data: Todo) => ({
                    id: data.id,
                    content: data.content,
                    children: data.children,
                    status: data.status,
                    createdAt: data.createdAt,
                    updatedAt: data.updatedAt && new Date(data.updatedAt),
                }))
            );
        });
    }
    useEffect(() => {
        loadAllTodos();
    }, []);

    return (
        <div>
            {todos.map((todo) => (
                <div key={todo.id} style={{border: "1px solid black"}}>
                    #{todo.id} | {todo.content} | @{todo.createdAt} |
                    <span onClick={() => {
                        api.deleteTodo(todo.id).then(() => loadAllTodos());
                    }}>[X]</span>
                </div>
            ))}
        </div>
    );
}

export default TodoMain;
