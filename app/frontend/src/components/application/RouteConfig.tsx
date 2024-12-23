import {Route, Routes} from "react-router-dom";
import React from "react";
import {RouteAuthGuard} from "./RouteAuthGuard.tsx";
import {User} from "../system/User.tsx";
import {Home} from "../../views/application/Home.tsx";
import {NotFound} from "../../views/application/NotFound.tsx";
import {RoleType} from "../../models";
import {Login, Logout} from "../system/Auth.tsx";

export const RouteConfig: React.FC = () => {
    return (
        <>
            <Routes>
                <Route path="/" element={<RouteAuthGuard component={<Home/>} redirectPath="/login"/>}/>
                <Route path="/user" element={<RouteAuthGuard component={<User/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
        </>
    );
}
