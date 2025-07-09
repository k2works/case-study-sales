import {useState} from "react";
import {
    AccountCriteriaType,
    AccountType,
    PaymentAccountType,
    BankAccountType
} from "../../../../models/master/account.ts";
import {AccountService, AccountServiceType} from "../../../../services/master/account.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";

export const useAccount = () => {
    const initialAccount = {
        accountCode: "",
        accountName: "",
        startDate: "",
        endDate: "",
        accountNameAfterStart: "",
        accountType: PaymentAccountType.BANK,
        accountNumber: "",
        bankAccountType: BankAccountType.ORDINARY,
        accountHolder: "",
        departmentCode: "",
        departmentStartDate: "",
        bankCode: "",
        branchCode: "",
        checked: false
    };

    const [accounts, setAccounts] = useState<AccountType[]>([]);
    const [newAccount, setNewAccount] = useState<AccountType>(initialAccount);
    const [searchAccountCriteria, setSearchAccountCriteria] = useState<AccountCriteriaType>({});
    const accountService = AccountService();

    return {
        initialAccount,
        accounts,
        newAccount,
        setNewAccount,
        searchAccountCriteria,
        setSearchAccountCriteria,
        setAccounts,
        accountService,
    }
}

export const useFetchAccounts = (
    setLoading: (loading: boolean) => void,
    setList: (list: AccountType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: AccountServiceType
) => useFetchEntities<AccountType, AccountServiceType, AccountCriteriaType>(
    setLoading, 
    setList, 
    setPageNation, 
    setError, 
    showErrorMessage, 
    service, 
    "口座情報の取得に失敗しました:"
);