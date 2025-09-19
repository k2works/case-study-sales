import {useState} from "react";
import {
    InventoryCriteriaType,
    InventoryType,
    InventorySearchCriteriaType
} from "../../../../models/inventory/inventory.ts";
import {InventoryService, InventoryServiceType} from "../../../../services/inventory/inventory.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";

export const useInventory = () => {
    const initialInventory = {
        warehouseCode: "",
        productCode: "",
        lotNumber: "",
        stockCategory: "1",
        qualityCategory: "G",
        actualStockQuantity: 0,
        availableStockQuantity: 0,
        lastShipmentDate: undefined,
        productName: "",
        warehouseName: "",
        checked: false
    };

    const [inventories, setInventories] = useState<InventoryType[]>([]);
    const [newInventory, setNewInventory] = useState<InventoryType>(initialInventory);
    const [searchInventoryCriteria, setSearchInventoryCriteria] = useState<InventorySearchCriteriaType>({});
    const inventoryService = InventoryService();

    return {
        initialInventory,
        inventories,
        newInventory,
        setNewInventory,
        searchInventoryCriteria,
        setSearchInventoryCriteria,
        setInventories,
        inventoryService,
    }
}

export const useFetchInventories = (
    setLoading: (loading: boolean) => void,
    setList: (list: InventoryType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: InventoryServiceType
) => useFetchEntities<InventoryType, InventoryServiceType, InventoryCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "在庫情報の取得に失敗しました:");