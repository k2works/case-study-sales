import React from "react";
import {usePartnerCategoryContext} from "../../../../providers/PartnerCategory.tsx";
import {PartnerCategoryType} from "../../../../models/master/partner";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerCategoryCollectionView} from "../../../../views/master/partner/category/PartnerCategoryCollection.tsx";
import {PartnerCategorySearchModal} from "./PartnerCategorySearchModal.tsx";
import {PartnerCategoryTypeEditModal} from "./PartnerCategoryTypeEditModal.tsx";

export const PartnerCategoryCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setSearchModalIsOpen,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialPartnerCategory,
        partnerCategories,
        setPartnerCategories,
        setNewPartnerCategory,
        searchPartnerCategoryCriteria,
        setSearchPartnerCategoryCriteria,
        fetchPartnerCategories,
        partnerCategoryService,
    } = usePartnerCategoryContext();

    const handleOpenModal = (partnerCategory?: PartnerCategoryType) => {
        setMessage("");
        setError("");
        if (partnerCategory) {
            setNewPartnerCategory(partnerCategory);
            setEditId(partnerCategory.partnerCategoryTypeCode);
            setIsEditing(true);
        } else {
            setNewPartnerCategory(initialPartnerCategory);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

    const handleDeletePartnerCategory = async (partnerCategoryTypeCode: string) => {
        try {
            if (!window.confirm(`取引先分類コード:${partnerCategoryTypeCode} を削除しますか？`))
                return;
            await partnerCategoryService.destroy(partnerCategoryTypeCode);
            await fetchPartnerCategories.load();
            setMessage("取引先分類を削除しました。");
        } catch (error: any) {
            showErrorMessage(`取引先分類の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckPartnerCategory = (partnerCategory: PartnerCategoryType) => {
        const newPartnerCategories = partnerCategories.map((category) => {
            if (category.partnerCategoryTypeCode === partnerCategory.partnerCategoryTypeCode) {
                return { ...category, checked: !category.checked };
            }
            return category;
        });
        setPartnerCategories(newPartnerCategories);
    };

    const handleCheckAllPartnerCategory = () => {
        const newPartnerCategories = partnerCategories.map((category) => ({
            ...category,
            checked: !partnerCategories.every((category) => category.checked),
        }));
        setPartnerCategories(newPartnerCategories);
    };

    const handleDeleteCheckedCollection = async () => {
        const checkedPartnerCategories = partnerCategories.filter((category) => category.checked);
        if (!checkedPartnerCategories.length) {
            setError("削除する取引先分類を選択してください。");
            return;
        }
        try {
            if (!window.confirm("選択した取引先分類を削除しますか？")) return;
            await Promise.all(
                checkedPartnerCategories.map((category) =>
                    partnerCategoryService.destroy(category.partnerCategoryTypeCode)
                )
            );
            await fetchPartnerCategories.load();
            setMessage("選択した取引先分類を削除しました。");
        } catch (error: any) {
            showErrorMessage(
                `選択した取引先分類の削除に失敗しました: ${error?.message}`,
                setError
            );
        }
    };

    return (
        <>
            <PartnerCategorySearchModal/>
            <PartnerCategoryTypeEditModal/>
            <PartnerCategoryCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchPartnerCategoryCriteria,
                    setSearchPartnerCategoryCriteria,
                    handleOpenSearchModal,
                }}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllPartnerCategory,
                    handleDeleteCheckedCollection,
                }}
                collectionItems={{
                    partnerCategories,
                    handleOpenModal,
                    handleDeletePartnerCategory,
                    handleCheckPartnerCategory,
                }}
                pageNationItems={{
                    pageNation,
                    fetchCategories: fetchPartnerCategories.load,
                    criteria,
                }}
            />
        </>
    );
};
