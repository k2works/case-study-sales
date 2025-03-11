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
        regionCode: region.regionCode,
        regionName: region.regionName,
        checked: region.checked
    }
}

export const mapToRegionCriteria = (criteria: RegionCriteriaType): Record<string, string> => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(!isEmpty(criteria.regionCode) && {regionCode: criteria.regionCode}),
        ...(!isEmpty(criteria.regionName) && {regionName: criteria.regionName}),
    } as Record<string, string>
}
