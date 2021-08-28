import { TodoWithoutChildren } from "./TodoWithoutChildren";
import { TodoStatus } from "./TodoStatus";

export interface Todo {
    id: number;
    content: string;
    children: Array<TodoWithoutChildren>;
    status: TodoStatus;
    createdAt: Date;
    updatedAt?: Date;
}
