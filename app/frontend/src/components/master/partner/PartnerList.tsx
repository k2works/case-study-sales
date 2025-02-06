import React, { useEffect, useState } from 'react';
import { showErrorMessage } from "../../application/utils.ts";
import { useMessage } from "../../application/Message.tsx";
import { useModal } from "../../application/hooks.ts";
import { usePageNation } from "../../../views/application/PageNation.tsx";
import Modal from "react-modal";
import {PartnerCriteriaType, PartnerGroupCriteriaType, PartnerType} from "../../../models/master/partner";
import {useFetchPartnerGroups, useFetchPartners, usePartner, usePartnerGroup} from "./hooks";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {PartnerSearchView} from "../../../views/master/partner/PartnerSearch.tsx";
import {PartnerCollectionView} from "../../../views/master/partner/PartnerCollection.tsx";
import {PartnerSingleView} from "../../../views/master/partner/PartnerSingle.tsx";
import {
    PartnerGroupCollectionSelectView,
    PartnerGroupSelectView
} from "../../../views/master/partner/group/PartnerGroupSelect.tsx";

export const PartnerList: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();

        const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PartnerCriteriaType>();
        const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

        const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
        const {
            initialPartner,
            partners,
            setPartners,
            newPartner,
            setNewPartner,
            searchPartnerCriteria,
            setSearchPartnerCriteria,
            partnerService
        } = usePartner();
        const fetchPartners = useFetchPartners(
            setLoading,
            setPartners,
            setPageNation,
            setError,
            showErrorMessage,
            partnerService
        );

        const {
            pageNation: partnerGroupPageNation,
            setPageNation: setPartnerGroupPageNation,
        } = usePageNation<PartnerGroupCriteriaType>();
        const {
            modalIsOpen: partnerGroupModalIsOpen,
            setModalIsOpen: setPartnerGroupModalIsOpen,
        } = useModal();
        const {
            partnerGroups,
            setPartnerGroups,
            partnerGroupService
        } = usePartnerGroup();
        const fetchPartnerGroups = useFetchPartnerGroups(
            setLoading,
            setPartnerGroups,
            setPartnerGroupPageNation,
            setError,
            showErrorMessage,
            partnerGroupService
        );

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchPartners.load(),
                        fetchPartnerGroups.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`取引先情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);


        const modalView = () => {
            const editModal = () => {
                const handleOpenModal = (partner?: PartnerType) => {
                    setMessage("");
                    setError("");
                    if (partner) {
                        setNewPartner(partner);
                        setEditId(partner.partnerCode.value);
                        setIsEditing(true);
                    } else {
                        setNewPartner(initialPartner);
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
                        contentLabel="取引先情報を入力"
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
                        {
                            <>
                                <PartnerSearchView
                                    criteria={searchPartnerCriteria}
                                    setCondition={setSearchPartnerCriteria}
                                    handleSelect={async () => {
                                        if (!searchPartnerCriteria) return;
                                        setLoading(true);
                                        try {
                                            const result = await partnerService.search(searchPartnerCriteria);
                                            setPartners(result ? result.list : []);
                                            if (result.list.length === 0) {
                                                showErrorMessage("検索結果は0件です", setError);
                                            } else {
                                                setCriteria(searchPartnerCriteria);
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

                                {partnerGroupModal().partnerGroupSearchModalView()}
                                <PartnerGroupSelectView
                                    handleSelect={() => setPartnerGroupModalIsOpen(true)}
                                />
                            </>
                        }
                    </Modal>
                );

                return { searchModalView, handleOpenSearchModal, handleCloseSearchModal };
            };

            const partnerGroupModal = () => {
                const partnerGroupModalView = () => {
                    return (
                        <Modal
                            isOpen={partnerGroupModalIsOpen}
                            onRequestClose={() => setPartnerGroupModalIsOpen(false)}
                            contentLabel="取引先グループ情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                <PartnerGroupCollectionSelectView
                                    partnerGroups={partnerGroups}
                                    handleSelect={(partnerGroup) => {
                                        setNewPartner({
                                            ...newPartner,
                                            partnerGroupCode: partnerGroup.partnerGroupCode
                                        })
                                        setPartnerGroupModalIsOpen(false);
                                    }}
                                    handleClose={() => setPartnerGroupModalIsOpen(false)}
                                    pageNation={partnerGroupPageNation}
                                    fetchPartnerGroups={fetchPartnerGroups.load}
                                />
                            }
                        </Modal>
                    );
                };

                const partnerGroupSearchModalView = () => {
                    return (
                        <Modal
                            isOpen={partnerGroupModalIsOpen}
                            onRequestClose={() => setPartnerGroupModalIsOpen(false)}
                            contentLabel="取引先グループ情報を検索"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                <PartnerGroupCollectionSelectView
                                    partnerGroups={partnerGroups}
                                    handleSelect={(partnerGroup) => {
                                        setSearchPartnerCriteria({
                                            ...searchPartnerCriteria,
                                            partnerGroupCode: partnerGroup.partnerGroupCode.value
                                        });
                                        setPartnerGroupModalIsOpen(false);
                                    }}
                                    handleClose={() => setPartnerGroupModalIsOpen(false)}
                                    pageNation={partnerGroupPageNation}
                                    fetchPartnerGroups={fetchPartnerGroups.load}
                                />
                            }
                        </Modal>
                    );
                };

                return {
                    partnerGroupModalView,
                    partnerGroupSearchModalView,
                };
            };

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {searchModal().searchModalView()}
                    {partnerGroupModal().partnerGroupModalView()}
                </>
            );

            return { editModal, searchModal, init };
        };

        const collectionView = () => {
            const { handleOpenModal } = modalView().editModal();
            const { handleOpenSearchModal } = modalView().searchModal();

            const handleDeletePartner = async (partnerCode: string) => {
                try {
                    if (!window.confirm(`取引先コード:${partnerCode} を削除しますか？`)) return;
                    await partnerService.destroy(partnerCode);
                    await fetchPartners.load();
                    setMessage("取引先を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`取引先の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckPartner = (partner: PartnerType) => {
                const newPartners = partners.map((p) => {
                    if (p.partnerCode.value === partner.partnerCode.value) {
                        return { ...p, checked: !p.checked };
                    }
                    return p;
                });
                setPartners(newPartners);
            }

            const handleCheckAllPartners = () => {
                const newPartners = partners.map((p) => {
                    return { ...p, checked: !p.checked };
                });
                setPartners(newPartners);
            }

            const handleDeleteCheckCollection = async () => {
                const checkedPartners = partners.filter((p) => p.checked);
                if (checkedPartners.length === 0) {
                    showErrorMessage("削除する取引先を選択してください", setError);
                    return;
                }
                try {
                    if (!window.confirm("選択した取引先を削除しますか？")) return;
                    await Promise.all(
                        checkedPartners.map((partners) =>
                            partnerService.destroy(partners.partnerCode.value)
                        )
                    );
                    await fetchPartners.load();
                    setMessage("選択した取引先を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`選択した取引先の削除に失敗しました: ${error?.message}`, setError);
                }
            }

            return (
                <PartnerCollectionView
                    error={error}
                    message={message}
                    searchItems={{
                        searchPartnerCriteria,
                        setSearchPartnerCriteria,
                        handleOpenSearchModal,
                    }}
                    headerItems={{
                        handleOpenModal,
                        handleCheckToggleCollection: handleCheckAllPartners,
                        handleDeleteCheckedCollection: handleDeleteCheckCollection,
                    }}
                    collectionItems={{
                        partners,
                        handleOpenModal,
                        handleDeletePartner,
                        handleCheckPartner,
                    }}
                    pageNationItems={{
                        pageNation,
                        fetchPartners: fetchPartners.load,
                        criteria,
                    }}
                />
            );
        };

        const singleView = () => {
            const { handleCloseModal } = modalView().editModal();
            const handleCreateOrUpdatePartner = async () => {
                if (!newPartner.partnerCode.value.trim() || !newPartner.partnerName.name.trim()) {
                    setError("取引先コードと名称は必須項目です。");
                    return;
                }
                try {
                    if (isEditing && editId) {
                        await partnerService.update(newPartner);
                    } else {
                        await partnerService.create(newPartner);
                    }
                    setNewPartner({ ...initialPartner });
                    await fetchPartners.load();
                    setMessage(isEditing ? "取引先を更新しました。" : "取引先を作成しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`取引先の作成または更新に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <>
                    <PartnerSingleView
                        error={error}
                        message={message}
                        isEditing={isEditing}
                        headerItems={{ handleCreateOrUpdatePartner, handleCloseModal }}
                        formItems={{ newPartner, setNewPartner }}
                    />

                    <PartnerGroupSelectView
                        handleSelect={() => setPartnerGroupModalIsOpen(true)}
                    />
                </>
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