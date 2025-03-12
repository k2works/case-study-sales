import {PageNationType} from "../../../views/application/PageNation.tsx";

export type RegionType = {
    regionCode: string;
    regionName: string;
    checked: boolean;
}

export type RegionFetchType = {
    list: RegionType[];
} & PageNationType;

export type RegionCriteriaType = {
    regionCode?: string;
    regionName?: string;
}

export const mapToRegionResource = (region: RegionType): RegionType => {
    return {
        ...region
    }
}

export const mapToRegionCriteria = (criteria: RegionCriteriaType): RegionCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(!isEmpty(criteria.regionCode) && {regionCode: criteria.regionCode}),
        ...(!isEmpty(criteria.regionName) && {regionName: criteria.regionName}),
    }
}
