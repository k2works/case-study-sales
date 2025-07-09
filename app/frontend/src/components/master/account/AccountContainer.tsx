import React, {useEffect} from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {AccountProvider, useAccountContext} from "../../../providers/master/Account.tsx";
import {AccountCollection} from "./AccountCollection.tsx";

export const AccountContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchAccounts,
        } = useAccountContext();

        useEffect(() => {
            (async () => {
                try {
                    await fetchAccounts.load();
                } catch (error: unknown) {
                    showErrorMessage(`口座情報の取得に失敗しました: ${error instanceof Error ? error.message : String(error)}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <AccountCollection/>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <AccountProvider>
                <Content/>
            </AccountProvider>
        </SiteLayout>
    );
};