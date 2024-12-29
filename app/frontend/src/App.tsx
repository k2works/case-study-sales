import React from 'react'
import './App.css'
import {Providers} from "./components/application/Providers.tsx";
import {RouteConfig} from "./components/application/RouteConfig.tsx";

export const App = () => {
    return (
        <>
            <Providers>
                <RouteConfig/>
            </Providers>
        </>
    );
}

export default App;
