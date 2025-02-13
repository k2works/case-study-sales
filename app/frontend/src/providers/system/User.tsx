import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import {showErrorMessage} from "../../components/application/utils.ts";
import {PageNationType, usePageNation} from "../../views/application/PageNation.tsx";
import {UserAccountType} from "../../models";
import {UserServiceType} from "../../services/system/user.ts";
import {useMessage} from "../../components/application/Message.tsx";
import {useModal} from "../../components/application/hooks.ts";
import {useFetchUsers, useUser} from "../../components/system/user/hooks";

// Context の型定義を更新
type UserContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    showErrorMessage: typeof showErrorMessage;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialUser: UserAccountType;
    users: UserAccountType[];
    setUsers: Dispatch<SetStateAction<UserAccountType[]>>;
    newUser: UserAccountType;
    setNewUser: Dispatch<SetStateAction<UserAccountType>>;
    searchUserId: string;
    setSearchUserId: Dispatch<SetStateAction<string>>;
    fetchUsers: {
        load: () => Promise<void>;
    };
    userService: UserServiceType;
};

// Context の作成
const UserContext = createContext<UserContextType | undefined>(undefined);

// Context を利用するためのカスタムフック
export const useUserContext = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUserContext must be used within a UserProvider");
    }
    return context;
};

// Provider コンポーネント
type Props = {
    children: ReactNode;
};

export const UserProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);

    const { message, setMessage, error, setError, showErrorMessage } = useMessage();
    const { pageNation, setPageNation } = usePageNation();
    const {
        modalIsOpen,
        setModalIsOpen,
        isEditing,
        setIsEditing,
        editId,
        setEditId,
    } = useModal();
    const {
        initialUser,
        users,
        setUsers,
        newUser,
        setNewUser,
        searchUserId,
        setSearchUserId,
        userService
    } = useUser();

    const fetchUsers = useFetchUsers(
        setLoading,
        setUsers,
        setPageNation,
        setError,
        showErrorMessage,
        userService
    );

    // Context に渡す値を useMemo でキャッシュ
    const value = useMemo(
        () => ({
            loading,
            setLoading,
            message,
            setMessage,
            error,
            setError,
            showErrorMessage,
            pageNation,
            setPageNation,
            modalIsOpen,
            setModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId,
            initialUser,
            users,
            setUsers,
            newUser,
            setNewUser,
            searchUserId,
            setSearchUserId,
            fetchUsers,
            userService
        }),
        [
            loading,
            message,
            setMessage,
            error,
            setError,
            showErrorMessage,
            pageNation,
            setPageNation,
            modalIsOpen,
            setModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId,
            initialUser,
            users,
            setUsers,
            newUser,
            setNewUser,
            searchUserId,
            setSearchUserId,
            fetchUsers,
            userService
        ]
    );

    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    );
};