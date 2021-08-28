import React from "react";
import {TodoStatus} from "../domain/TodoStatus";
import {toYYYYMMDD} from "../../global/util";

export const TodoDisplay = (props: {
    id: number;
    content: string;
    status: TodoStatus;
    childrenIds: Array<number>;
    createdAt: Date;
    updatedAt?: Date;
}) => {
    const { id, content, status, childrenIds, createdAt, updatedAt } = props;
    return status === TodoStatus.DONE ? (
        <div className="ml-3 font-semibold line-through">
            <div>#{id} {content}</div>
            {childrenIds.map((childId) => <span>@{childId}</span>)}
            <div>작성: {toYYYYMMDD(createdAt)} {updatedAt && `수정: ${toYYYYMMDD(updatedAt)}`}</div>
        </div>
    ) : (
        <div className="ml-3 font-semibold">
            <div>#{id} {content}</div>
            {childrenIds.map((childId) => <span>@{childId} </span>)}
            <div>작성: {toYYYYMMDD(createdAt)} {updatedAt && `수정: ${toYYYYMMDD(updatedAt)}`}</div>
        </div>
    );
};
