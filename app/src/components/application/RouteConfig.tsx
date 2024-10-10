import {Route, Routes} from "react-router-dom";
import React from "react";
import {RouteAuthGuard} from "./RouteAuthGuard";
import {NotFound} from "./NotFound";
import {Login} from "../system/Login.tsx";
import {Logout} from "../system/Logout.tsx";
import {Home} from "./Home.tsx";

export const RouteConfig: React.FC = () => {
    return (
        <>
            <Routes>
                <Route path="/" element={<RouteAuthGuard component={<Home/>} redirectPath="/login"/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
        </>
    );
}

