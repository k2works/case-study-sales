import {useEffect, useState} from "react";
import {useMessage} from "../application/Message.tsx";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {useModal} from "../application/hooks.ts";
import {useAudit, useFetchAudits} from "./hooks.ts";
import {AuditType} from "../../models/audit.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {AuditCollectionView} from "../../views/system/AuditCollection.tsx";
import {AuditSingleView} from "../../views/system/AuditSingle.tsx";

export const Audit: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError, showErrorMessage} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, setEditId, Modal} = useModal();

        // 監査情報用のフック
        const {
            initialAudit,
            audits,
            setAudits,
            newAudit,
            setNewAudit,
            searchAuditId,
            setSearchAuditId,
            auditService
        } = useAudit();

        // 監査情報を取得するフック
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
            const handleSearchAudit = async () => {
                if (!searchAuditId) {
                    return;
                }
                setLoading(true);
                try {
                    /**
                    const fetchedAudit = await auditService.find(searchAuditId);
                    setAudits(fetchedAudit ? [fetchedAudit] : []);
                    setMessage("");
                    setError("");
                    **/
                } catch (error: any) {
                    showErrorMessage(`監査情報の検索に失敗しました: ${error?.message}`, setError);
                } finally {
                    setLoading(false);
                }
            };

            const handleDeleteAudit = async (auditId: number) => {
                try {
                    if (!window.confirm(`監査ID:${auditId} を削除しますか？`)) return;
                    await auditService.destroy(auditId);
                    await fetchAudits.load();
                    setMessage("アプリケーション実行履歴情報を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`アプリケーション実行履歴情報の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckAudit = (audit: AuditType) => {
                const newAudit = audits.map((d) => {
                    if (d.id === audit.id) {
                        return {
                            ...d,
                            checked: !d.checked
                        };
                    }
                    return d;
                });
                setAudits(newAudit);
            }

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
                    await fetchAudits.load();
                    setMessage("選択した部門を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`選択した部門の削除に失敗しました: ${error?.message}`, setError);
                }
            }
            return (
                <>
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
                        searchAuditId={searchAuditId}
                        setSearchAuditId={setSearchAuditId}
                        handleSearchAudit={handleSearchAudit}
                        handleOpenModal={handleOpenModal}
                        audits={audits}
                        handleDeleteAudit={handleDeleteAudit}
                        fetchAudits={fetchAudits.load}
                        handleCheckAllAudit={handleCheckAudit}
                        handleCheckToggleCollection={handleCheckAllAudit}
                        handleDeleteCheckedCollection={handleDeleteCheckedCollection}
                        pageNation={pageNation}
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