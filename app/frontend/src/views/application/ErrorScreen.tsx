import React from "react";

type ErrorScreenProps = {
    error?: {
        message: string;
    };
};

export const ErrorScreen: React.FC<ErrorScreenProps> = ({error = {message: "Unknown error"}}: ErrorScreenProps) => {
    return (
        <div className="view-message-container" id="message">
            <div className="view-message-content">
                <div className="view-message-content-error-text">
                    {error && <p>{error.message}</p>}
                </div>
            </div>
        </div>
    );
}
