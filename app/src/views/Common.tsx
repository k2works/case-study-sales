import React from "react";

export const SingleViewHeaderItem: React.FC<{ title: string, subtitle: string }> = ({title, subtitle}) => (
    <div className="single-view-header-item">
        <h1 className="single-view-title">{title}</h1>
        <p className="single-view-subtitle">{subtitle}</p>
    </div>
);

export const SingleViewHeaderActions: React.FC<{
    isEditing: boolean,
    handleCreateOrUpdateUser: () => void,
    handleCloseModal: () => void
}> = ({isEditing, handleCreateOrUpdateUser, handleCloseModal}) => (
    <div className="single-view-header-item">
        <div className="button-container">
            <button className="action-button" onClick={handleCreateOrUpdateUser}>
                {isEditing ? "更新" : "作成"}
            </button>
            <button className="action-button" onClick={handleCloseModal}>キャンセル</button>
        </div>
    </div>
);
