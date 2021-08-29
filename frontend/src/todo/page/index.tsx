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

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { toYYYYMMDD } from "../../global/util";
import { CheckButton } from "../element/CheckButton";
import { stripIndents } from "common-tags";
import { Pagination } from "../element/Pagination";

function TodoMain() {
    const [todos, setTodos] = useState<Array<Todo>>([]);
    const [doneTodoIds, setDoneTodoIds] = useState<Array<number>>([]);

    const [page, setPage] = useState<number>(0); // dev friendly (start from 0)
    const [totalPages, setTotalPages] = useState<number>(1);

    const [content, setContent] = useState<string>();

    const [searchCondition, setSearchCondition] = useState<FindConditionType>();
    const [byContent, setByContent] = useState<string>();
    const [byStatus, setByStatus] = useState<TodoStatus>();
    const [byCreatedDate, setByCreatedDate] = useState<Date>();

    const [followerId, setFollowerId] = useState<string>();
    const [precedenceIds, setPrecedenceIds] = useState<string>();

    const DEFAULT_SIZE = 7;
    useEffect(() => {
        // getTodos();
        getTodosPaging(page, DEFAULT_SIZE);
    }, []);

    useEffect(() => {
        callGetTodosPagingWithSearchCondition(searchCondition);
    }, [page]);

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
                    <AddButton onClick={() => createTodo(content!)} />
                </div>

                {/* Todo List */}
                <div>
                    <ul className="mt-4">
                        {todos.map((todo) => (
                            <li key={todo.id} className="flex items-center justify-between mt-3 ">
                                <div key={todo.id} className="flex items-center">
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
                </div>

                {/* Pagination */}
                <Pagination totalPages={totalPages} page={page} goNext={goNext} goBefore={goBefore} goPage={goPage} />

                {/* Filter List */}
                <div>
                    <hr className="mt-6 border-t-2 border-gray-300 mb-4" />
                    <div>
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

                    <div className="flex mt-4">
                        <input
                            className="border border-gray-800 focus:border-blue-500 rounded w-full py-2 px-3 mr-4 text-black"
                            placeholder="내용으로 찾기"
                            value={byContent}
                            onChange={(e) => setByContent(e.target.value)}
                        />
                        <FindButton onClick={() => getTodoByByContent(byContent!)} />
                    </div>

                    <div className="flex mt-4">
                        <DatePicker
                            selected={byCreatedDate}
                            dateFormat="yyyy-MM-dd"
                            placeholderText="yyyy-mm-dd"
                            onChange={(date) => setByCreatedDate(date as Date)}
                        />
                        <FindButton onClick={() => getTodosByCreatedDate(byCreatedDate!)} />
                    </div>

                    <div className="text-right text-red-600 mt-4">
                        <span className="cursor-pointer" onClick={clearFindCondition}>
                            초기화
                        </span>
                    </div>
                </div>

                {/* Set Precedence */}
                <hr className="mt-6 border-t-2 border-gray-300 mb-4" />
                <div className="mb-2">선후 관계설정</div>
                <div className="flex">
                    <input
                        type="text"
                        className="border border-gray-800 focus:border-blue-500 rounded w-full py-2 px-3 mr-4 text-black"
                        placeholder="먼저 (예: 1,2)"
                        value={precedenceIds}
                        onChange={(e) => setPrecedenceIds(e.target.value.replace(" ", ""))}
                    />
                    <input
                        type="number"
                        min={1}
                        className="border border-gray-800 focus:border-blue-500 rounded w-full py-2 px-3 mr-4 text-black"
                        placeholder="나중 (예: 3)"
                        value={followerId}
                        onChange={(e) => setFollowerId(e.target.value)}
                    />
                    <CheckButton
                        onClick={() => {
                            const precedenceIdsAsArray: number[] | undefined = precedenceIds
                                ?.replace(/,$/, "")
                                ?.split(",")
                                .map((value) => parseInt(value));
                            if (precedenceIdsAsArray === undefined) {
                                return;
                            }
                            setPrecedence(parseInt(followerId!), precedenceIdsAsArray!);
                        }}
                    />
                </div>
                <div className="text-right text-red-600 mt-4">
                    <span className="cursor-pointer" onClick={clearPrecedenceInput}>
                        초기화
                    </span>
                </div>
            </div>
        </div>
    );

    function getTodoByByContent(content: string) {
        if (content === "") {
            return;
        }
        setSearchCondition(FindConditionType.CONTENT);
        setByContent(content);
        getTodos(FindConditionType.CONTENT, content);
    }

    function GetTodosByStatus(status: TodoStatus) {
        setSearchCondition(FindConditionType.STATUS);
        setByStatus(status);
        getTodos(FindConditionType.STATUS, status);
    }

    function getTodosByCreatedDate(createdDate: Date) {
        setSearchCondition(FindConditionType.CREATED_DATE);
        setByCreatedDate(createdDate);
        getTodos(FindConditionType.CREATED_DATE, toYYYYMMDD(createdDate));
    }

    function clearFindCondition() {
        setSearchCondition(undefined);
        setByContent("");
        setByStatus(undefined);
        setByCreatedDate(undefined);
        getTodos();
    }

    // @Deprecated
    function getTodos(condition?: FindConditionType, by?: string | TodoStatus) {
        switch (condition) {
            case FindConditionType.STATUS:
                api.getTodos(undefined, by as TodoStatus, undefined).then((res) => {
                    setTodosAndSetDoneTodoIds(res);
                });
                break;
            case FindConditionType.CONTENT:
                api.getTodos(by, undefined, undefined).then((res) => {
                    setTodosAndSetDoneTodoIds(res);
                });
                break;
            case FindConditionType.CREATED_DATE:
                api.getTodos(undefined, undefined, by).then((res) => {
                    setTodosAndSetDoneTodoIds(res);
                });
                break;
            default:
                api.getTodos().then((res) => {
                    setTodosAndSetDoneTodoIds(res);
                });
        }
    }

    function getTodosPaging(page: number, size: number, condition?: FindConditionType, by?: string | TodoStatus) {
        switch (condition) {
            case FindConditionType.STATUS:
                api.getTodosPagingByStatus(by as TodoStatus, page, size).then((res) => {
                    setTodosAndDoneTodoIdsAndPagingInfo(res);
                });
                break;
            case FindConditionType.CONTENT:
                api.getTodosPagingByContent(by as string, page, size).then((res) => {
                    setTodosAndDoneTodoIdsAndPagingInfo(res);
                });
                break;
            case FindConditionType.CREATED_DATE:
                api.getTodosPagingByCreatedDate(by as string, page, size).then((res) => {
                    setTodosAndDoneTodoIdsAndPagingInfo(res);
                });
                break;
            default:
                console.log(`default paging call. page: ${page}, size: ${size}`);
                api.getAllTodosPaging(page, size).then((res) => {
                    setTodosAndDoneTodoIdsAndPagingInfo(res);
                });
        }
    }

    function setTodosAndSetDoneTodoIds(res: AxiosResponse<any>) {
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

    function setTodosAndDoneTodoIdsAndPagingInfo(res: AxiosResponse<any>) {
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
        setTotalPages(res.data.totalPages);
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

    function setPrecedence(followerId: number, precedenceIds: Array<number>) {
        api.setPrecedence(followerId, { precedenceIds: precedenceIds }).then(() => {
            // getTodos();
            const precedences = todos.filter((value) => precedenceIds.includes(value.id)).sort((a, b) => a.id - b.id);

            setTodos((prevState) => {
                return prevState.map((todo) => {
                    if (todo.id === followerId) {
                        todo.children = precedences.map((todo) => {
                            return {
                                id: todo.id,
                                content: todo.content,
                                status: todo.status,
                                createdAt: todo.createdAt,
                                updatedAt: new Date(),
                            };
                        });
                        todo.updatedAt = new Date();
                    }
                    return todo;
                });
            });
        });
    }

    function deleteTodo(id: number, cascade: boolean = false) {
        const todo = todos.find((value) => value.id === id);
        if (todo === undefined) {
            console.error(`No todo found where id: ${id}`);
        }
        if (!cascade && todo!.children.length > 0) {
            return alert(
                stripIndents`Cannot remove todo having precedences.
                Retry after removing precedences(${todo!.children.map((it) => it.id)})`
            );
        }
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

    function clearPrecedenceInput() {
        setPrecedenceIds("");
        setFollowerId("");
    }

    function goNext() {
        if (page + 1 >= totalPages) {
            return;
        }
        setPage((prevState) => prevState + 1);
    }

    function goBefore() {
        if (page - 1 < 0) {
            return;
        }
        setPage((prevState) => prevState - 1);
    }

    function goPage(page: number) {
        if (page < 0 || page > totalPages) {
            return;
        }
        setPage(page);
    }

    function callGetTodosPagingWithSearchCondition(searchCondition?: FindConditionType) {
        switch (searchCondition) {
            case FindConditionType.CONTENT:
                getTodosPaging(page, DEFAULT_SIZE, searchCondition, byContent);
                break;
            case FindConditionType.STATUS:
                getTodosPaging(page, DEFAULT_SIZE, searchCondition, byStatus);
                break;
            case FindConditionType.CREATED_DATE:
                getTodosPaging(page, DEFAULT_SIZE, searchCondition, toYYYYMMDD(byCreatedDate!));
                break;
            default:
                getTodosPaging(page, DEFAULT_SIZE);
        }
    }
}

export default TodoMain;
