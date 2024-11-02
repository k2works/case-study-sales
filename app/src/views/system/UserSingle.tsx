import React from "react";
import {UserAccountType} from "../../models";
import {Message} from "../../components/application/Message.tsx";
import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";


interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdate: () => void;
    handleCloseModal: () => void;
}

const Header: React.FC<HeaderProps> = ({
                                           title,
                                           subtitle,
                                           isEditing,
                                           handleCreateOrUpdate,
                                           handleCloseModal
                                       }) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdate}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface UserFormProps {
    isEditing: boolean;
    newUser: UserAccountType;
    setNewUser: React.Dispatch<React.SetStateAction<UserAccountType>>;
}

const Form = ({isEditing, newUser, setNewUser}: UserFormProps) => {
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

interface UserSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    handleCreateOrUpdateUser: () => void;
    handleCloseModal: () => void;
    newUser: UserAccountType;
    setNewUser: React.Dispatch<React.SetStateAction<UserAccountType>>;
}

export const UserSingleView = ({
                                   error,
                                   message,
                                   isEditing,
                                   handleCreateOrUpdateUser,
                                   handleCloseModal,
                                   newUser,
                                   setNewUser
                               }: UserSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="ユーザー"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdate={handleCreateOrUpdateUser}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newUser={newUser}
                        setNewUser={setNewUser}
                    />
                </div>
            </div>
        </div>
    </div>
);

