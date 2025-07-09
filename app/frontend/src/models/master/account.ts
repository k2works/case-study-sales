import {PageNationType} from "../../views/application/PageNation.tsx";
import {toISOStringWithTimezone} from "../../components/application/utils.ts";

// Account types
export enum PaymentAccountEnumType  {
    銀行 = "銀行",
    郵便局 = "郵便局",
    農協 = "農協",
    その他 = "その他",
}

export const PaymentAccountTypeValues = {
    [PaymentAccountEnumType.銀行]: 0,
    [PaymentAccountEnumType.郵便局]: 1,
    [PaymentAccountEnumType.農協]: 2,
    [PaymentAccountEnumType.その他]: 3
};

// Bank account types
export enum BankAccountEnumType {
    普通 = "普通",
    当座 = "当座",
    貯蓄 = "貯蓄",
    その他 = "その他",
}

export const BankAccountTypeValue = {
   [BankAccountEnumType.普通]: 0,
   [BankAccountEnumType.当座]: 1,
   [BankAccountEnumType.貯蓄]: 2,
   [BankAccountEnumType.その他]: 3
}

// Main account type
export type AccountType = {
    accountCode: string;
    accountName: string;
    startDate: string;
    endDate: string;
    accountNameAfterStart: string;
    accountType: PaymentAccountEnumType;
    accountNumber: string;
    bankAccountType: BankAccountEnumType;
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
