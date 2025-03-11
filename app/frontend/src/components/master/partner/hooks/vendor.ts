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
        vendorCode: "",
        vendorBranchNumber: 0,
        vendorName: "",
        vendorNameKana: "",
        vendorContactName: "",
        vendorDepartmentName: "",
        vendorPostalCode: "",
        vendorPrefecture: PrefectureEnumType.東京都,
        vendorAddress1: "",
        vendorAddress2: "",
        vendorPhoneNumber: "",
        vendorFaxNumber: "",
        vendorEmailAddress: "",
        vendorClosingDate: ClosingDateEnumType.末日,
        vendorPaymentMonth: PaymentMonthEnumType.当月,
        vendorPaymentDate: PaymentDayEnumType.末日,
        vendorPaymentMethod: PaymentMethodEnumType.振込,
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
