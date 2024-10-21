import {Route, Routes} from "react-router-dom";
import React from "react";
import {RouteAuthGuard} from "./RouteAuthGuard";
import {NotFound} from "./NotFound";
import {Login} from "../system/Login.tsx";
import {Logout} from "../system/Logout.tsx";
import {Home} from "./Home.tsx";
import {User} from "../system/User.tsx";
import {RoleType} from "../../types";
import {Department} from "../master/Department.tsx";
import {Employee} from "../master/Employee.tsx";

export const RouteConfig: React.FC = () => {
    return (
        <>
            <Routes>
                <Route path="/" element={<RouteAuthGuard component={<Home/>} redirectPath="/login"/>}/>
                <Route path="/user" element={<RouteAuthGuard component={<User/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/department" element={<RouteAuthGuard component={<Department/>} redirectPath="/"
                                                                   allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/employee" element={<RouteAuthGuard component={<Employee/>} redirectPath="/"
                                                                 allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
        </>
    );
}
