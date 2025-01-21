import {useState} from "react";
import {RegionService, RegionServiceType} from "../../../services/master/region.ts";
import {RegionType, RegionCriteriaType} from "../../../models";
import {useFetchEntities} from "../../application/hooks.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";

export const useRegion = () => {
    const initialRegion: RegionType = {
        regionCode: {value: ""},
        regionName: "",
        checked: false
    };

    const [regions, setRegions] = useState<RegionType[]>([]);
    const [newRegion, setNewRegion] = useState<RegionType>(initialRegion);
    const [searchRegionCriteria, setSearchRegionCriteria] = useState<RegionCriteriaType>({});

    const regionService = RegionService();

    return {
        initialRegion,
        regions,
        newRegion,
        setNewRegion,
        searchRegionCriteria,
        setSearchRegionCriteria,
        setRegions,
        regionService,
    };
};


export const useFetchRegions = (
    setLoading: (loading: boolean) => void,
    setList: (list: RegionType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: RegionServiceType
) => useFetchEntities<RegionType, RegionServiceType, RegionCriteriaType>(
    setLoading,
    setList,
    setPageNation,
    setError,
    showErrorMessage,
    service,
    "地域情報の取得に失敗しました:"
);