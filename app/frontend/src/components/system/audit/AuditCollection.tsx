import React from "react";
import {useAuditContext} from "../../../providers/Audit.tsx";
import {AuditType} from "../../../models/system/audit.ts";
import {showErrorMessage} from "../../application/utils.ts";
import {AuditCollectionView} from "../../../views/system/audit/AuditCollection.tsx";
import {AuditSearchModal} from "./AuditSearchModal.tsx";
import {AuditEditModal} from "./AuditEditModal.tsx";

export const AuditCollection: React.FC = () => {
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
        initialAudit,
        audits,
        setAudits,
        setNewAudit,
        searchAuditCriteria,
        setSearchAuditCriteria,
        fetchAudits,
        auditService,
    } = useAuditContext();

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

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

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

    const handleDeleteAudit = async (auditId: number) => {
        try {
            if (!window.confirm(`実行履歴ID:${auditId} を削除しますか？`)) return;
            await auditService.destroy(auditId);
            await fetchAudits.load(pageNation?.pageNum, searchAuditCriteria);
            setMessage("アプリケーション実行履歴情報を削除しました。");
        } catch (error: any) {
            showErrorMessage(`アプリケーション実行履歴情報の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleDeleteCheckedCollection = async () => {
        const checkedAudits = audits.filter((d) => d.checked);
        if (!checkedAudits.length) {
            setError("削除する履歴を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した履歴を削除しますか？")) return;
            await Promise.all(checkedAudits.map((d) => auditService.destroy(d.id)));
            await fetchAudits.load(pageNation?.pageNum, searchAuditCriteria);
            setMessage("選択した履歴を削除しました。");
        } catch (error: any) {
            showErrorMessage(`選択した履歴の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <AuditSearchModal/>
            <AuditEditModal/>
            <AuditCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchAuditCriteria,
                    setSearchAuditCriteria,
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
                    handleCheckAudit,
                }}
                pageNationItems={{
                    pageNation: pageNation,
                    criteria: criteria,
                    fetchAudits: fetchAudits.load
                }}
            />
        </>
    );
}
