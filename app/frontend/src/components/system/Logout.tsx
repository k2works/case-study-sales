import {useNavigate} from "react-router-dom";
import React from "react";
import {AuthUserContextType, useAuthUserContext} from "../../providers/AuthUser";

export const Logout: React.FC = () => {
    const authUser: AuthUserContextType = useAuthUserContext();
    const navigate = useNavigate();

    const handleSignOut = () => {
        authUser.signOut(() => {
            navigate("/login");
        });
    };

    React.useEffect(() => {
        handleSignOut();
    }, []);

    return null;
};
