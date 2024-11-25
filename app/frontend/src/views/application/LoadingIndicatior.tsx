import React from "react";
import BeatLoader from "react-spinners/BeatLoader";

const LOADING_COLOR = "#36D7B7";

const LoadingIndicator: React.FC = () => (
    <div className="loading">
        <BeatLoader color={LOADING_COLOR}/>
    </div>
);

export default LoadingIndicator;