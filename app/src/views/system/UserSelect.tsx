import React from 'react';
import {FaTimes} from 'react-icons/fa';
import {PageNation} from '../application/PageNation.tsx';
import {UserAccountType} from "../../types";

interface UserSelectProps {
    handleSelect: () => void;
}

export const UserSelectView: React.FC<UserSelectProps> = ({handleSelect}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">ユーザー</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleSelect}>選択</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

interface UserCollectionSelectProps {
    users: UserAccountType[];
    handleSelect: (user: UserAccountType) => void;
    handleClose: () => void;
    pageNation: any; // 適切な型を使用してください
    fetchUsers: () => void;
}

export const UserCollectionSelectView: React.FC<UserCollectionSelectProps> = ({
                                                                                  users,
                                                                                  handleSelect,
                                                                                  handleClose,
                                                                                  pageNation,
                                                                                  fetchUsers
                                                                              }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">ユーザー一覧</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {users.map(user => (
                                <li className="collection-object-item" key={user.userId.value}>
                                    <div className="collection-object-item-content" data-id={user.userId.value}>
                                        <div className="collection-object-item-content-details">ユーザーID</div>
                                        <div className="collection-object-item-content-name">{user.userId.value}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={user.userId.value}>
                                        <div className="collection-object-item-content-details">ユーザー名</div>
                                        <div
                                            className="collection-object-item-content-name">{user.name.firstName + " " + user.name.lastName}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={user.userId.value}>
                                        <div className="collection-object-item-content-details">権限</div>
                                        <div className="collection-object-item-content-name">{user.roleName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={user.userId.value}>
                                        <button className="action-button" onClick={() => handleSelect(user)}>選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchUsers}/>
            </div>
        </div>
    );
};
