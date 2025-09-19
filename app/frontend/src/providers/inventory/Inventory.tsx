import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo, useEffect} from "react";
import {PageNationType, usePageNation} from "../../views/application/PageNation.tsx";
import {InventoryCriteriaType, InventoryType, InventorySearchCriteriaType} from "../../models/inventory/inventory.ts";
import { useModal } from "../../components/application/hooks.ts";
import { useInventory, useFetchInventories } from "../../components/inventory/list/hooks";
import { showErrorMessage } from "../../components/application/utils.ts";
import { useMessage } from "../../components/application/Message.tsx";
import {InventoryServiceType, UploadResultType} from "../../services/inventory/inventory.ts";

type InventoryContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: InventoryCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<InventoryCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialInventory: InventoryType;
    inventories: InventoryType[];
    setInventories: Dispatch<SetStateAction<InventoryType[]>>;
    newInventory: InventoryType;
    setNewInventory: Dispatch<SetStateAction<InventoryType>>;
    searchInventoryCriteria: InventorySearchCriteriaType;
    setSearchInventoryCriteria: Dispatch<SetStateAction<InventorySearchCriteriaType>>;
    fetchInventories: { load: (page?: number, criteria?: InventoryCriteriaType) => Promise<void> };
    inventoryService: InventoryServiceType;
    uploadModalIsOpen: boolean;
    setUploadModalIsOpen: Dispatch<SetStateAction<boolean>>;
    uploadResults: UploadResultType[];
    setUploadResults: Dispatch<SetStateAction<UploadResultType[]>>;
    uploadInventories: (file: File) => Promise<void>;
};

const InventoryContext = createContext<InventoryContextType | undefined>(undefined);

export const useInventoryContext = () => {
    const context = useContext(InventoryContext);
    if (!context) {
        throw new Error("useInventoryContext must be used within a InventoryProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const InventoryProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<InventoryCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen: uploadModalIsOpen, setModalIsOpen: setUploadModalIsOpen } = useModal();

    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialInventory,
        inventories,
        setInventories,
        newInventory,
        setNewInventory,
        searchInventoryCriteria,
        setSearchInventoryCriteria,
        inventoryService
    } = useInventory();
    const fetchInventories = useFetchInventories(
        setLoading,
        setInventories,
        setPageNation,
        setError,
        showErrorMessage,
        inventoryService
    );
    const defaultCriteria: InventoryCriteriaType = {};

    const [uploadResults, setUploadResults] = useState<UploadResultType[]>([]);

    // 検索条件の変換用useEffect
    useEffect(() => {
        const mappedCriteria: InventoryCriteriaType = {
            ...searchInventoryCriteria,
            hasStock: searchInventoryCriteria.hasStock === "true",
            isAvailable: searchInventoryCriteria.isAvailable === "true"
        };
        setCriteria(mappedCriteria);
    }, [searchInventoryCriteria, setCriteria]);

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
        initialInventory,
        inventories,
        setInventories,
        newInventory,
        setNewInventory,
        searchInventoryCriteria,
        setSearchInventoryCriteria,
        fetchInventories,
        inventoryService,
        uploadModalIsOpen,
        setUploadModalIsOpen,
        uploadResults,
        setUploadResults,
        uploadInventories,
    }), [criteria, defaultCriteria, inventoryService, inventories, editId, error, fetchInventories, initialInventory, isEditing, loading, message, modalIsOpen, newInventory, pageNation, searchInventoryCriteria, searchModalIsOpen, uploadModalIsOpen, uploadResults, setCriteria, setInventories, setEditId, setError, setIsEditing, setMessage, setModalIsOpen, setNewInventory, setPageNation, setSearchInventoryCriteria, setSearchModalIsOpen, setUploadModalIsOpen, setUploadResults])

    return (
        <InventoryContext.Provider value={value}>
            {children}
        </InventoryContext.Provider>
    );
};