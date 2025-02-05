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
        customerCode: { code: { value: "" }, branchNumber: 0 },
        customerType: CustomerEnumType.顧客でない,
        billingCode: { code: { value: "" }, branchNumber: 0 },
        collectionCode: { code: { value: "" }, branchNumber: 0 },
        customerName: { value: { name: "", nameKana: "" } },
        companyRepresentativeCode: "",
        customerRepresentativeName: "",
        customerDepartmentName: "",
        customerAddress: {
            postalCode: { value: "", regionCode: "" },
            prefecture: PrefectureEnumType.東京都,
            address1: "",
            address2: ""
        },
        customerPhoneNumber: { value: "", areaCode: "", localExchange: "", subscriberNumber: "" },
        customerFaxNumber: { value: "", areaCode: "", localExchange: "", subscriberNumber: "" },
        customerEmailAddress: { value: "" },
        invoice: {
            customerBillingCategory: CustomerBillingCategoryEnumType.都度請求,
            closingInvoice1: {
                closingDay: ClosingDateEnumType.末日,
                paymentMonth: PaymentMonthEnumType.当月,
                paymentDay: PaymentDayEnumType.末日,
                paymentMethod: PaymentMethodEnumType.振込
            },
            closingInvoice2: {
                closingDay: ClosingDateEnumType.末日,
                paymentMonth: PaymentMonthEnumType.当月,
                paymentDay: PaymentDayEnumType.末日,
                paymentMethod: PaymentMethodEnumType.振込
            }
        },
        shippings: [],
        checked: false
    };

    const initialShipping: ShippingType = {
        shippingCode: { customerCode: { code: {value: ""}, branchNumber: 0 }, destinationNumber: 0 },
        destinationName: "",
        regionCode: { value: "" },
        shippingAddress: {
            postalCode: { value: "", regionCode: "" },
            prefecture: PrefectureEnumType.東京都,
            address1: "",
            address2: ""
        }
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
