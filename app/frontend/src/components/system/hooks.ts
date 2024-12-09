import {useState} from "react";
import {UserAccountType} from "../../models";
import {UserService, UserServiceType} from "../../services/user";
import {PageNationType} from "../../views/application/PageNation.tsx";
import {
    ApplicationExecutionHistoryType,
    ApplicationExecutionProcessFlag,
    ApplicationExecutionProcessType, AuditType
} from "../../models/audit.ts";
import {AuditService, AuditServiceType} from "../../services/audit.ts";

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
            const {list, ...pagination} = fetchedUsers;

            setList(list);
            setPageNation(pagination);
            setError("");
        } catch (error: any) {
            showErrorMessage(`${ERROR_MESSAGE} ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return {
        load
    };
};

export const useAudit = () => {
    const initialAudit = {
        id: 0,
        process: {
            processType: ApplicationExecutionProcessType.OTHER,
            name: "",
            code: ""
        },
        type: ApplicationExecutionHistoryType.SYNC,
        processStart: new Date().toISOString(),
        processEnd: new Date().toISOString(),
        processFlag: ApplicationExecutionProcessFlag.NOT_EXECUTED,
        processDetails: null,
        user: {
            userId: { value: "" },
            name: { firstName: "", lastName: "" },
            password: { value: "" },
            roleName: ""
        }
    };

    const [audits, setAudits] = useState<AuditType[]>([]);
    const [newAudit, setNewAudit] = useState<AuditType>(initialAudit);
    const [searchAuditId, setSearchAuditId] = useState<number>(0);
    const auditService = AuditService();

    return {
        initialAudit,
        audits,
        setAudits,
        newAudit,
        setNewAudit,
        searchAuditId,
        setSearchAuditId,
        auditService
    };
};

export const useFetchAudits = (
    setLoading: (loading: boolean) => void,
    setList: (list: AuditType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: AuditServiceType) => {

    const load = async (page: number = 1): Promise<void> => {
        const ERROR_MESSAGE = "監査情報の取得に失敗しました:";
        setLoading(true);

        try {
            const fetchedAudits = await service.select(page);
            const {list, ...pagination} = fetchedAudits;

            setList(list);
            setPageNation(pagination);
            setError("");
        } catch (error: any) {
            showErrorMessage(`${ERROR_MESSAGE} ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return {
        load
    };
};