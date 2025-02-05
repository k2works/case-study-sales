import {useState} from "react";
import {
    VendorCriteriaType,
    VendorType
} from "../../../../models/master/partner";
import {useFetchEntities} from "../../../application/hooks.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {VendorService} from "../../../../services/master/vendor.ts";
import {
    ClosingDateEnumType,
    PaymentDayEnumType,
    PaymentMethodEnumType,
    PaymentMonthEnumType,
    PrefectureEnumType
} from "../../../../models";

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