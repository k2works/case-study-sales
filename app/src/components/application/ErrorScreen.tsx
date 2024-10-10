import React from "react";

type ErrorScreenProps = {
    error?: {
        message: string;
    };
};

export default function ErrorScreen({error = {message: "Unknown error"}}: ErrorScreenProps) {
    return (
        <div className="error">
            <h3>We are sorry... something went wrong</h3>
            <p>We cannot process your request at this moment.</p>
            <p>ERROR: {error.message}</p>
        </div>
    );
}
