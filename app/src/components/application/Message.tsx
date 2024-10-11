import {MessageScreen} from "./MessageScreen";
import ErrorBoundary from "./ErrorBoundary";
import {ErrorScreen} from "./ErrorScreen";
import React from "react";

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
