import React from "react";
import {Navigate, useLocation} from "react-router-dom";
import {useAuthUserContext} from "../../providers/system/AuthUser.tsx";
import {RoleType} from "../../models";

type Props = {
    component: React.ReactNode;
    redirectPath: string;
    allowedRoles?: RoleType[];
}

const isUserAllowed = (roles: RoleType[], allowedRoles?: RoleType[]): boolean => {
    return allowedRoles ? roles.some(role => allowedRoles.includes(role)) : true;
}

export const useAuthUser = (): RoleType => {
    const {user: authUser} = useAuthUserContext();
    if (!authUser) return RoleType.USER;
    return authUser.roles[0];
}

export const RouteAuthGuard: React.FC<Props> = ({component, redirectPath, allowedRoles}) => {
    const {user: authUser} = useAuthUserContext();
    const location = useLocation();

    if (!authUser) {
        return <Navigate to={redirectPath} state={{from: location}} replace/>;
    }

    if (!isUserAllowed(authUser.roles, allowedRoles)) {
        return <Navigate to={redirectPath} state={{from: location}} replace/>;
    }

    return <>{component}</>;
}
