import ErrorBoundary from "./ErrorBoundary.tsx";
import React from "react";
import {MessageScreen} from "../../views/application/MessageScreen.tsx";
import {ErrorScreen} from "../../views/application/ErrorScreen.tsx";
import {showErrorMessage} from "./utils.ts";

interface MessageProps {
    message: string | null;
    error: string | null;
}

export const useMessage = () => {
    const [message, setMessage] = React.useState<string | null>(null);
    const [error, setError] = React.useState<string | null>(null);

    return {
        message,
        setMessage,
        error,
        setError,
        showErrorMessage
    }
}

export const Message: React.FC<MessageProps> = ({message, error}) => {
    if (message) {
        return <MessageScreen message={{content: message}}/>;
    } else if (error) {
        return (
            <ErrorBoundary>
                <ErrorScreen error={{message: error}}/>
            </ErrorBoundary>
        );
    }
    return null;
};
