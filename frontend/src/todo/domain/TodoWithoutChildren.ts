import { TodoStatus } from "./TodoStatus";

export interface TodoWithoutChildren {
    id: number;
    content: string;
    status: TodoStatus;
    createdAt: Date;
    updatedAt?: Date;
}
