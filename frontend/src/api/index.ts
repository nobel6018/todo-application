import axios from "axios";
import { BASE_URL } from "./Config";
import { TodoStatus } from "../todo/domain/TodoStatus";
import { CreateTodoDTO } from "../todo/dto/CreateTodoDTO";
import { SetChildrenDTO } from "../todo/dto/SetChildrenDTO";

export const apiInstance = axios.create({
    baseURL: BASE_URL,
    timeout: 3000,
});

function getTodos(content?: string, status?: TodoStatus, createdDate?: Date) {
    return apiInstance.get(`/api/v1/todos`, { params: { content, status, createdDate } });
}

function createTodo(todo: CreateTodoDTO) {
    return apiInstance.post(`/api/v1/todos`, todo);
}

function updateTodoStatus(todoId: number, status: TodoStatus) {
    return apiInstance.patch(`/api/v1/todos/${todoId}`, status);
}

function setChildren(parentId: number, childrenIds: SetChildrenDTO) {
    return apiInstance.patch(`/api/v1/todos/${parentId}`, childrenIds);
}

function deleteTodo(id: number) {
    return apiInstance.delete(`/api/v1/todos/${id}`);
}

export default {
    getTodos,
    createTodo,
    updateTodoStatus,
    setChildren,
    deleteTodo,
};
