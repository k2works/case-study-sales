import {useState} from "react";
import {
    CustomerBillingCategoryEnumType,
    CustomerCriteriaType,
    CustomerEnumType,
    CustomerType,
    ShippingType,
} from "../../../../models/master/partner";
import {useFetchEntities} from "../../../application/hooks.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {CustomerService, CustomerServiceType} from "../../../../services/master/customer.ts";
import {
    ClosingDateEnumType,
    PaymentDayEnumType,
    PaymentMethodEnumType,
    PaymentMonthEnumType,
    PrefectureEnumType
} from "../../../../models";

export const useCustomer = () => {
    const initialCustomer: CustomerType = {
        customerCode: "",
        customerBranchNumber: 0,
        customerType: CustomerEnumType.顧客でない,
        billingCode: "",
        billingBranchNumber: 0,
        collectionCode: "",
        collectionBranchNumber: 0,
        customerName: "",
        customerNameKana: "",
        companyRepresentativeCode: "",
        customerRepresentativeName: "",
        customerDepartmentName: "",
        customerPostalCode: "",
        customerPrefecture: PrefectureEnumType.東京都,
        customerAddress1: "",
        customerAddress2: "",
        customerPhoneNumber: "",
        customerFaxNumber: "",
        customerEmailAddress: "",
        customerBillingType: CustomerBillingCategoryEnumType.都度請求,
        customerClosingDay1: ClosingDateEnumType.末日,
        customerPaymentMonth1: PaymentMonthEnumType.当月,
        customerPaymentDay1: PaymentDayEnumType.末日,
        customerPaymentMethod1: PaymentMethodEnumType.振込,
        customerClosingDay2: ClosingDateEnumType.末日,
        customerPaymentMonth2: PaymentMonthEnumType.当月,
        customerPaymentDay2: PaymentDayEnumType.末日,
        customerPaymentMethod2: PaymentMethodEnumType.振込,
        shippings: [],
        checked: false
    };

    const initialShipping: ShippingType = {
        customerCode: "",
        customerBranchNumber: 0,
        destinationNumber: 0,
        destinationName: "",
        regionCode: "",
        postalCode: "",
        prefecture: PrefectureEnumType.東京都,
        address1: "",
        address2: ""
    };

    const [customers, setCustomers] = useState<CustomerType[]>([]);
    const [newCustomer, setNewCustomer] = useState<CustomerType>(initialCustomer);
    const [searchCustomerCriteria, setSearchCustomerCriteria] = useState<CustomerCriteriaType>({});
    const customerService = CustomerService(); // 実装済みのサービス関数
    const [newShipping, setNewShipping] = useState<ShippingType>(initialShipping);

    return {
        initialCustomer,
        customers,
        newCustomer,
        setNewCustomer,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        setCustomers,
        customerService,
        newShipping,
        setNewShipping
    };
};

export const useFetchCustomers = (
    setLoading: (loading: boolean) => void,
    setList: (list: CustomerType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: CustomerServiceType
) => {
    return useFetchEntities<CustomerType, CustomerServiceType, CustomerCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "顧客情報の取得に失敗しました:"
    );
};
