import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {CustomLocation, DataType, RoleType, UserType} from "../../models";
import {AuthUserContextType, useAuthUserContext} from "../../providers/AuthUser";
import AuthService from "../../services/auth";
import {LoginSingleView} from "../../views/system/Login.tsx";

const DEFAULT_USER_ID = "U000003";
const DEFAULT_PASSWORD = "a234567Z";

export const Login: React.FC = () => {
    const [userId, setUserId] = useState(DEFAULT_USER_ID);
    const [password, setPassword] = useState(DEFAULT_PASSWORD);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const location: CustomLocation = useLocation() as CustomLocation;
    const fromPathName: string = location.state?.from?.pathname || "/";
    const authUser: AuthUserContextType = useAuthUserContext();

    useEffect(() => {
        if (authUser.isLogin()) {
            navigate("/", {replace: true});
        }
    }, [authUser, navigate]);

    const handleSignIn = async () => {
        const authService = AuthService();
        try {
            const result = await authService.signIn(userId, password);
            if (result.message) {
                setMessage(result.message);
                return;
            }
            const data: DataType = result as DataType;
            const user: UserType = {
                userId: data.userId,
                token: data.accessToken,
                roles: data.roles as RoleType[],
            };
            authUser.signIn(user, () => {
                navigate(fromPathName, {replace: true});
            });
        } catch (e: any) {
            setMessage(e.message);
        }
    };

    return (
        <LoginSingleView
            message={message}
            handleSignIn={handleSignIn}
            userId={userId}
            setUserId={setUserId}
            password={password}
            setPassword={setPassword}
        />
    );
}
