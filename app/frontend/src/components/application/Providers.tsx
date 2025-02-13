import React from "react";
import {AuthUserProvider} from "../../providers/system/AuthUser.tsx";

type Props = {
    children: React.ReactNode;
}
export const Providers: React.FC<Props> = (props) => {
    return (
            <AuthUserProvider>
                {props.children}
            </AuthUserProvider>
    );
}
