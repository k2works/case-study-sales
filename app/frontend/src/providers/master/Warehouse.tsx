import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo} from "react";
import {PageNationType, usePageNation} from "../../views/application/PageNation.tsx";
import {WarehouseCriteriaType, WarehouseType, WarehouseSearchCriteriaType} from "../../models/master/warehouse.ts";
import { useModal } from "../../components/application/hooks.ts";
import { useWarehouse, useFetchWarehouses } from "../../components/master/warehouse/hooks";
import { showErrorMessage } from "../../components/application/utils.ts";
import { useMessage } from "../../components/application/Message.tsx";
import {WarehouseServiceType} from "../../services/master/warehouse.ts";

type WarehouseContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: WarehouseCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<WarehouseCriteriaType | null>>;
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
    searchWarehouseCriteria: WarehouseCriteriaType;
    setSearchWarehouseCriteria: Dispatch<SetStateAction<WarehouseCriteriaType>>;
    fetchWarehouses: { load: (page?: number, criteria?: WarehouseCriteriaType) => Promise<void> };
    warehouseService: WarehouseServiceType;
};

const WarehouseContext = createContext<WarehouseContextType | undefined>(undefined);

export const useWarehouseContext = () => {
    const context = useContext(WarehouseContext);
    if (!context) {
        throw new Error("useWarehouseContext must be used within a WarehouseProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const WarehouseProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<WarehouseCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialWarehouse,
        warehouses,
        setWarehouses,
        newWarehouse,
        setNewWarehouse,
        searchWarehouseCriteria,
        setSearchWarehouseCriteria,
        warehouseService
    } = useWarehouse();
    const fetchWarehouses = useFetchWarehouses(
        setLoading,
        setWarehouses,
        setPageNation,
        setError,
        showErrorMessage,
        warehouseService
    );
    const defaultCriteria: WarehouseCriteriaType = {};

    const value = useMemo(() => ({
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        pageNation,
        setPageNation,
        criteria: criteria ?? defaultCriteria,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        modalIsOpen,
        setModalIsOpen,
        isEditing,
        setIsEditing,
        editId,
        setEditId,
        initialWarehouse,
        warehouses,
        setWarehouses,
        newWarehouse,
        setNewWarehouse,
        searchWarehouseCriteria,
        setSearchWarehouseCriteria,
        fetchWarehouses,
        warehouseService
    }), [criteria, defaultCriteria, warehouseService, warehouses, editId, error, fetchWarehouses, initialWarehouse, isEditing, loading, message, modalIsOpen, newWarehouse, pageNation, searchWarehouseCriteria, searchModalIsOpen, setCriteria, setWarehouses, setEditId, setError, setIsEditing, setMessage, setModalIsOpen, setNewWarehouse, setPageNation, setSearchWarehouseCriteria, setSearchModalIsOpen])

    return (
        <WarehouseContext.Provider value={value}>
            {children}
        </WarehouseContext.Provider>
    );
};