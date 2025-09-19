import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo, useEffect} from "react";
import {PageNationType} from "../../views/application/PageNation.tsx";
import {WarehouseCriteriaType, WarehouseType, WarehouseFetchType, WarehouseSearchCriteriaType} from "../../models/master/warehouse.ts";
import {useMessage} from "../../components/application/Message.tsx";
import {WarehouseService, WarehouseServiceType} from "../../services/master/warehouse.ts";

type WarehouseContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: WarehouseFetchType;
    setPageNation: Dispatch<SetStateAction<WarehouseFetchType>>;
    criteria: WarehouseCriteriaType;
    setCriteria: Dispatch<SetStateAction<WarehouseCriteriaType>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialWarehouse: WarehouseType;
    warehouses: WarehouseType[];
    setWarehouses: Dispatch<SetStateAction<WarehouseType[]>>;
    newWarehouse: WarehouseType;
    setNewWarehouse: Dispatch<SetStateAction<WarehouseType>>;
    searchWarehouseCriteria: WarehouseSearchCriteriaType;
    setSearchWarehouseCriteria: Dispatch<SetStateAction<WarehouseSearchCriteriaType>>;
    searchCriteria: WarehouseSearchCriteriaType;
    setSearchCriteria: Dispatch<SetStateAction<WarehouseSearchCriteriaType>>;
    fetchWarehouses: (pageNumber?: number, searchCriteria?: WarehouseCriteriaType) => Promise<void>;
    warehouseService: ReturnType<typeof WarehouseService>;
};

const WarehouseContext = createContext<WarehouseContextType | undefined>(undefined);

export const WarehouseProvider: React.FC<{children: ReactNode}> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);
    const [warehouses, setWarehouses] = useState<WarehouseType[]>([]);
    const [pageNation, setPageNation] = useState<WarehouseFetchType>({
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

    const initialWarehouse: WarehouseType = {
        warehouseCode: "",
        warehouseName: ""
    };

    const [newWarehouse, setNewWarehouse] = useState<WarehouseType>(initialWarehouse);
    const [searchWarehouseCriteria, setSearchWarehouseCriteria] = useState<WarehouseSearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<WarehouseSearchCriteriaType>({});
    const [criteria, setCriteria] = useState<WarehouseCriteriaType>({});

    const warehouseService = WarehouseService();

    const fetchWarehouses = async (pageNumber: number = 1, searchCriteria?: WarehouseCriteriaType) => {
        setLoading(true);
        try {
            let response: WarehouseFetchType;

            const currentCriteria = searchCriteria || criteria;
            const hasSearchCriteria = Object.values(currentCriteria).some(value =>
                value !== undefined &&
                value !== null &&
                value !== ""
            );

            if (hasSearchCriteria) {
                response = await warehouseService.search(currentCriteria, pageNumber, 20);
            } else {
                response = await warehouseService.select(pageNumber, 20);
            }

            setWarehouses(response.list.map((warehouse: WarehouseType) => ({ ...warehouse, checked: false })));
            setPageNation(response);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const mappedCriteria: WarehouseCriteriaType = {
            ...searchWarehouseCriteria
        };
        setCriteria(mappedCriteria);
    }, [searchWarehouseCriteria]);

    useEffect(() => {
        fetchWarehouses(1);
    }, []);

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
            initialWarehouse,
            newWarehouse,
            setNewWarehouse,
            warehouses,
            setWarehouses,
            searchWarehouseCriteria,
            setSearchWarehouseCriteria,
            searchCriteria,
            setSearchCriteria,
            fetchWarehouses,
            warehouseService,
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, isEditing, editId, newWarehouse, warehouses, searchWarehouseCriteria, searchCriteria, warehouseService]
    );

    return <WarehouseContext.Provider value={value}>{children}</WarehouseContext.Provider>;
};

export const useWarehouseContext = () => {
    const context = useContext(WarehouseContext);
    if (context === undefined) {
        throw new Error("useWarehouseContext must be used within a WarehouseProvider");
    }
    return context;
};