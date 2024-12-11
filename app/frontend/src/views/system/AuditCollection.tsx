import React from "react";
import {PageNation, PageNationType} from "../application/PageNation.tsx";
import {Message} from "../../components/application/Message.tsx";
import {
    AuditType,
    searchAuditCondition
} from "../../models/audit.ts";
import {convertToDateTimeInputFormat} from "../../components/application/utils.ts";

interface SearchInputProps {
    searchAuditCondition: searchAuditCondition;
    setSearchAuditCondition: (value: searchAuditCondition) => void;
    handleSearchAudit: () => void;
}

const SearchInput: React.FC<SearchInputProps> = ({handleSearchAudit}) => {
    return (
        <div className="search-container">
            <div className="single-view-content-item-form">
                <div className="button-container">
                    <button className="action-button" id="search-all" onClick={handleSearchAudit}>
                        検索
                    </button>
                </div>
            </div>
        </div>
    );
};

interface AuditListItemProps {
    audit: AuditType;
    handleOpenModal: (audit?: AuditType) => void;
    handleDeleteAudit: (auditId: number) => void;
    onCheck: (audit: AuditType) => void;
}

const AuditListItem: React.FC<AuditListItemProps> = ({audit, handleOpenModal, handleDeleteAudit, onCheck}) => {
    return (
        <li className="collection-object-item" key={audit.id}>
            <div className="collection-object-item-content" data-id={audit.id}>
                <input type="checkbox" className="collection-object-item-checkbox" checked={audit.checked}
                       onChange={() => onCheck(audit)}/>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">プロセス開始</div>
                <div
                    className="collection-object-item-content-name">{convertToDateTimeInputFormat(audit.processStart)}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">プロセス終了</div>
                <div
                    className="collection-object-item-content-name">{convertToDateTimeInputFormat(audit.processEnd)}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">プロセス名</div>
                <div className="collection-object-item-content-name">{audit.process.name}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">状態</div>
                <div className="collection-object-item-content-name">{audit.processFlag}</div>
            </div>
            <div className="collection-object-item-content">
                <div className="collection-object-item-content-details">タイプ</div>
                <div className="collection-object-item-content-name">{audit.type}</div>
            </div>
            <div className="collection-object-item-actions">
                <button className="action-button" onClick={() => handleOpenModal(audit)} id="edit">編集</button>
            </div>
            <div className="collection-object-item-actions">
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
    handleCheckAllAudit: (audit: AuditType) => void;
}

const AuditList: React.FC<AuditListProps> = ({audits, handleOpenModal, handleDeleteAudit, handleCheckAllAudit}) => {
    return (
        <div className="collection-object-container">
            <ul className="collection-object-list">
                {audits.map(audit => (
                    <AuditListItem
                        key={audit.id}
                        audit={audit}
                        handleOpenModal={handleOpenModal}
                        handleDeleteAudit={handleDeleteAudit}
                        onCheck={handleCheckAllAudit}
                    />
                ))}
            </ul>
        </div>
    );
};

interface AuditCollectionViewProps {
    error: string | null;
    message: string | null;
    searchAuditCondition: searchAuditCondition;
    setSearchAuditCondition: (value: searchAuditCondition) => void;
    handleSearchAudit: () => void;
    handleOpenModal: (audit?: AuditType) => void;
    audits: AuditType[];
    handleDeleteAudit: (auditId: number) => void;
    fetchAudits: () => void;
    handleCheckAllAudit: (audit: AuditType) => void;
    handleCheckToggleCollection: () => void;
    handleDeleteCheckedCollection: () => void;
    pageNation: PageNationType | null;
}

export const AuditCollectionView: React.FC<AuditCollectionViewProps> = ({
    error,
    message,
    searchAuditCondition,
    setSearchAuditCondition,
    handleSearchAudit,
    handleOpenModal,
    audits,
    handleDeleteAudit,
    fetchAudits,
    handleCheckAllAudit,
    handleCheckToggleCollection,
    handleDeleteCheckedCollection,
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
                <SearchInput
                    searchAuditCondition={searchAuditCondition}
                    setSearchAuditCondition={setSearchAuditCondition}
                    handleSearchAudit={handleSearchAudit}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleCheckToggleCollection()} id="checkAll">
                        一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()} id="deleteAll">
                        一括削除
                    </button>
                </div>
                <AuditList
                    audits={audits}
                    handleOpenModal={handleOpenModal}
                    handleDeleteAudit={handleDeleteAudit}
                    handleCheckAllAudit={handleCheckAllAudit}
                />
                <PageNation pageNation={pageNation} callBack={fetchAudits}/>
            </div>
        </div>
    </div>
);