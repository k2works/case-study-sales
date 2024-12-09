import React from "react";
import {UserAccountType} from "../../models";
import {PageNation, PageNationType} from "../application/PageNation.tsx";
import {Message} from "../../components/application/Message.tsx";
import {AuditType} from "../../models/audit.ts";
import {convertToDateTimeInputFormat} from "../../components/application/utils.ts";

interface SearchInputProps {
    searchAuditId: string;
    setSearchAuditId: (value: number) => void;
    handleSearchAudit: () => void;
}

const SearchInput: React.FC<SearchInputProps> = ({searchAuditId, setSearchAuditId, handleSearchAudit}) => {
    return (
        <div className="search-container">
            <input
                type="text"
                id="search-input"
                placeholder="監査IDで検索"
                value={searchAuditId}
                onChange={(e) => setSearchAuditId(parseInt(e.target.value))}
            />
            <button className="action-button" id="search-all" onClick={handleSearchAudit}>
                検索
            </button>
        </div>
    );
};

interface AuditListItemProps {
    audit: AuditType;
    handleOpenModal: (audit?: AuditType) => void;
    handleDeleteAudit: (auditId: number) => void;
}

const AuditListItem: React.FC<AuditListItemProps> = ({audit, handleOpenModal, handleDeleteAudit}) => {
    return (
        <li className="collection-object-item" key={audit.id}>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">プロセス開始</div>
                <div className="collection-object-item-content-name">{convertToDateTimeInputFormat(audit.processStart)}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">プロセス終了</div>
                <div className="collection-object-item-content-name">{convertToDateTimeInputFormat(audit.processEnd)}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">プロセス名</div>
                <div className="collection-object-item-content-name">{audit.process.name}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">タイプ</div>
                <div className="collection-object-item-content-name">{audit.type}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">ユーザーID</div>
                <div className="collection-object-item-content-name">{audit.user.userId.value}</div>
            </div>
            <div className="collection-object-item-actions">
                <button className="action-button" onClick={() => handleOpenModal(audit)} id="edit">編集</button>
                <button className="action-button" onClick={() => handleDeleteAudit(parseInt(audit.id.toString()))}
                        id="delete">削除
                </button>
            </div>
        </li>
    );
};

interface AuditListProps {
    audits: AuditType[];
    handleOpenModal: (audit?: AuditType) => void;
    handleDeleteAudit: (auditId: number) => void;
}

const AuditList: React.FC<AuditListProps> = ({audits, handleOpenModal, handleDeleteAudit}) => {
    return (
        <div className="collection-object-container">
            <ul className="collection-object-list">
                {audits.map(audit => (
                    <AuditListItem
                        key={audit.id}
                        audit={audit}
                        handleOpenModal={handleOpenModal}
                        handleDeleteAudit={handleDeleteAudit}
                    />
                ))}
            </ul>
        </div>
    );
};

interface AuditCollectionViewProps {
    error: string | null;
    message: string | null;
    searchAuditId: number;
    setSearchAuditId: (value: number) => void;
    handleSearchAudit: () => void;
    handleOpenModal: (audit?: AuditType) => void;
    audits: AuditType[];
    handleDeleteAudit: (auditId: number) => void;
    fetchAudits: () => void;
    pageNation: PageNationType | null;
}

export const AuditCollectionView: React.FC<AuditCollectionViewProps> = ({
    error,
    message,
    searchAuditId,
    setSearchAuditId,
    handleSearchAudit,
    handleOpenModal,
    audits,
    handleDeleteAudit,
    fetchAudits,
    pageNation
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">アプリケーション実行履歴</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <SearchInput searchAuditId={searchAuditId.toString()} setSearchAuditId={setSearchAuditId}
                             handleSearchAudit={handleSearchAudit}/>
                <AuditList audits={audits} handleOpenModal={handleOpenModal} handleDeleteAudit={handleDeleteAudit}/>
                <PageNation pageNation={pageNation} callBack={fetchAudits}/>
            </div>
        </div>
    </div>
);