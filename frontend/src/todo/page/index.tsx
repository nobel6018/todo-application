import React, { useEffect, useState } from "react";
import api from "../../api";
import { Todo } from "../domain/Todo";
import { AddButton } from "../element/AddButton";
import { DeleteButton } from "../element/DeleteButton";
import { TodoStatus } from "../domain/TodoStatus";

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
        // bg
        <div className="bg-gray-200 text-gray-800 flex items-center justify-center h-screen p-3.5">
            {/*wrapper*/}
            <div className="container px-3 max-w-md mx-auto">
                {/*title*/}
                <div className="flex">
                    <input
                        className="border border-gray-800 focus:border-blue-500 rounded w-full py-2 px-3 mr-4 text-black"
                        placeholder="Add Todo"
                    />
                    <AddButton />
                </div>

                {/* Todo List */}
                <div>
                    <ul className="mt-4">
                        {todos.map((todo) => (
                            <li className="flex items-center justify-between mt-3 ">
                                <div className="flex items-center">
                                    <input type="checkbox" name="" checked={todo.status === TodoStatus.DONE} />
                                    {todo.status === TodoStatus.DONE ? (
                                        <div className="capitalize ml-3 font-semibold line-through">{todo.content}</div>
                                    ) : (
                                        <div className="capitalize ml-3 font-semibold">{todo.content}</div>
                                    )}
                                    <DeleteButton onClick={() => deleteTodo(todo.id)} />
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );

    function deleteTodo(id: number) {
        api.deleteTodo(id).then(() => loadAllTodos());
    }
}

export default TodoMain;
