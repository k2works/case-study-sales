import {useState} from "react";
import {
    LocationNumberCriteriaType,
    LocationNumberType
} from "../../../../../models/master/locationnumber.ts";
import {LocationNumberService, LocationNumberServiceType} from "../../../../../services/master/locationnumber.ts";
import {PageNationType} from "../../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../../application/hooks.ts";

export const useLocationNumber = () => {
    const initialLocationNumber = {
        warehouseCode: "",
        locationNumberCode: "",
        productCode: "",
        checked: false
    };

    const [locationNumbers, setLocationNumbers] = useState<LocationNumberType[]>([]);
    const [newLocationNumber, setNewLocationNumber] = useState<LocationNumberType>(initialLocationNumber);
    const [searchLocationNumberCriteria, setSearchLocationNumberCriteria] = useState<LocationNumberCriteriaType>({});
    const locationNumberService = LocationNumberService();

    return {
        initialLocationNumber,
        locationNumbers,
        newLocationNumber,
        setNewLocationNumber,
        searchLocationNumberCriteria,
        setSearchLocationNumberCriteria,
        setLocationNumbers,
        locationNumberService,
    }
}

export const useFetchLocationNumbers = (
    setLoading: (loading: boolean) => void,
    setList: (list: LocationNumberType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: LocationNumberServiceType
) => useFetchEntities<LocationNumberType, LocationNumberServiceType, LocationNumberCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "棚番情報の取得に失敗しました:");