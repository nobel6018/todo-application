import React from "react";
import { TodoStatus } from "../domain/TodoStatus";
import { toYYYYMMDD } from "../../global/util";

export const TodoDisplay = (props: {
    id: number;
    content: string;
    status: TodoStatus;
    childrenIds: Array<number>;
    createdAt: Date;
    updatedAt?: Date;
}) => {
    const { id, content, status, childrenIds, createdAt, updatedAt } = props;
    return (
        <div className={`ml-3 font-semibold ${status === TodoStatus.DONE ? "line-through" : ""}`}>
            <div>
                <span>#{id}</span> <span>{content}</span>
            </div>
            <div>
                {childrenIds.map((childId) => (
                    <span>@{childId}</span>
                ))}
            </div>
            <div>
                <span>작성: {toYYYYMMDD(createdAt)}</span> <span>{updatedAt && `수정: ${toYYYYMMDD(updatedAt)}`}</span>
            </div>
        </div>
    );
};
