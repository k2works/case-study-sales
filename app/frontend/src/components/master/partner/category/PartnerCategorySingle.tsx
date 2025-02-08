import React from "react";
import {usePartnerCategoryContext} from "../../../../providers/PartnerCategory.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerCategorySingleView} from "../../../../views/master/partner/category/PartnerCategorySingle.tsx";
import {
    PartnerCategoryItemCollectionAddListView
} from "../../../../views/master/partner/category/ParnterCategoryItemCollection.tsx";
import {PartnerCategoryItemType} from "../../../../models/master/partner";
import {PartnerCategoryItemEditModal} from "./PartnerCategoryItemEditModal.tsx";
import {PartnerSelectModal} from "./PartnerSelectModal.tsx";

export const PartnerCategorySingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        setCategoryItemModalIsOpen,
        setCategoryItemIsEditing,
        initialPartnerCategory,
        newPartnerCategory,
        setNewPartnerCategory,
        setNewPartnerCategoryItem,
        fetchPartnerCategories,
        partnerCategoryService,
    } = usePartnerCategoryContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePartnerCategory = async () => {
        const validatePartnerCategory = (): boolean => {
            if (!newPartnerCategory.partnerCategoryTypeCode.trim() ||
                !newPartnerCategory.partnerCategoryTypeName.trim()) {
                setError("取引先分類コードと名称は必須項目です。");
                return false;
            }
            return true;
        };

        if (!validatePartnerCategory()) return;

        try {
            if (isEditing && editId) {
                await partnerCategoryService.update(newPartnerCategory);
            } else {
                await partnerCategoryService.create(newPartnerCategory);
            }

            setNewPartnerCategory({ ...initialPartnerCategory });
            await fetchPartnerCategories.load();

            setMessage(isEditing ? "取引先分類を更新しました。" : "取引先分類を作成しました。");
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`取引先分類の作成または更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <>
            <PartnerCategoryItemEditModal/>
            <PartnerSelectModal/>
            <PartnerCategorySingleView
                error={error}
                message={message}
                isEditing={isEditing}
                headerItems={{ handleCreateOrUpdatePartnerCategory, handleCloseModal }}
                formItems={{ newPartnerCategory, setNewPartnerCategory }}
            />

            {isEditing && (
                <PartnerCategoryItemCollectionAddListView
                    partnerCategory = {newPartnerCategory}
                    partnerCategoryItems={newPartnerCategory.partnerCategoryItems}
                    handleNew={(partnerCategoryItem: PartnerCategoryItemType) => {
                        const newPartnerCategoryItems = newPartnerCategory.partnerCategoryItems.filter((e) => e.partnerCategoryItemCode !== partnerCategoryItem.partnerCategoryItemCode);
                        newPartnerCategoryItems.push({
                            ...partnerCategoryItem
                        });
                        setNewPartnerCategory({
                            ...newPartnerCategory,
                            partnerCategoryItems: newPartnerCategoryItems
                        });
                    }}
                    handleAdd={(code) => {
                        setMessage("");
                        setError("");
                        setCategoryItemIsEditing(true);
                        setCategoryItemModalIsOpen(true);
                        const newPartnerCategoryItem = newPartnerCategory.partnerCategoryItems.find((e) => e.partnerCategoryItemCode == code);
                        if (newPartnerCategoryItem) {
                            setNewPartnerCategoryItem({...newPartnerCategoryItem});
                        }
                    }}
                    handleDelete={(partnerCategoryItem: PartnerCategoryItemType) => {
                        setNewPartnerCategory({
                            ...newPartnerCategory,
                            partnerCategoryItems: newPartnerCategory.partnerCategoryItems.filter((e) => e.partnerCategoryItemCode !== partnerCategoryItem.partnerCategoryItemCode)
                        });
                    }}
                />
            )
            }
        </>
    );
}
