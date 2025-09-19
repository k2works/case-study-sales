import React, { createContext, useContext, useState, useMemo, useEffect, Dispatch, SetStateAction } from "react";
import {
    InventoryType,
    InventoryCriteriaType,
    InventorySearchCriteriaType,
    InventoryFetchType
} from "../../models/inventory/inventory.ts";
import { InventoryService, UploadResultType } from "../../services/inventory/inventory.ts";
import { useMessage } from "../../components/application/Message.tsx";

type Props = {
    children: React.ReactNode;
};

type InventoryContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: InventoryFetchType;
    criteria: InventoryCriteriaType;
    setCriteria: Dispatch<SetStateAction<InventoryCriteriaType>>;
    setPageNation: Dispatch<SetStateAction<InventoryFetchType>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialInventory: InventoryType;
    newInventory: InventoryType;
    setNewInventory: Dispatch<SetStateAction<InventoryType>>;
    inventories: InventoryType[];
    setInventories: Dispatch<SetStateAction<InventoryType[]>>;
    searchInventoryCriteria: InventorySearchCriteriaType;
    setSearchInventoryCriteria: Dispatch<SetStateAction<InventorySearchCriteriaType>>;
    searchCriteria: InventorySearchCriteriaType;
    setSearchCriteria: Dispatch<SetStateAction<InventorySearchCriteriaType>>;
    fetchInventories: (pageNumber?: number, searchCriteria?: InventoryCriteriaType) => Promise<void>;
    inventoryService: ReturnType<typeof InventoryService>;
    uploadModalIsOpen: boolean;
    setUploadModalIsOpen: Dispatch<SetStateAction<boolean>>;
    uploadResults: UploadResultType[];
    setUploadResults: Dispatch<SetStateAction<UploadResultType[]>>;
    uploadInventories: (file: File) => Promise<void>;
};

const InventoryContext = createContext<InventoryContextType | undefined>(undefined);

export const InventoryProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
    const [uploadModalIsOpen, setUploadModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);
    const [inventories, setInventories] = useState<InventoryType[]>([]);
    const [pageNation, setPageNation] = useState<InventoryFetchType>({
        list: [],
        endRow: 0,
        hasNextPage: false,
        hasPreviousPage: false,
        isFirstPage: true,
        isLastPage: true,
        navigateFirstPage: 1,
        navigateLastPage: 1,
        navigatePages: 8,
        navigatepageNums: [],
        nextPage: 0,
        pageNum: 1,
        pageSize: 20,
        pages: 0,
        prePage: 0,
        size: 0,
        startRow: 0,
        total: 0
    });

    const initialInventory: InventoryType = {
        warehouseCode: "",
        productCode: "",
        lotNumber: "",
        stockCategory: "1",
        qualityCategory: "G",
        actualStockQuantity: 0,
        availableStockQuantity: 0,
        lastShipmentDate: undefined,
        productName: "",
        warehouseName: ""
    };

    const [newInventory, setNewInventory] = useState<InventoryType>(initialInventory);
    const [searchInventoryCriteria, setSearchInventoryCriteria] = useState<InventorySearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<InventorySearchCriteriaType>({});
    const [criteria, setCriteria] = useState<InventoryCriteriaType>({});
    const [uploadResults, setUploadResults] = useState<UploadResultType[]>([]);

    const inventoryService = InventoryService();

    const fetchInventories = async (pageNumber: number = 1, searchCriteria?: InventoryCriteriaType) => {
        setLoading(true);
        try {
            let response: InventoryFetchType;

            // 引数で渡された検索条件があるか、または現在の検索条件が設定されているかチェック
            const currentCriteria = searchCriteria || criteria;
            const hasSearchCriteria = Object.values(currentCriteria).some(value =>
                value !== undefined &&
                value !== null &&
                value !== "" &&
                value !== false
            );

            if (hasSearchCriteria) {
                response = await inventoryService.search(currentCriteria, pageNumber, 20);
            } else {
                response = await inventoryService.select(pageNumber, 20);
            }

            setInventories(response.list.map((inventory: InventoryType) => ({ ...inventory, checked: false })));
            setPageNation(response);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        // 検索条件をマッピング
        const mappedCriteria: InventoryCriteriaType = {
            ...searchInventoryCriteria,
            hasStock: searchInventoryCriteria.hasStock === "true",
            isAvailable: searchInventoryCriteria.isAvailable === "true"
        };
        setCriteria(mappedCriteria);
    }, [searchInventoryCriteria]);

    // 初回ロード
    useEffect(() => {
        fetchInventories(1);
    }, []);

    const uploadInventories = async (file: File) => {
        try {
            setLoading(true);
            const results = await inventoryService.upload(file);
            setUploadResults(prev => [...prev, ...results]);
            setMessage("在庫データのアップロードが完了しました。");
        } catch (error: any) {
            setError(`在庫データのアップロードに失敗しました: ${error?.message}`);
            throw error;
        } finally {
            setLoading(false);
        }
    };


    const value = useMemo(
        () => ({
            loading,
            setLoading,
            message,
            setMessage,
            error,
            setError,
            pageNation,
            setPageNation,
            criteria,
            setCriteria,
            modalIsOpen,
            setModalIsOpen,
            searchModalIsOpen,
            setSearchModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId,
            initialInventory,
            newInventory,
            setNewInventory,
            inventories,
            setInventories,
            searchInventoryCriteria,
            setSearchInventoryCriteria,
            searchCriteria,
            setSearchCriteria,
            fetchInventories,
            inventoryService,
            uploadModalIsOpen,
            setUploadModalIsOpen,
            uploadResults,
            setUploadResults,
            uploadInventories,
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, uploadModalIsOpen, isEditing, editId, newInventory, inventories, searchInventoryCriteria, searchCriteria, uploadResults, inventoryService]
    );

    return <InventoryContext.Provider value={value}>{children}</InventoryContext.Provider>;
};

export const useInventoryContext = () => {
    const context = useContext(InventoryContext);
    if (context === undefined) {
        throw new Error("useInventoryContext must be used within a InventoryProvider");
    }
    return context;
};