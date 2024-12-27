import {useState} from "react";
import {UserAccountType} from "../../../models";
import {UserService, UserServiceType} from "../../../services/system/user.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";
import {
    ApplicationExecutionHistoryType,
    ApplicationExecutionProcessFlag,
    ApplicationExecutionProcessType, AuditCriteriaType, AuditType
} from "../../../models/audit.ts";
import {AuditService, AuditServiceType} from "../../../services/audit.ts";

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
            processType: ApplicationExecutionProcessType.その他,
            name: "",
            code: ""
        },
        type: ApplicationExecutionHistoryType.同期,
        processStart: new Date().toISOString(),
        processEnd: new Date().toISOString(),
        processFlag: ApplicationExecutionProcessFlag.未実行,
        processDetails: null,
        user: {
            userId: { value: "" },
            name: { firstName: "", lastName: "" },
            password: { value: "" },
            roleName: ""
        },
        checked: false
    };

    const initialSearchAuditCriteria = {}


    const [audits, setAudits] = useState<AuditType[]>([]);
    const [newAudit, setNewAudit] = useState<AuditType>(initialAudit);
    const [searchAuditCriteria, setSearchAuditCriteria] = useState<AuditCriteriaType>(initialSearchAuditCriteria);
    const auditService = AuditService();

    return {
        initialAudit,
        initialSearchAuditCriteria,
        audits,
        setAudits,
        newAudit,
        setNewAudit,
        searchAuditCriteria,
        setSearchAuditCriteria,
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

    const load = async (page: number = 1, criteria?: AuditCriteriaType): Promise<void> => {
        const ERROR_MESSAGE = "アプリケーション実行履歴情報の取得に失敗しました:";
        setLoading(true);

        try {
            const fetchAudits = async (criteria?: AuditCriteriaType, page: number = 1) => {
                return criteria ? service.search(criteria, page) : service.select(page);
            };

            const { list, ...pagination } = await fetchAudits(criteria, page);

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