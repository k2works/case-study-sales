import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    AccountCriteriaType,
    AccountFetchType,
    AccountType,
    mapToAccountCriteriaResource,
    mapToAccountResource
} from "../../models/master/account.ts";
import {toISOStringWithTimezone} from "../../components/application/utils.ts";

export interface AccountServiceType {
    select: (page?: number, pageSize?: number) => Promise<AccountFetchType>;
    find: (accountCode: string, startDate: string) => Promise<AccountType[]>;
    create: (account: AccountType) => Promise<void>;
    update: (account: AccountType) => Promise<void>;
    destroy: (accountCode: string, startDate: string) => Promise<void>;
    search: (criteria: AccountCriteriaType, page?: number, pageSize?: number) => Promise<AccountFetchType>;
}

export const AccountService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/payment-accounts`;

    const select = async (page?: number, pageSize?: number): Promise<AccountFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<AccountFetchType>(url);
    };

    const find = async (accountCode: string, startDate: string): Promise<AccountType[]> => {
        const formattedStartDate = toISOStringWithTimezone(new Date(startDate));
        const url = `${endPoint}/${accountCode}/${formattedStartDate}`;
        return await apiUtils.fetchGet<AccountType[]>(url);
    };

    const create = async (account: AccountType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToAccountResource(account));
    };

    const update = async (account: AccountType): Promise<void> => {
        const url = `${endPoint}/${account.accountCode}`;
        await apiUtils.fetchPut<void>(url, mapToAccountResource(account));
    };

    const search = async (criteria: AccountCriteriaType, page?: number, pageSize?: number): Promise<AccountFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<AccountFetchType>(url, mapToAccountCriteriaResource(criteria));
    };

    const destroy = async (accountCode: string, startDate: string): Promise<void> => {
        const formattedStartDate = toISOStringWithTimezone(new Date(startDate));
        const url = `${endPoint}/${accountCode}/${formattedStartDate}`;
        await apiUtils.fetchDelete<void>(url);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search
    };
}