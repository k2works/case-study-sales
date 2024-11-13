import React, {Component, ReactNode} from "react";
import {ErrorScreen} from "../../views/application/ErrorScreen.tsx";

interface ErrorBoundaryProps {
    children: ReactNode;
    fallback?: (props: { error: Error }) => ReactNode;
}

interface ErrorBoundaryState {
    error: Error | null;
}

export default class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
    state: ErrorBoundaryState = {error: null};

    static getDerivedStateFromError(error: Error): ErrorBoundaryState {
        return {error};
    }

    render() {
        const {error} = this.state;
        const {children, fallback} = this.props;

        if (error) {
            return fallback ? fallback({error}) : <ErrorScreen error={error}/>;
        }

        return children;
    }
}
