import React from "react";
import {RoleNameEnumType, UserAccountType} from "../../models";
import {Message} from "../../components/application/Message.tsx";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";


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
    const [roleType, setRoleType] = React.useState<RoleNameEnumType>(newUser.roleName as RoleNameEnumType);

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="ユーザーID"
                id="userId"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="ユーザーID"
                value={newUser.userId.value}
                onChange={(e) => setNewUser({
                    ...newUser,
                    userId: { value: e.target.value }
                })}
                disabled={isEditing}
            />
            <FormInput
                label="姓"
                id="firstName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="姓"
                value={newUser.name?.firstName || ""}
                onChange={(e) => setNewUser({
                    ...newUser,
                    name: { ...newUser.name, firstName: e.target.value }
                })}
            />
            <FormInput
                label="名"
                id="lastName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="名"
                value={newUser.name?.lastName || ""}
                onChange={(e) => setNewUser({
                    ...newUser,
                    name: { ...newUser.name, lastName: e.target.value }
                })}
            />
            <FormSelect
                label="役割"
                id="roleName"
                className="single-view-content-item-form-item-input"
                value={roleType}
                options={RoleNameEnumType}
                onChange={(e) => {
                    setRoleType(e);
                    setNewUser({ ...newUser, roleName: e })
                }}
            />
            <FormInput
                label="パスワード"
                id="password"
                type="password"
                className="single-view-content-item-form-item-input"
                placeholder="パスワード"
                value={newUser.password?.value || ""}
                onChange={(e) => setNewUser({
                    ...newUser,
                    password: { value: e.target.value }
                })}
            />
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

