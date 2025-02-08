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

//TODO: エラー:(17, 30) ESLint: React Hook &quot;useAuthUserContext&quot; is called in function &quot;getRoleFromUser&quot; that is neither a React function component nor a custom React Hook function. React component names must start with an uppercase letter. React Hook names must start with the word &quot;use&quot;. (react-hooks/rules-of-hooks)
export const getRoleFromUser = (): RoleType => {
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
