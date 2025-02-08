import React from "react";
import {useAuditContext} from "../../../providers/Audit.tsx";
import {AuditSingleView} from "../../../views/system/audit/AuditSingle.tsx";

export const AuditSingle: React.FC = () => {
    const {
        message,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newAudit,
        setNewAudit,
    } = useAuditContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

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
}