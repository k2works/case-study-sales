import React from "react";

type MessageScreenProps = {
    message?: {
        content: string;
    };
};

export const MessageScreen: React.FC<MessageScreenProps> = ({message = {content: ""}}: MessageScreenProps) => {
    return (
        <div className="view-message-container" id="message">
            <div className="view-message-content">
                <div className="view-message-content-text">
                    {message && <p>{message.content}</p>}
                </div>
            </div>
        </div>
    );
}
