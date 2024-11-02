import React from "react";
import {UserAccountType} from "../../models";
import {PageNation, PageNationType} from "../application/PageNation.tsx";
import {Message} from "../../components/application/Message.tsx";

interface SearchInputProps {
    searchUserId: string;
    setSearchUserId: (value: string) => void;
    handleSearchUser: () => void;
}

const SearchInput: React.FC<SearchInputProps> = ({searchUserId, setSearchUserId, handleSearchUser}) => {
    return (
        <div className="search-container">
            <input
                type="text"
                id="search-input"
                placeholder="ユーザーIDで検索"
                value={searchUserId}
                onChange={(e) => setSearchUserId(e.target.value)}
            />
            <button className="action-button" id="search-all" onClick={handleSearchUser}>
                検索
            </button>
        </div>
    );
};

interface UserListItemProps {
    user: UserAccountType;
    handleOpenModal: (user?: UserAccountType) => void;
    handleDeleteUser: (userId: string) => void;
}

const UserListItem: React.FC<UserListItemProps> = ({user, handleOpenModal, handleDeleteUser}) => {
    return (
        <li className="collection-object-item" key={user.userId.value}>
            <div className="collection-object-item-content" data-id={user.userId.value}>
                <div className="collection-object-item-content-details">ユーザーID</div>
                <div className="collection-object-item-content-name">{user.userId.value}</div>
            </div>
            <div className="collection-object-item-content" data-id={user.userId.value}>
                <div className="collection-object-item-content-details">名</div>
                <div className="collection-object-item-content-name">{user.name.firstName}</div>
            </div>
            <div className="collection-object-item-content" data-id={user.userId.value}>
                <div className="collection-object-item-content-details">姓</div>
                <div className="collection-object-item-content-name">{user.name.lastName}</div>
            </div>
            <div className="collection-object-item-content" data-id={user.userId.value}>
                <div className="collection-object-item-content-details">役割</div>
                <div className="collection-object-item-content-name">{user.roleName}</div>
            </div>
            <div className="collection-object-item-actions" data-id={user.userId.value}>
                <button className="action-button" onClick={() => handleOpenModal(user)}>編集</button>
            </div>
            <div className="collection-object-item-actions" data-id={user.userId.value}>
                <button className="action-button" onClick={() => handleDeleteUser(user.userId.value)}>削除</button>
            </div>
        </li>
    );
};

interface UserListProps {
    users: UserAccountType[];
    handleOpenModal: (user?: UserAccountType) => void;
    handleDeleteUser: (userId: string) => void;
}

const UserList: React.FC<UserListProps> = ({users, handleOpenModal, handleDeleteUser}) => {
    return (
        <div className="collection-object-container">
            <ul className="collection-object-list">
                {users.map(user => (
                    <UserListItem
                        key={user.userId.value}
                        user={user}
                        handleOpenModal={handleOpenModal}
                        handleDeleteUser={handleDeleteUser}
                    />
                ))}
            </ul>
        </div>
    );
};

interface UserCollectionViewProps {
    error: string | null;
    message: string | null;
    searchUserId: string;
    setSearchUserId: (value: string) => void;
    handleSearchUser: () => void;
    handleOpenModal: (user?: UserAccountType) => void;
    users: UserAccountType[];
    handleDeleteUser: (userId: string) => void;
    fetchUsers: () => void;
    pageNation: PageNationType | null;
}

export const UserCollectionView = ({
                                       error,
                                       message,
                                       searchUserId,
                                       setSearchUserId,
                                       handleSearchUser,
                                       handleOpenModal,
                                       users,
                                       handleDeleteUser,
                                       fetchUsers,
                                       pageNation
                                   }: UserCollectionViewProps) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">ユーザー</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <SearchInput searchUserId={searchUserId} setSearchUserId={setSearchUserId}
                             handleSearchUser={handleSearchUser}/>
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()}>
                        新規
                    </button>
                </div>
                <UserList users={users} handleOpenModal={handleOpenModal}
                          handleDeleteUser={handleDeleteUser}/>
                <PageNation pageNation={pageNation} callBack={fetchUsers}/>
            </div>
        </div>
    </div>
);

