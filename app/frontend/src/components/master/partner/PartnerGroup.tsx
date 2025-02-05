// PartnerGroup.tsx
import React, { useEffect, useState } from 'react';
import { showErrorMessage } from "../../application/utils.ts";
import { useMessage } from "../../application/Message.tsx";
import { useModal } from "../../application/hooks.ts";
import { usePageNation } from "../../../views/application/PageNation.tsx";
import { PartnerGroupCriteriaType, PartnerGroupType } from "../../../models/master/partner";
import { useFetchPartnerGroups, usePartnerGroup } from "./hooks";
import Modal from "react-modal";
import { PartnerGroupSearchView } from "../../../views/master/partner/group/PartnerGroupSearch.tsx";
import { PartnerGroupCollectionView } from "../../../views/master/partner/group/PartnerGroupCollection.tsx";
import { PartnerGroupSingleView } from "../../../views/master/partner/group/PartnerGroupSingle.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";

export const PartnerGroup: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();
        const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PartnerGroupCriteriaType>();
        const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
        const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
        const {
            initialPartnerGroup,
            partnerGroups,
            setPartnerGroups,
            newPartnerGroup,
            setNewPartnerGroup,
            searchPartnerGroupCriteria,
            setSearchPartnerGroupCriteria,
            partnerGroupService
        } = usePartnerGroup();
        const fetchPartnerGroups = useFetchPartnerGroups(
            setLoading,
            setPartnerGroups,
            setPageNation,
            setError,
            showErrorMessage,
            partnerGroupService
        );

        useEffect(() => {
            fetchPartnerGroups.load();
        }, []);

        const modalView = () => {
            const editModal = () => {
                const handleOpenModal = (partnerGroup?: PartnerGroupType) => {
                    setMessage("");
                    setError("");
                    if (partnerGroup) {
                        setNewPartnerGroup(partnerGroup);
                        setEditId(partnerGroup.partnerGroupCode.value);
                        setIsEditing(true);
                    } else {
                        setNewPartnerGroup(initialPartnerGroup);
                        setIsEditing(false);
                    }
                    setModalIsOpen(true);
                };

                const handleCloseModal = () => {
                    setError("");
                    setModalIsOpen(false);
                    setEditId(null);
                };

                const editModalView = () => (
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={handleCloseModal}
                        contentLabel="取引先グループ情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {singleView()}
                    </Modal>
                );

                return { editModalView, handleOpenModal, handleCloseModal };
            };

            const searchModal = () => {
                const handleOpenSearchModal = () => {
                    setSearchModalIsOpen(true);
                };

                const handleCloseSearchModal = () => {
                    setSearchModalIsOpen(false);
                };

                const searchModalView = () => (
                    <Modal
                        isOpen={searchModalIsOpen}
                        onRequestClose={handleCloseSearchModal}
                        contentLabel="検索情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        <PartnerGroupSearchView
                            criteria={searchPartnerGroupCriteria}
                            setCondition={setSearchPartnerGroupCriteria}
                            handleSelect={async () => {
                                if (!searchPartnerGroupCriteria) return;

                                setLoading(true);
                                try {
                                    const result = await partnerGroupService.search(searchPartnerGroupCriteria);
                                    setPartnerGroups(result ? result.list : []);
                                    if (result.list.length === 0) {
                                        showErrorMessage("検索結果は0件です", setError);
                                    } else {
                                        setCriteria(searchPartnerGroupCriteria);
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

                return { searchModalView, handleOpenSearchModal, handleCloseSearchModal };
            };

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {searchModal().searchModalView()}
                </>
            );

            return { editModal, searchModal, init };
        };

        const collectionView = () => {
            const { handleOpenModal } = modalView().editModal();
            const { handleOpenSearchModal } = modalView().searchModal();

            const handleDeletePartnerGroup = async (partnerGroupTypeCode: string) => {
                try {
                    if (!window.confirm(`取引先グループコード:${partnerGroupTypeCode} を削除しますか？`))
                        return;
                    await partnerGroupService.destroy(partnerGroupTypeCode);
                    await fetchPartnerGroups.load();
                    setMessage("取引先グループを削除しました。");
                } catch (error: any) {
                    showErrorMessage(`取引先グループの削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckPartnerGroup = (partnerGroup: PartnerGroupType) => {
                const newPartnerGroups = partnerGroups.map((group) => {
                    if (group.partnerGroupCode === partnerGroup.partnerGroupCode) {
                        return { ...group, checked: !group.checked };
                    }
                    return group;
                });
                setPartnerGroups(newPartnerGroups);
            };

            const handleCheckAllPartnerGroup = () => {
                const newPartnerGroups = partnerGroups.map((group) => ({
                    ...group,
                    checked: !partnerGroups.every((group) => group.checked),
                }));
                setPartnerGroups(newPartnerGroups);
            };

            const handleDeleteCheckedCollection = async () => {
                const checkedPartnerGroups = partnerGroups.filter((group) => group.checked);
                if (!checkedPartnerGroups.length) {
                    setError("削除する取引先グループを選択してください。");
                    return;
                }
                try {
                    if (!window.confirm("選択した取引先グループを削除しますか？")) return;
                    await Promise.all(
                        checkedPartnerGroups.map((group) =>
                            partnerGroupService.destroy(group.partnerGroupCode.value)
                        )
                    );
                    await fetchPartnerGroups.load();
                    setMessage("選択した取引先グループを削除しました。");
                } catch (error: any) {
                    showErrorMessage(
                        `選択した取引先グループの削除に失敗しました: ${error?.message}`,
                        setError
                    );
                }
            };

            return (
                <PartnerGroupCollectionView
                    error={error}
                    message={message}
                    searchItems={{
                        searchPartnerGroupCriteria,
                        setSearchPartnerGroupCriteria,
                        handleOpenSearchModal,
                    }}
                    headerItems={{
                        handleOpenModal,
                        handleCheckToggleCollection: handleCheckAllPartnerGroup,
                        handleDeleteCheckedCollection,
                    }}
                    collectionItems={{
                        partnerGroups,
                        handleOpenModal,
                        handleDeletePartnerGroup,
                        handleCheckPartnerGroup,
                    }}
                    pageNationItems={{
                        pageNation,
                        fetchGroups: fetchPartnerGroups.load,
                        criteria,
                    }}
                />
            );
        };

        const singleView = () => {
            const { handleCloseModal } = modalView().editModal();
            const handleCreateOrUpdatePartnerGroup = async () => {
                const validatePartnerGroup = (): boolean => {
                    if (!newPartnerGroup.partnerGroupCode.value.trim() ||
                        !newPartnerGroup.partnerGroupName.trim()) {
                        setError("取引先グループコードと名称は必須項目です。");
                        return false;
                    }
                    return true;
                };

                if (!validatePartnerGroup()) return;

                try {
                    if (isEditing && editId) {
                        await partnerGroupService.update(newPartnerGroup);
                    } else {
                        await partnerGroupService.create(newPartnerGroup);
                    }
                    setNewPartnerGroup({ ...initialPartnerGroup });
                    await fetchPartnerGroups.load();
                    setMessage(isEditing ? "取引先グループを更新しました。" : "取引先グループを作成しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`取引先グループの作成または更新に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <PartnerGroupSingleView
                    error={error}
                    message={message}
                    isEditing={isEditing}
                    headerItems={{ handleCreateOrUpdatePartnerGroup, handleCloseModal }}
                    formItems={{ newPartnerGroup, setNewPartnerGroup }}
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