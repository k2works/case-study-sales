import React from "react";
import {useAuditContext} from "../../../providers/Audit.tsx";
import Modal from "react-modal";
import {AuditSearchSingleView} from "../../../views/system/audit/AuditSearch.tsx";
import {showErrorMessage} from "../../application/utils.ts";

export const AuditSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setAudits,
        searchAuditCriteria,
        setSearchAuditCriteria,
        auditService,
    } = useAuditContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    return (
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
                    criteria={searchAuditCriteria}
                    setCondition={setSearchAuditCriteria}
                    handleSelect={async () => {
                        if (!searchAuditCriteria) {
                            return;
                        }
                        setLoading(true);
                        try {
                            const fetchedAudit = await auditService.search(searchAuditCriteria);
                            setAudits(fetchedAudit ? fetchedAudit.list : []);
                            if (fetchedAudit.list.length === 0) {
                                showErrorMessage(`検索結果は0件です`, setError);
                            } else {
                                setCriteria(searchAuditCriteria);
                                setPageNation(fetchedAudit);
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
    )
}