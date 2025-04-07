import {useState} from "react";
import {UserAccountType} from "../../../../models";
import {UserService, UserServiceType} from "../../../../services/system/user.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";

export const useUser = () => {
    const initialUser = {
        userId: {value: ""},
        name: {firstName: "", lastName: ""},
        password: {value: ""},
        roleName: ""
    };

    const [users, setUsers] = useState<UserAccountType[]>([]);
    const [newUser, setNewUser] = useState<UserAccountType>(initialUser);
    const [searchUserId, setSearchUserId] = useState<string>("");
    const userService = UserService();

    return {
        initialUser,
        users,
        setUsers,
        newUser,
        setNewUser,
        searchUserId,
        setSearchUserId,
        userService
    }
}

export const useFetchUsers = (
    setLoading: (loading: boolean) => void,
    setList: (list: UserAccountType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void, service: UserServiceType) => {
    const load = async (page: number = 1): Promise<void> => {
        const ERROR_MESSAGE = "ユーザー情報の取得に失敗しました:";
        setLoading(true);

        try {
            const fetchedUsers = await service.select(page);
            setList(fetchedUsers.list);
            setPageNation(fetchedUsers);
            setError("");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`${ERROR_MESSAGE} ${errorMessage}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return {
        load
    };
};
