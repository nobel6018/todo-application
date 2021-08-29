import axios from "axios";
import { BASE_URL } from "./Config";
import { TodoStatus } from "../todo/domain/TodoStatus";
import { CreateTodoDTO } from "../todo/dto/CreateTodoDTO";
import { SetPrecedenceDTO } from "../todo/dto/SetPrecedenceDTO";
import { UpdateTodoStatusDTO } from "../todo/dto/UpdateTodoStatusDTO";

export const apiInstance = axios.create({
    baseURL: BASE_URL,
    headers: { "Content-Type": "application/json" },
    timeout: 3000,
});

// @Deprecated
function getTodos(content?: string, status?: TodoStatus, createdDate?: string) {
    return apiInstance.get(`/api/v1/todos`, { params: { content, status, createdDate } });
}

const DEFAULT_SIZE = 7;

function getAllTodosPaging(page: number, size: number = DEFAULT_SIZE) {
    return apiInstance.get(`/api/v2/todos?page=${page}&size=${size}`);
}

function getTodosPagingByContent(content: string, page: number, size: number = DEFAULT_SIZE) {
    return apiInstance.get(`/api/v2/todos?content=${content}&page=${page}&size=${size}`);
}

function getTodosPagingByStatus(status: TodoStatus, page: number, size: number = DEFAULT_SIZE) {
    return apiInstance.get(`/api/v2/todos?status=${status}&page=${page}&size=${size}`);
}

function getTodosPagingByCreatedDate(createdDate: string, page: number, size: number = DEFAULT_SIZE) {
    return apiInstance.get(`/api/v2/todos?createdDate=${createdDate}&page=${page}&size=${size}`);
}

function createTodo(todo: CreateTodoDTO) {
    return apiInstance.post(`/api/v1/todos`, todo);
}

function updateTodoStatus(todoId: number, status: UpdateTodoStatusDTO) {
    return apiInstance.patch(`/api/v1/todos/${todoId}/status`, status);
}

function setPrecedence(followerId: number, precedenceIds: SetPrecedenceDTO) {
    return apiInstance.patch(`/api/v1/todos/${followerId}/precedence`, precedenceIds);
}

function deleteTodo(id: number) {
    return apiInstance.delete(`/api/v1/todos/${id}`);
}

export default {
    getTodos,
    getAllTodosPaging,
    getTodosPagingByContent,
    getTodosPagingByStatus,
    getTodosPagingByCreatedDate,
    createTodo,
    updateTodoStatus,
    setPrecedence,
    deleteTodo,
};
