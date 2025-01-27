import {useState} from "react";
import {
    ClosingDateEnumType,
    CustomerBillingCategoryEnumType,
    CustomerCriteriaType,
    CustomerEnumType,
    CustomerType,
    MiscellaneousEnumType,
    PartnerCategoryCriteriaType,
    PartnerCategoryItemType,
    PartnerCategoryType,
    PartnerCriteriaType,
    PartnerGroupCriteriaType,
    PartnerGroupType,
    PartnerType,
    PaymentDayEnumType,
    PaymentMethodEnumType,
    PaymentMonthEnumType,
    PrefectureEnumType,
    TradeProhibitedFlagEnumType,
    VendorCriteriaType,
    VendorEnumType,
    VendorType
} from "../../../models/master/partner"; // モデルからインポート
import {useFetchEntities} from "../../application/hooks.ts"; // 共通フックのインポート
import {PageNationType} from "../../../views/application/PageNation.tsx"; // ページネーションモデル
import {PartnerCategoryService, PartnerCategoryServiceType} from "../../../services/master/partner_category.ts";
import {PartnerGroupService, PartnerGroupServiceType} from "../../../services/master/partner_group.ts";
import {PartnerService, PartnerServiceType} from "../../../services/master/partner.ts";
import {CustomerService, CustomerServiceType} from "../../../services/master/customer.ts";
import {VendorService} from "../../../services/master/vendor.ts"; // サービス

export const usePartnerCategory = () => {
    const initialPartnerCategory: PartnerCategoryType = {
        partnerCategoryTypeCode: "",
        partnerCategoryTypeName: "",
        partnerCategoryItems: [],
        checked: false
    };

    const initialPartnerCategoryItem: PartnerCategoryItemType = {
        partnerCategoryTypeCode: "",
        partnerCategoryItemCode: "",
        partnerCategoryItemName: "",
        partnerCategoryAffiliations: [],
    }

    const [partnerCategories, setPartnerCategories] = useState<PartnerCategoryType[]>([]);
    const [newPartnerCategory, setNewPartnerCategory] = useState<PartnerCategoryType>(initialPartnerCategory);
    const [newPartnerCategoryItem, setNewPartnerCategoryItem] = useState<PartnerCategoryItemType>(initialPartnerCategoryItem);
    const [searchPartnerCategoryCriteria, setSearchPartnerCategoryCriteria] = useState<PartnerCategoryCriteriaType>({});
    const partnerCategoryService = PartnerCategoryService();

    return {
        initialPartnerCategory,
        partnerCategories,
        newPartnerCategory,
        newPartnerCategoryItem,
        setNewPartnerCategory,
        setNewPartnerCategoryItem,
        searchPartnerCategoryCriteria,
        setSearchPartnerCategoryCriteria,
        setPartnerCategories,
        partnerCategoryService,
    };
};

export const useFetchPartnerCategories = (
    setLoading: (loading: boolean) => void,
    setList: (list: PartnerCategoryType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: PartnerCategoryServiceType
) => {
    return useFetchEntities<PartnerCategoryType, PartnerCategoryServiceType, PartnerCategoryCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "取引先分類情報の取得に失敗しました:"
    );
};

export const usePartnerGroup = () => {
    const initialPartnerGroup: PartnerGroupType = {
        partnerGroupCode: { value: "" },
        partnerGroupName: "",
        checked: false
    };

    const [partnerGroups, setPartnerGroups] = useState<PartnerGroupType[]>([]);
    const [newPartnerGroup, setNewPartnerGroup] = useState<PartnerGroupType>(initialPartnerGroup);
    const [searchPartnerGroupCriteria, setSearchPartnerGroupCriteria] = useState<PartnerGroupCriteriaType>({});
    const partnerGroupService = PartnerGroupService();

    return {
        initialPartnerGroup,
        partnerGroups,
        newPartnerGroup,
        setNewPartnerGroup,
        searchPartnerGroupCriteria,
        setSearchPartnerGroupCriteria,
        setPartnerGroups,
        partnerGroupService,
    };
};

export const useFetchPartnerGroups = (
    setLoading: (loading: boolean) => void,
    setList: (list: PartnerGroupType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: PartnerGroupServiceType
) => {
    return useFetchEntities<PartnerGroupType, PartnerGroupServiceType, PartnerGroupCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "取引先グループ情報の取得に失敗しました:"
    );
};

export const usePartner = () => {
    const initialPartner: PartnerType = {
        partnerCode: { value: "" },
        partnerName: { name: "", nameKana: "" },
        vendorType: VendorEnumType.仕入先でない,
        address: {
            postalCode: { value: "", regionCode: "" },
            prefecture: PrefectureEnumType.東京都,
            address1: "",
            address2: ""
        },
        tradeProhibitedFlag: TradeProhibitedFlagEnumType.OFF,
        miscellaneousType: MiscellaneousEnumType.対象外,
        partnerGroupCode: { value: "" },
        credit: {
            creditLimit: { amount: 0, currency: "JPY" },
            temporaryCreditIncrease: { amount: 0, currency: "JPY" }
        },
        checked: false
    };

    const [partners, setPartners] = useState<PartnerType[]>([]);
    const [newPartner, setNewPartner] = useState<PartnerType>(initialPartner);
    const [searchPartnerCriteria, setSearchPartnerCriteria] = useState<PartnerCriteriaType>({});
    const partnerService = PartnerService();

    return {
        initialPartner,
        partners,
        newPartner,
        setNewPartner,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
        setPartners,
        partnerService,
    };
};

export const useFetchPartners = (
    setLoading: (loading: boolean) => void,
    setList: (list: PartnerType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: PartnerServiceType
) => {
    return useFetchEntities<PartnerType, PartnerServiceType, PartnerCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "取引先情報の取得に失敗しました:"
    );
};

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

    const [customers, setCustomers] = useState<CustomerType[]>([]);
    const [newCustomer, setNewCustomer] = useState<CustomerType>(initialCustomer);
    const [searchCustomerCriteria, setSearchCustomerCriteria] = useState<CustomerCriteriaType>({});
    const customerService = CustomerService(); // 実装済みのサービス関数

    return {
        initialCustomer,
        customers,
        newCustomer,
        setNewCustomer,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        setCustomers,
        customerService,
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

export const useVendor = () => {
    const initialVendor: VendorType = {
        vendorCode: { code: {value: ""}, branchNumber: 0 },
        vendorName: { value: { name: "", nameKana: "" } },
        vendorContactName: "",
        vendorDepartmentName: "",
        vendorAddress: {
            postalCode: { value: "", regionCode: "" },
            prefecture: PrefectureEnumType.東京都,
            address1: "",
            address2: "",
        },
        vendorPhoneNumber: { value: "", areaCode: "", localExchange: "", subscriberNumber: "" },
        vendorFaxNumber: { value: "", areaCode: "", localExchange: "", subscriberNumber: "" },
        vendorEmailAddress: { value: "" },
        vendorClosingInvoice: {
            closingDay: ClosingDateEnumType.末日,
            paymentMonth: PaymentMonthEnumType.当月,
            paymentDay: PaymentDayEnumType.末日,
            paymentMethod: PaymentMethodEnumType.振込,
        },
        checked: false
    };

    const [vendors, setVendors] = useState<VendorType[]>([]);
    const [newVendor, setNewVendor] = useState<VendorType>(initialVendor);
    const [searchVendorCriteria, setSearchVendorCriteria] = useState<VendorCriteriaType>({});
    const vendorService = VendorService();

    return {
        initialVendor,
        vendors,
        newVendor,
        setNewVendor,
        searchVendorCriteria,
        setSearchVendorCriteria,
        setVendors,
        vendorService,
    };
};

export const useFetchVendors = (
    setLoading: (loading: boolean) => void,
    setList: (list: VendorType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ReturnType<typeof VendorService>
) => {
    return useFetchEntities<VendorType, typeof service, VendorCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "仕入先情報の取得に失敗しました:"
    );
};