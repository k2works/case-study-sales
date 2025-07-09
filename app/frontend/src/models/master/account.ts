import {PageNationType} from "../../views/application/PageNation.tsx";
import {toISOStringWithTimezone} from "../../components/application/utils.ts";

// Account types
export const PaymentAccountType = {
    BANK: "BANK",
    CASH: "CASH",
}
export type PaymentAccountType = typeof PaymentAccountType[keyof typeof PaymentAccountType];

// Bank account types
export const BankAccountType = {
    ORDINARY: "ORDINARY",
    CURRENT: "CURRENT",
    SAVINGS: "SAVINGS",
}
export type BankAccountType = typeof BankAccountType[keyof typeof BankAccountType];

// Main account type
export type AccountType = {
    accountCode: string;
    accountName: string;
    startDate: string;
    endDate: string;
    accountNameAfterStart: string;
    accountType: PaymentAccountType;
    accountNumber: string;
    bankAccountType: BankAccountType;
    accountHolder: string;
    departmentCode: string;
    departmentStartDate: string;
    bankCode: string;
    branchCode: string;
    checked?: boolean;
}

// Type for fetching accounts with pagination
export type AccountFetchType = {
    list: AccountType[];
} & PageNationType;

// Type for search criteria
export type AccountCriteriaType = {
    accountCode?: string;
    accountName?: string;
    accountType?: string;
    startDate?: string;
    endDate?: string;
    departmentCode?: string;
}

// Mapping function for converting to resource format
export const mapToAccountResource = (account: AccountType): AccountType => {
    return {
        ...account,
        startDate: toISOStringWithTimezone(new Date(account.startDate)),
        endDate: toISOStringWithTimezone(new Date(account.endDate)),
        departmentStartDate: toISOStringWithTimezone(new Date(account.departmentStartDate))
    };
};

// Mapping function for search criteria
export const mapToAccountCriteriaResource = (criteria: AccountCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        accountCode?: string;
        accountName?: string;
        accountType?: string;
        startDate?: string;
        endDate?: string;
        departmentCode?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.accountCode) ? {} : { accountCode: criteria.accountCode }),
        ...(isEmpty(criteria.accountName) ? {} : { accountName: criteria.accountName }),
        ...(isEmpty(criteria.accountType) ? {} : { accountType: criteria.accountType }),
        ...(isEmpty(criteria.startDate) ? {} : { startDate: toISOStringWithTimezone(new Date(criteria.startDate))}),
        ...(isEmpty(criteria.endDate) ? {} : { endDate: toISOStringWithTimezone(new Date(criteria.endDate))}),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
    };

    return resource;
};
