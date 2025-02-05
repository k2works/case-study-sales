import {useState} from "react";
import {
    MiscellaneousEnumType,
    PartnerCriteriaType,
    PartnerType,
    TradeProhibitedFlagEnumType,
    VendorEnumType,
} from "../../../../models/master/partner";
import {useFetchEntities} from "../../../application/hooks.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {PartnerService, PartnerServiceType} from "../../../../services/master/partner.ts";
import {
    PrefectureEnumType
} from "../../../../models";

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
        customers: [],
        vendors: [],
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
