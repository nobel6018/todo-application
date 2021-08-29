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

function getTodos(content?: string, status?: TodoStatus, createdDate?: string) {
    return apiInstance.get(`/api/v1/todos`, { params: { content, status, createdDate } });
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
    createTodo,
    updateTodoStatus,
    setPrecedence,
    deleteTodo,
};
