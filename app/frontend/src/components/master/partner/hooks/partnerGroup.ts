import {useState} from "react";
import {
    PartnerGroupCriteriaType,
    PartnerGroupType,
} from "../../../../models/master/partner";
import {useFetchEntities} from "../../../application/hooks.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {PartnerGroupService, PartnerGroupServiceType} from "../../../../services/master/partnerGroup.ts";

export const usePartnerGroup = () => {
    const initialPartnerGroup: PartnerGroupType = {
        partnerGroupCode: "",
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
