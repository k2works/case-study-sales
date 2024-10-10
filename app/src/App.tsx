import React, {useState} from 'react'
import './App.css'
import SiteLayout from "./components/application/SiteLayout";
import ErrorBoundary from "./components/application/ErrorBoundary";

function App() {
    const [count, setCount] = useState(0)

    return (
        <SiteLayout menu={
            <p>Site Layout Menu</p>
        }>
            <ErrorBoundary>
                <h1>Contents</h1>
                <p>This is the main part of the example layout</p>
            </ErrorBoundary>
        </SiteLayout>
    );
}

export default App;
