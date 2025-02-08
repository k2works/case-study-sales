import React, {useEffect} from "react";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import {UserProvider, useUserContext} from "../../../providers/system/User.tsx";
import {UserCollection} from "./UserCollection.tsx";

export const UserContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            fetchUsers,
        } = useUserContext();

        useEffect(() => {
            fetchUsers.load().then(() => {
            });
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <UserCollection/>
                )}
            </>
        );
    }

    return (
        <SiteLayout>
            <UserProvider>
                <Content/>
            </UserProvider>
        </SiteLayout>
    );
};
