import React from "react";
import {Message} from "../../../components/application/Message.tsx";
import {FormInput, FormTextarea, SingleViewHeaderItem} from "../../Common.tsx";
import {AuditType} from "../../../models/audit.ts";
import {convertToDateTimeInputFormat} from "../../../components/application/utils.ts";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCloseModal: () => void;
}

const Header: React.FC<HeaderProps> = ({
    title,
    subtitle
                                       }) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
    </div>
);

interface AuditFormProps {
    isEditing: boolean;
    newAudit: AuditType;
    setNewAudit: React.Dispatch<React.SetStateAction<AuditType>>;
}

const Form: React.FC<AuditFormProps> = ({newAudit, setNewAudit}) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="ID"
                id="auditId"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newAudit.id || ""}
                onChange={(e) => setNewAudit({
                    ...newAudit,
                    id: parseInt(e.target.value)
                })}
                disabled
            />
            <FormInput
                label="プロセスタイプ"
                id="processType"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newAudit.process.processType}
                onChange={() => {}}
                disabled
            />
            <FormInput
                label="履歴タイプ"
                id="historyType"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newAudit.type}
                onChange={() => {}}
                disabled
            />
            <FormInput
                label="状態"
                id="processFlag"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newAudit.processFlag}
                onChange={() => {}}
                disabled
            />
            <FormInput
                label="プロセス開始"
                id="processStart"
                type="text"
                className="single-view-content-item-form-item-input"
                value={convertToDateTimeInputFormat(newAudit.processStart)}
                onChange={(e) => setNewAudit({
                    ...newAudit,
                    processStart: e.target.value
                })}
                disabled
            />
            <FormInput
                label="プロセス終了"
                id="processEnd"
                type="text"
                className="single-view-content-item-form-item-input"
                value={convertToDateTimeInputFormat(newAudit.processEnd)}
                onChange={(e) => setNewAudit({
                    ...newAudit,
                    processEnd: e.target.value
                })}
                disabled
            />
            <FormTextarea
                label="プロセス詳細"
                id="processDetails"
                className="single-view-content-item-form-item-input"
                value={newAudit.processDetails || ""}
                onChange={(e) =>
                    setNewAudit({
                        ...newAudit,
                        processDetails: e.target.value
                    })
                }
                disabled
            />
            <FormInput
                label="ユーザーID"
                id="errorMessage"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newAudit.user.userId.value || ""}
                onChange={() => {}}
                disabled
            />
        </div>
    );
};

interface AuditSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    handleCloseModal: () => void;
    newAudit: AuditType;
    setNewAudit: React.Dispatch<React.SetStateAction<AuditType>>;
}

export const AuditSingleView: React.FC<AuditSingleViewProps> = ({
    error,
    message,
    isEditing,
    handleCloseModal,
    newAudit,
    setNewAudit
}) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="アプリケーション実行履歴"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newAudit={newAudit}
                        setNewAudit={setNewAudit}
                    />
                </div>
            </div>
        </div>
    </div>
);