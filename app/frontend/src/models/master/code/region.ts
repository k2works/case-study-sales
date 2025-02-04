import {PageNationType} from "../../../views/application/PageNation.tsx";

export type RegionCodeType = {
    value: string;
}

export type RegionType = {
    regionCode: RegionCodeType;
    regionName: string;
    checked: boolean;
}

export type RegionFetchType = {
    list: RegionType[];
} & PageNationType;

export type RegionResponseType = {
    regionCode: string;
    regionName: string;
}

export type RegionCriteriaType = {
    regionCode?: string;
    regionName?: string;
}

export const mapToRegionResource = (region: RegionType): RegionResponseType => {
    return {
        regionCode: region.regionCode.value,
        regionName: region.regionName
    }
}

export const mapToRegionCriteria = (criteria: RegionCriteriaType): any => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(!isEmpty(criteria.regionCode) && {regionCode: criteria.regionCode}),
        ...(!isEmpty(criteria.regionName) && {regionName: criteria.regionName}),
    }
}