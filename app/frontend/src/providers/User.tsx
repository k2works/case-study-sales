import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import {UserAccountType} from "../models";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import {UserServiceType} from "../services/system/user.ts";
import {useFetchUsers, useUser} from "../components/system/user/hooks";

type UserContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    users: UserAccountType[];
    setUsers: Dispatch<SetStateAction<UserAccountType[]>>;
    fetchUsers: {
        load: () => Promise<void>;
    };
    userService: UserServiceType;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

export const useUserContext = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUserContext must be used within a UserProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const UserProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation: userPageNation, setPageNation: setUserPageNation } = usePageNation();
    const { modalIsOpen: userModalIsOpen, setModalIsOpen: setUserModalIsOpen } = useModal();
    const { users, setUsers, userService } = useUser();
    const fetchUsers = useFetchUsers(
        setLoading,
        setUsers,
        setUserPageNation,
        setError,
        showErrorMessage,
        userService
    );
    const value = useMemo(() => ({
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        pageNation: userPageNation,
        setPageNation: setUserPageNation,
        modalIsOpen: userModalIsOpen,
        setModalIsOpen: setUserModalIsOpen,
        users,
        setUsers,
        fetchUsers,
        userService
    }), [loading, message, setMessage, error, setError, userPageNation, setUserPageNation, userModalIsOpen, setUserModalIsOpen, users, setUsers, fetchUsers, userService]);

    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    );
};