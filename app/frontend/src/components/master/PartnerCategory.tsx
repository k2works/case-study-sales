import React, { useEffect, useState } from 'react';
import { showErrorMessage } from "../application/utils.ts";
import { useMessage } from "../application/Message.tsx";
import { useModal } from "../application/hooks.ts";
import { usePageNation } from "../../views/application/PageNation.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import { PartnerCategoryCriteriaType, PartnerCategoryType } from "../../models";
import { useFetchPartnerCategories, usePartnerCategory } from "./hooks";
import Modal from "react-modal";
import {PartnerCategorySearchView} from "../../views/master/partner/category/PartnerCategorySearch.tsx";
import {PartnerCategoryCollectionView} from "../../views/master/partner/category/PartnerCategoryCollection.tsx";
import {PartnerCategorySingleView} from "../../views/master/partner/category/PartnerCategorySingle.tsx";

export const PartnerCategory: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();
        const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PartnerCategoryCriteriaType>();
        const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
        const {modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen,} = useModal();
        const { initialPartnerCategory, partnerCategories, setPartnerCategories, newPartnerCategory,
            setNewPartnerCategory, searchPartnerCategoryCriteria,
            setSearchPartnerCategoryCriteria, partnerCategoryService } = usePartnerCategory();

        const fetchPartnerCategories = useFetchPartnerCategories(
            setLoading,
            setPartnerCategories,
            setPageNation,
            setError,
            showErrorMessage,
            partnerCategoryService
        );

        useEffect(() => {
            fetchPartnerCategories.load();
        }, []);

        // Modalロジック
        const modalView = () => {
            const editModal = () => {
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

                const handleCloseModal = () => {
                    setError("");
                    setModalIsOpen(false);
                    setEditId(null);
                };

                const editModalView = () => {
                    return (
                        <Modal
                            isOpen={modalIsOpen}
                            onRequestClose={handleCloseModal}
                            contentLabel="取引先分類情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {singleView()}
                        </Modal>
                    );
                };

                return {
                    editModalView,
                    handleOpenModal,
                    handleCloseModal,
                };
            };

            const searchModal = () => {
                const handleOpenSearchModal = () => {
                    setSearchModalIsOpen(true);
                };

                const handleCloseSearchModal = () => {
                    setSearchModalIsOpen(false);
                };

                const searchModalView = () => {
                    return (
                        <Modal
                            isOpen={searchModalIsOpen}
                            onRequestClose={handleCloseSearchModal}
                            contentLabel="検索情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            <PartnerCategorySearchView
                                criteria={searchPartnerCategoryCriteria}
                                setCondition={setSearchPartnerCategoryCriteria}
                                handleSelect={async () => {
                                    if (!searchPartnerCategoryCriteria) {
                                        return;
                                    }
                                    setLoading(true);
                                    try {
                                        const result = await partnerCategoryService.search(searchPartnerCategoryCriteria);
                                        setPartnerCategories(result ? result.list : []);
                                        if (result.list.length === 0) {
                                            showErrorMessage("検索結果は0件です", setError);
                                        } else {
                                            setCriteria(searchPartnerCategoryCriteria);
                                            setPageNation(result);
                                            setMessage("");
                                            setError("");
                                        }
                                    } catch (error: any) {
                                        showErrorMessage(`検索に失敗しました: ${error?.message}`, setError);
                                    } finally {
                                        setLoading(false);
                                    }
                                }}
                                handleClose={handleCloseSearchModal}
                            />
                        </Modal>
                    );
                };

                return {
                    searchModalView,
                    handleOpenSearchModal,
                    handleCloseSearchModal,
                };
            };

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {searchModal().searchModalView()}
                </>
            );

            return {
                editModal,
                searchModal,
                init,
            };
        };

        const collectionView = () => {
            const { handleOpenModal } = modalView().editModal();
            const { handleOpenSearchModal } = modalView().searchModal();

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
            );
        };

        // 編集用モーダルのシングルビュー
        const singleView = () => {
            const { handleCloseModal } = modalView().editModal();
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
                <PartnerCategorySingleView
                    error={error}
                    message={message}
                    isEditing={isEditing}
                    headerItems={{ handleCreateOrUpdatePartnerCategory, handleCloseModal }}
                    formItems={{ newPartnerCategory, setNewPartnerCategory }}
                />
            );
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <>
                        {modalView().init()}
                        {collectionView()}
                    </>
                )}
            </>
        );
    };

    return <Content />;
};