import {PageNationType} from "../../views/application/PageNation.tsx";

export type WarehouseType = {
    warehouseCode: string;
    warehouseName: string;
    checked?: boolean;
}

export type WarehouseFetchType = {
    list: WarehouseType[];
} & PageNationType;

export type WarehouseCriteriaType = {
    warehouseCode?: string;
    warehouseName?: string;
}

export type WarehouseSearchCriteriaType = {
    warehouseCode?: string;
    warehouseName?: string;
}

export const mapToWarehouseResource = (warehouse: WarehouseType): WarehouseType => {
    return {
        ...warehouse
    };
};

export const mapToWarehouseCriteriaResource = (criteria: WarehouseCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        warehouseCode?: string;
        warehouseName?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.warehouseName) ? {} : { warehouseName: criteria.warehouseName }),
    };

    return resource;
};

export const mapToWarehouseSearchResource = (searchCriteria: WarehouseSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        warehouseCode?: string;
        warehouseName?: string;
    };
    const resource: Resource = {
        ...(isEmpty(searchCriteria.warehouseCode) ? {} : { warehouseCode: searchCriteria.warehouseCode }),
        ...(isEmpty(searchCriteria.warehouseName) ? {} : { warehouseName: searchCriteria.warehouseName }),
    };

    return resource;
};