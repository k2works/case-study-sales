import {useState} from "react";
import {
    WarehouseCriteriaType,
    WarehouseType
} from "../../../../models/master/warehouse.ts";
import {WarehouseService, WarehouseServiceType} from "../../../../services/master/warehouse.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";

export const useWarehouse = () => {
    const initialWarehouse = {
        warehouseCode: "",
        warehouseName: "",
        checked: false
    };

    const [warehouses, setWarehouses] = useState<WarehouseType[]>([]);
    const [newWarehouse, setNewWarehouse] = useState<WarehouseType>(initialWarehouse);
    const [searchWarehouseCriteria, setSearchWarehouseCriteria] = useState<WarehouseCriteriaType>({});
    const warehouseService = WarehouseService();

    return {
        initialWarehouse,
        warehouses,
        newWarehouse,
        setNewWarehouse,
        searchWarehouseCriteria,
        setSearchWarehouseCriteria,
        setWarehouses,
        warehouseService,
    }
}

export const useFetchWarehouses = (
    setLoading: (loading: boolean) => void,
    setList: (list: WarehouseType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: WarehouseServiceType
) => useFetchEntities<WarehouseType, WarehouseServiceType, WarehouseCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "倉庫情報の取得に失敗しました:");