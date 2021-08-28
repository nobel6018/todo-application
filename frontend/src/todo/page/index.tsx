import React, { useEffect, useState } from "react";
import api from "../../api";
import { Todo } from "../domain/Todo";
import { AddButton } from "../element/AddButton";
import { DeleteButton } from "../element/DeleteButton";
import { TodoStatus } from "../domain/TodoStatus";

function TodoMain() {
    const [todos, setTodos] = useState<Todo[]>([]);
    const [doneTodoIds, setDoneTodoIds] = useState<Array<number>>([]);

    function loadAllTodos() {
        api.getTodos().then((res) => {
            console.log(res.data);
            setTodos(
                res.data.data.map((todo: Todo) => ({
                    id: todo.id,
                    content: todo.content,
                    children: todo.children,
                    status: todo.status,
                    createdAt: todo.createdAt,
                    updatedAt: todo.updatedAt && new Date(todo.updatedAt),
                }))
            );
            setDoneTodoIds(
                res.data.data.filter((todo: Todo) => todo.status === TodoStatus.DONE).map((todo: Todo) => todo.id)
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
                            <li key={todo.id} className="flex items-center justify-between mt-3 ">
                                <div className="flex items-center">
                                    <input
                                        type="checkbox"
                                        name=""
                                        checked={doneTodoIds.includes(todo.id)}
                                        onChange={() => updateTodoStatus(todo.id, inverseStatus(todo.status))}
                                    />
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

    function updateTodoStatus(id: number, status: TodoStatus) {
        api.updateTodoStatus(id, { status }).then(() => {
            setTodos((prevState) => {
                return prevState.map((value) => {
                    if (value.id === id) {
                        value.status = status;
                    }
                    return value;
                });
            });
            setDoneTodoIds((prevState) => {
                if (status === TodoStatus.DONE) {
                    return [...prevState, id];
                }
                return prevState.filter((value) => value !== id);
            });
        });
    }

    function inverseStatus(status: TodoStatus) {
        return status === TodoStatus.DONE ? TodoStatus.NOT_YET : TodoStatus.DONE;
    }

    function deleteTodo(id: number) {
        api.deleteTodo(id).then(() => {
            setTodos((prevState) => {
                return prevState.filter((value) => value.id !== id);
            });
        });
    }
}

export default TodoMain;
