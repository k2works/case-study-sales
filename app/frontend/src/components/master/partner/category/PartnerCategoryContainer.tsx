import React, { useEffect} from 'react';
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {PartnerCategoryProvider, usePartnerCategoryContext} from "../../../../providers/PartnerCategory.tsx";
import {PartnerListProvider, usePartnerListContext} from "../../../../providers/PartnerList.tsx";
import {PartnerCategoryCollection} from "./PartnerCategoryCollection.tsx";

export const PartnerCategoryContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPartnerCategories,
        } = usePartnerCategoryContext();

        const {
            fetchPartners,
        } = usePartnerListContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchPartnerCategories.load(),
                        fetchPartners.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`取引先分類情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <PartnerCategoryCollection/>
                )}
            </>
        );
    };

    return (
        <PartnerCategoryProvider>
            <PartnerListProvider>
                <Content />
            </PartnerListProvider>
        </PartnerCategoryProvider>
    );
};