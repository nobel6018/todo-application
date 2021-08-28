import React from "react";

export const DeleteButton = (props: {
    onClick: any;
}) => {
    return (
        <button onClick={props.onClick}>
            <svg className="w-4 h-4 text-gray-600 fill-current" fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" viewBox="0 0 24 24" stroke="currentColor">
                <path d="M6 18L18 6M6 6l12 12" />
            </svg>
        </button>
    );
};
