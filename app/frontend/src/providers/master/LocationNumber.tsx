import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo} from "react";
import {PageNationType, usePageNation} from "../../views/application/PageNation.tsx";
import {LocationNumberCriteriaType, LocationNumberType, LocationNumberSearchCriteriaType} from "../../models/master/locationnumber.ts";
import { useModal } from "../../components/application/hooks.ts";
import { useLocationNumber, useFetchLocationNumbers } from "../../components/master/inventory/locationnumber/hooks";
import { showErrorMessage } from "../../components/application/utils.ts";
import { useMessage } from "../../components/application/Message.tsx";
import {LocationNumberServiceType} from "../../services/master/locationnumber.ts";

type LocationNumberContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: LocationNumberCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<LocationNumberCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialLocationNumber: LocationNumberType;
    locationNumbers: LocationNumberType[];
    setLocationNumbers: Dispatch<SetStateAction<LocationNumberType[]>>;
    newLocationNumber: LocationNumberType;
    setNewLocationNumber: Dispatch<SetStateAction<LocationNumberType>>;
    searchLocationNumberCriteria: LocationNumberCriteriaType;
    setSearchLocationNumberCriteria: Dispatch<SetStateAction<LocationNumberCriteriaType>>;
    fetchLocationNumbers: { load: (page?: number, criteria?: LocationNumberCriteriaType) => Promise<void> };
    locationNumberService: LocationNumberServiceType;
};

const LocationNumberContext = createContext<LocationNumberContextType | undefined>(undefined);

export const useLocationNumberContext = () => {
    const context = useContext(LocationNumberContext);
    if (!context) {
        throw new Error("useLocationNumberContext must be used within a LocationNumberProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const LocationNumberProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<LocationNumberCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialLocationNumber,
        locationNumbers,
        setLocationNumbers,
        newLocationNumber,
        setNewLocationNumber,
        searchLocationNumberCriteria,
        setSearchLocationNumberCriteria,
        locationNumberService
    } = useLocationNumber();
    const fetchLocationNumbers = useFetchLocationNumbers(
        setLoading,
        setLocationNumbers,
        setPageNation,
        setError,
        showErrorMessage,
        locationNumberService
    );
    const defaultCriteria: LocationNumberCriteriaType = {};

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
        initialLocationNumber,
        locationNumbers,
        setLocationNumbers,
        newLocationNumber,
        setNewLocationNumber,
        searchLocationNumberCriteria,
        setSearchLocationNumberCriteria,
        fetchLocationNumbers,
        locationNumberService
    }), [criteria, defaultCriteria, locationNumberService, locationNumbers, editId, error, fetchLocationNumbers, initialLocationNumber, isEditing, loading, message, modalIsOpen, newLocationNumber, pageNation, searchLocationNumberCriteria, searchModalIsOpen, setCriteria, setLocationNumbers, setEditId, setError, setIsEditing, setMessage, setModalIsOpen, setNewLocationNumber, setPageNation, setSearchLocationNumberCriteria, setSearchModalIsOpen])

    return (
        <LocationNumberContext.Provider value={value}>
            {children}
        </LocationNumberContext.Provider>
    );
};