import {PageNationType} from "../../views/application/PageNation.tsx";

export type LocationNumberType = {
    warehouseCode: string;
    locationNumberCode: string;
    productCode: string;
    checked?: boolean;
}

export type LocationNumberFetchType = {
    list: LocationNumberType[];
} & PageNationType;

export type LocationNumberCriteriaType = {
    warehouseCode?: string;
    locationNumberCode?: string;
    productCode?: string;
}

export type LocationNumberSearchCriteriaType = {
    warehouseCode?: string;
    locationNumberCode?: string;
    productCode?: string;
}

export const mapToLocationNumberResource = (locationNumber: LocationNumberType): LocationNumberType => {
    return {
        ...locationNumber
    };
};

export const mapToLocationNumberCriteriaResource = (criteria: LocationNumberCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        warehouseCode?: string;
        locationNumberCode?: string;
        productCode?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.locationNumberCode) ? {} : { locationNumberCode: criteria.locationNumberCode }),
        ...(isEmpty(criteria.productCode) ? {} : { productCode: criteria.productCode }),
    };

    return resource;
};

export const mapToLocationNumberSearchResource = (searchCriteria: LocationNumberSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        warehouseCode?: string;
        locationNumberCode?: string;
        productCode?: string;
    };
    const resource: Resource = {
        ...(isEmpty(searchCriteria.warehouseCode) ? {} : { warehouseCode: searchCriteria.warehouseCode }),
        ...(isEmpty(searchCriteria.locationNumberCode) ? {} : { locationNumberCode: searchCriteria.locationNumberCode }),
        ...(isEmpty(searchCriteria.productCode) ? {} : { productCode: searchCriteria.productCode }),
    };

    return resource;
};