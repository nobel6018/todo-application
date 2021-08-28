import React, { useEffect, useState } from "react";
import api from "../../api";
import { Todo } from "../domain/Todo";
import { AddButton } from "../element/AddButton";
import { DeleteButton } from "../element/DeleteButton";
import { TodoStatus } from "../domain/TodoStatus";
import { TodoDisplay } from "../element/TodoDisplay";
import { FindConditionType } from "../dto/FindConditionType";
import { AxiosResponse } from "axios";
import { FindButton } from "../element/FindButton";

function TodoMain() {
    const [todos, setTodos] = useState<Array<Todo>>([]);
    const [doneTodoIds, setDoneTodoIds] = useState<Array<number>>([]);
    const [content, setContent] = useState<string>();

    const [byContent, setByContent] = useState<string>();
    const [byStatus, setByStatus] = useState<TodoStatus>();
    const [byCreatedDate, setByCreatedDate] = useState<Date>();

    useEffect(() => {
        getTodos();
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
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />
                    <AddButton onClick={() => createTodo(content as string)} />
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
                                        onChange={() => updateTodoStatus(todo.id, getInverseStatus(todo.status))}
                                    />
                                    <TodoDisplay
                                        id={todo.id}
                                        content={todo.content}
                                        status={todo.status}
                                        childrenIds={todo.children.map((child) => child.id)}
                                        createdAt={todo.createdAt}
                                        updatedAt={todo.updatedAt}
                                    />
                                    <DeleteButton onClick={() => deleteTodo(todo.id)} />
                                </div>
                            </li>
                        ))}
                    </ul>

                    {/* Filter List */}
                    <hr className="mt-6 border-t-2 border-gray-300" />
                    <div className="mt-4">
                        <span
                            className="text-blue-600 cursor-pointer"
                            onClick={() => GetTodosByStatus(TodoStatus.DONE)}
                        >
                            완료
                        </span>{" "}
                        <span
                            className="text-blue-600 cursor-pointer"
                            onClick={() => GetTodosByStatus(TodoStatus.NOT_YET)}
                        >
                            진행중
                        </span>
                    </div>

                    <div className="flex">
                        <input
                            className="border border-gray-800 focus:border-blue-500 rounded w-full py-2 px-3 mr-4 text-black"
                            placeholder="내용으로 찾기"
                            value={byContent}
                            onChange={(e) => setByContent(e.target.value)}
                        />
                        <FindButton onClick={() => getTodoByByContent(byContent as string)} />
                    </div>

                    <div className="text-right text-red-600">
                        <span className="cursor-pointer" onClick={clearFindCondition}>
                            초기화
                        </span>
                    </div>
                </div>
            </div>
        </div>
    );

    function getTodoByByContent(content: string) {
        setByContent(content);
        getTodos(FindConditionType.CONTENT, content);
    }

    function GetTodosByStatus(status: TodoStatus) {
        setByStatus(status);
        getTodos(FindConditionType.STATUS, status);
    }

    function getTodosByCreatedDate(createdDate: Date) {
        setByCreatedDate(createdDate);
        getTodos(FindConditionType.CREATED_DATE, createdDate);
    }

    function clearFindCondition() {
        setByContent(undefined);
        setByStatus(undefined);
        setByCreatedDate(undefined);
        getTodos();
    }

    function getTodos(condition?: FindConditionType, by?: string | TodoStatus | Date) {
        switch (condition) {
            case FindConditionType.STATUS:
                // @ts-ignore
                api.getTodos(undefined, by, undefined).then((res) => {
                    getTodoCommonLogic(res);
                });
                break;
            case FindConditionType.CONTENT:
                // @ts-ignore
                api.getTodos(by, undefined, undefined).then((res) => {
                    getTodoCommonLogic(res);
                });
                break;
            case FindConditionType.CREATED_DATE:
                // @ts-ignore
                api.getTodos(undefined, undefined, by).then((res) => {
                    getTodoCommonLogic(res);
                });
                break;
            default:
                api.getTodos().then((res) => {
                    getTodoCommonLogic(res);
                });
        }
    }

    function getTodoCommonLogic(res: AxiosResponse<any>) {
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
    }

    function createTodo(content: string) {
        if (content === "") {
            alert("Content should not be empty");
            return;
        }
        api.createTodo({ content }).then(() => {
            setContent("");
            getTodos();
        });
    }

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

    function getInverseStatus(status: TodoStatus) {
        return status === TodoStatus.DONE ? TodoStatus.NOT_YET : TodoStatus.DONE;
    }

    function setPrecedence(followerId: number, precedenceId: number) {
        api.setChildren(followerId, { childrenIds: [precedenceId] }).then(() => {
            getTodos();
            // setTodos((prevState) => {
            //     return prevState.map((todo) => {
            //         if (todo.id === followerId) {
            //             todo.children
            //         }
            //     })
            // })
        });
    }

    function deleteTodo(id: number) {
        api.deleteTodo(id).then(() => {
            todos.forEach((todo) => {
                if (todo.children.filter((child) => child.id === id).length > 0) {
                    getTodos();
                    return;
                }
            });

            setTodos((prevState) => {
                return prevState.filter((value) => value.id !== id);
            });
        });
    }
}

export default TodoMain;
