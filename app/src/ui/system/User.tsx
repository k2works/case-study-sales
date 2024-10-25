import React from "react";
import {UserAccountType} from "../../types";
import {Message} from "../../components/application/Message.tsx";
import {PageNation} from "../../components/application/PageNation.tsx";

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

interface ButtonProps {
    text: string;
    onClick: () => void;
}

const Button: React.FC<ButtonProps> = ({text, onClick}) => {
    return (
        <button className="action-button" onClick={onClick}>
            {text}
        </button>
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
                                   }: any) => {
    return (
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
                        <Button text="新規" onClick={() => handleOpenModal()}/>
                    </div>
                    <UserList users={users} handleOpenModal={handleOpenModal}
                              handleDeleteUser={handleDeleteUser}/>
                    <PageNation pageNation={pageNation} callBack={fetchUsers}/>
                </div>
            </div>
        </div>
    )
}

interface SingleViewHeaderItemProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateUser: () => void;
    handleCloseModal: () => void;
}

const SingleViewHeaderItem: React.FC<{ title: string, subtitle: string }> = ({title, subtitle}) => (
    <div className="single-view-header-item">
        <h1 className="single-view-title">{title}</h1>
        <p className="single-view-subtitle">{subtitle}</p>
    </div>
);

const SingleViewHeaderActions: React.FC<{
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

const UserViewHeader: React.FC<SingleViewHeaderItemProps> = ({
                                                                 title,
                                                                 isEditing,
                                                                 handleCreateOrUpdateUser,
                                                                 handleCloseModal
                                                             }) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={isEditing ? "編集" : "新規作成"}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateUser}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

const UserForm = ({newUser, setNewUser, isEditing}: {
    newUser: any,
    setNewUser: React.Dispatch<React.SetStateAction<any>>,
    isEditing: boolean
}) => {
    return (
        <div className="single-view-content-item-form">
            <div className="single-view-content-item-form-item">
                <label className="single-view-content-item-form-item-label">ユーザーID</label>
                <input
                    type="text"
                    className="single-view-content-item-form-item-input"
                    placeholder="ユーザーID"
                    value={newUser.userId.value}
                    onChange={(e) => setNewUser({...newUser, userId: {value: e.target.value}})}
                    disabled={isEditing}
                />
            </div>
            {/* 以下の項目は同様にして追加する */}
            <div className="single-view-content-item-form-item">
                <label className="single-view-content-item-form-item-label">姓</label>
                <input
                    type="text"
                    className="single-view-content-item-form-item-input"
                    placeholder="姓"
                    value={newUser.name?.lastName || ""}
                    onChange={(e) => setNewUser({...newUser, name: {...newUser.name, lastName: e.target.value}})}
                />
            </div>
            <div className="single-view-content-item-form-item">
                <label className="single-view-content-item-form-item-label">名</label>
                <input
                    type="text"
                    className="single-view-content-item-form-item-input"
                    placeholder="名"
                    value={newUser.name?.firstName || ""}
                    onChange={(e) => setNewUser({...newUser, name: {...newUser.name, firstName: e.target.value}})}
                />
            </div>
            <div className="single-view-content-item-form-item">
                <label className="single-view-content-item-form-item-label">役割</label>
                <select
                    className="single-view-content-item-form-item"
                    name="roleNameList"
                    id="roleName"
                    value={newUser.roleName}
                    onChange={(e) => setNewUser({...newUser, roleName: e.target.value})}>
                    <option value="">選択してください</option>
                    <option value="USER">ユーザー</option>
                    <option value="ADMIN">管理者</option>
                </select>
            </div>
            <div className="single-view-content-item-form-item">
                <label className="single-view-content-item-form-item-label">パスワード</label>
                <input
                    type="password"
                    className="single-view-content-item-form-item-input"
                    placeholder="パスワード"
                    value={newUser.password?.value || ""}
                    onChange={(e) => setNewUser({...newUser, password: {value: e.target.value}})}
                />
            </div>
        </div>
    );
};

export const UserSingleView = ({
                                   error,
                                   message,
                                   isEditing,
                                   handleCreateOrUpdateUser,
                                   handleCloseModal,
                                   newUser,
                                   setNewUser
                               }: any) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <UserViewHeader
                    title="ユーザー"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdateUser={handleCreateOrUpdateUser}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <UserForm newUser={newUser} setNewUser={setNewUser} isEditing={isEditing}/>
                    </div>
                </div>
            </div>
        </div>
    );
};

