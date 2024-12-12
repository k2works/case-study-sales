import React, {useEffect, useState} from "react";
import {useMessage} from "../application/Message.tsx";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {useModal} from "../application/hooks.ts";
import {useAudit, useFetchAudits} from "./hooks.ts";
import {AuditType, SearchAuditConditionType} from "../../models/audit.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {AuditCollectionView} from "../../views/system/AuditCollection.tsx";
import {AuditSingleView} from "../../views/system/AuditSingle.tsx";
import {AuditSearchSingleView} from "../../views/system/AuditSearch.tsx";

export const Audit: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError, showErrorMessage} = useMessage();
        const {pageNation, setPageNation, condition, setCondition} = usePageNation<SearchAuditConditionType>();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, setEditId, Modal} = useModal();
        const {modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen,} = useModal();

        const {
            initialAudit,
            audits,
            setAudits,
            newAudit,
            setNewAudit,
            searchAuditCondition,
            setSearchAuditCondition,
            auditService
        } = useAudit();

        const fetchAudits = useFetchAudits(
            setLoading,
            setAudits,
            setPageNation,
            setError,
            showErrorMessage,
            auditService
        );

        useEffect(() => {
            fetchAudits.load().then(() => {
            });
        }, []);

        const handleOpenModal = (audit?: AuditType) => {
            setMessage("");
            setError("");
            if (audit) {
                setNewAudit(audit);
                setIsEditing(true);
            } else {
                setNewAudit(initialAudit);
                setIsEditing(false);
            }
            setModalIsOpen(true);
        };

        const handleCloseModal = () => {
            setError("");
            setModalIsOpen(false);
            setEditId(null);
        };

        const collectionView = () => {
            const handleCloseSearchModal = () => {
                setSearchModalIsOpen(false);
            }

            const handleOpenSearchModal = () => {
                setSearchModalIsOpen(true);
            }

            const handleDeleteAudit = async (auditId: number) => {
                try {
                    if (!window.confirm(`実行履歴ID:${auditId} を削除しますか？`)) return;
                    await auditService.destroy(auditId);
                    await fetchAudits.load(pageNation?.pageNum, searchAuditCondition);
                    setMessage("アプリケーション実行履歴情報を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`アプリケーション実行履歴情報の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckAllAudit = () => {
                const newAudits = audits.map((d) => {
                    return {
                        ...d,
                        checked: !audits.every((d) => d.checked)
                    };
                });
                setAudits(newAudits);
            }
            const handleDeleteCheckedCollection = async () => {
                const checkedAudits = audits.filter((d) => d.checked);
                if (!checkedAudits.length) {
                    setError("削除する履歴を選択してください。");
                    return;
                }

                try {
                    if (!window.confirm("選択した履歴を削除しますか？")) return;
                    await Promise.all(checkedAudits.map((d) => auditService.destroy(d.id)));
                    await fetchAudits.load(pageNation?.pageNum, searchAuditCondition);
                    setMessage("選択した履歴を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`選択した履歴の削除に失敗しました: ${error?.message}`, setError);
                }
            }
            return (
                <>
                    <Modal
                        isOpen={searchModalIsOpen}
                        onRequestClose={handleCloseSearchModal}
                        contentLabel="検索情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {
                            <AuditSearchSingleView
                                condition={searchAuditCondition}
                                setCondition={setSearchAuditCondition}
                                handleSelect={async () => {
                                    if (!searchAuditCondition) {
                                        return;
                                    }
                                    setLoading(true);
                                    try {
                                        const fetchedAudit = await auditService.search(searchAuditCondition);
                                        setAudits(fetchedAudit ? fetchedAudit.list : []);
                                        if (fetchedAudit.list.length === 0) {
                                            showErrorMessage(`検索結果は0件です`, setError);
                                        } else {
                                            setCondition(searchAuditCondition);
                                            setPageNation(fetchedAudit ? fetchedAudit: {});
                                            setMessage("");
                                            setError("");
                                        }
                                    } catch (error: any) {
                                        showErrorMessage(`実行履歴情報の検索に失敗しました: ${error?.message}`, setError);
                                    } finally {
                                        setLoading(false);
                                    }
                                }}
                                handleClose={handleCloseSearchModal}
                            />
                        }
                    </Modal>
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={handleCloseModal}
                        contentLabel="アプリケーション実行履歴情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {singleView()}
                    </Modal>
                    <AuditCollectionView
                        error={error}
                        message={message}
                        searchItems={{
                            searchAuditCondition,
                            setSearchAuditCondition,
                            handleOpenSearchModal,
                        }}
                        menuButtonItems={{
                            handleReloadCollection: fetchAudits.load,
                            handleCheckToggleCollection: handleCheckAllAudit,
                            handleDeleteCheckedCollection,
                        }}
                        collectionItems={{
                            handleOpenModal,
                            audits,
                            handleDeleteAudit,
                            handleCheckAllAudit,
                        }}
                        pageNationItems={{
                            pageNation: pageNation,
                            condition: condition,
                            fetchAudits: fetchAudits.load
                        }}
                    />
                </>
            );
        };

        const singleView = () => {
            return (
                <AuditSingleView
                    error={error}
                    message={message}
                    isEditing={isEditing}
                    handleCloseModal={handleCloseModal}
                    newAudit={newAudit}
                    setNewAudit={setNewAudit}
                />
            );
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    collectionView()
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <Content/>
        </SiteLayout>
    );
};