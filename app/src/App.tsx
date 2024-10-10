import React from 'react'
import './App.css'
import {Providers} from "./components/application/Providers";
import {RouteConfig} from "./components/application/RouteConfig";

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
