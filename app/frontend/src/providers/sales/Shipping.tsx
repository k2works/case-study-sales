import React, { createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo } from "react";
import { PageNationType, usePageNation } from "../../views/application/PageNation.tsx";
import { ShippingCriteriaType, ShippingType, initialShipping, initialShippingCriteria } from "../../models/sales/shipping";
import { useModal } from "../../components/application/hooks.ts";
import { useMessage } from "../../components/application/Message.tsx";
import { ShippingService, ShippingServiceType } from "../../services/shipping/shipping";
import { showErrorMessage } from "../../components/application/utils.ts";

type ShippingContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: ShippingCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<ShippingCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialShipping: ShippingType;
    shippings: ShippingType[];
    setShippings: Dispatch<SetStateAction<ShippingType[]>>;
    newShipping: ShippingType;
    setNewShipping: Dispatch<SetStateAction<ShippingType>>;
    searchShippingCriteria: ShippingCriteriaType;
    setSearchShippingCriteria: Dispatch<SetStateAction<ShippingCriteriaType>>;
    fetchShippings: { load: (page?: number, criteria?: ShippingCriteriaType) => Promise<void> };
    shippingService: ShippingServiceType;
    ruleCheckModalIsOpen: boolean;
    setRuleCheckModalIsOpen: Dispatch<SetStateAction<boolean>>;
    orderShippingModalIsOpen: boolean;
    setOrderShippingModalIsOpen: Dispatch<SetStateAction<boolean>>;
};

const ShippingContext = createContext<ShippingContextType | undefined>(undefined);

export const useShippingContext = () => {
    const context = useContext(ShippingContext);
    if (!context) {
        throw new Error("useShippingContext must be used within a ShippingProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

// Custom hook for Shipping management
const useShipping = () => {
    const [shippings, setShippings] = useState<ShippingType[]>([]);
    const [newShipping, setNewShipping] = useState<ShippingType>(initialShipping);
    const [searchShippingCriteria, setSearchShippingCriteria] = useState<ShippingCriteriaType>(initialShippingCriteria);
    const shippingService = ShippingService();

    return {
        shippings,
        setShippings,
        newShipping,
        setNewShipping,
        searchShippingCriteria,
        setSearchShippingCriteria,
        shippingService
    };
};

// Custom hook for fetching Shipping
const useFetchShippings = (
    setLoading: Dispatch<SetStateAction<boolean>>,
    setShippings: Dispatch<SetStateAction<ShippingType[]>>,
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>,
    setError: Dispatch<SetStateAction<string | null>>,
    showErrorMessage: (message: string, setError: Dispatch<SetStateAction<string | null>>) => void,
    shippingService: ShippingServiceType
) => {
    const load = async (page?: number, criteria?: ShippingCriteriaType) => {
        setLoading(true);
        try {
            let response;
            if (criteria) {
                response = await shippingService.search(criteria, page);
            } else {
                response = await shippingService.select(page);
            }
            setShippings(response.list);
            setPageNation(response);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`出荷情報の取得に失敗しました: ${errorMessage}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return { load };
};

export const ShippingProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<ShippingCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const { modalIsOpen: ruleCheckModalIsOpen, setModalIsOpen: setRuleCheckModalIsOpen } = useModal();
    const { modalIsOpen: orderShippingModalIsOpen, setModalIsOpen: setOrderShippingModalIsOpen } = useModal();

    const {
        shippings,
        setShippings,
        newShipping,
        setNewShipping,
        searchShippingCriteria,
        setSearchShippingCriteria,
        shippingService
    } = useShipping();

    const fetchShippings = useFetchShippings(
        setLoading,
        setShippings,
        setPageNation,
        setError,
        showErrorMessage,
        shippingService
    );

    const defaultCriteria: ShippingCriteriaType = {};

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
        initialShipping,
        shippings,
        setShippings,
        newShipping,
        setNewShipping,
        searchShippingCriteria,
        setSearchShippingCriteria,
        fetchShippings,
        shippingService,
        ruleCheckModalIsOpen,
        setRuleCheckModalIsOpen,
        orderShippingModalIsOpen,
        setOrderShippingModalIsOpen
    }), [criteria, defaultCriteria, shippingService, shippings, editId, error, fetchShippings, isEditing, loading, message, modalIsOpen, newShipping, pageNation, searchShippingCriteria, searchModalIsOpen, ruleCheckModalIsOpen, orderShippingModalIsOpen]);

    return (
        <ShippingContext.Provider value={value}>
            {children}
        </ShippingContext.Provider>
    );
};