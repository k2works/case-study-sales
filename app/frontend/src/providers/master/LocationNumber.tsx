import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo, useEffect} from "react";
import {LocationNumberCriteriaType, LocationNumberType, LocationNumberFetchType, LocationNumberSearchCriteriaType} from "../../models/master/locationnumber.ts";
import {useMessage} from "../../components/application/Message.tsx";
import {LocationNumberService} from "../../services/master/locationnumber.ts";

type LocationNumberContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: LocationNumberFetchType;
    setPageNation: Dispatch<SetStateAction<LocationNumberFetchType>>;
    criteria: LocationNumberCriteriaType;
    setCriteria: Dispatch<SetStateAction<LocationNumberCriteriaType>>;
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
    searchLocationNumberCriteria: LocationNumberSearchCriteriaType;
    setSearchLocationNumberCriteria: Dispatch<SetStateAction<LocationNumberSearchCriteriaType>>;
    searchCriteria: LocationNumberSearchCriteriaType;
    setSearchCriteria: Dispatch<SetStateAction<LocationNumberSearchCriteriaType>>;
    fetchLocationNumbers: (pageNumber?: number, searchCriteria?: LocationNumberCriteriaType) => Promise<void>;
    locationNumberService: ReturnType<typeof LocationNumberService>;
};

const LocationNumberContext = createContext<LocationNumberContextType | undefined>(undefined);

export const LocationNumberProvider: React.FC<{children: ReactNode}> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);
    const [locationNumbers, setLocationNumbers] = useState<LocationNumberType[]>([]);
    const [pageNation, setPageNation] = useState<LocationNumberFetchType>({
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

    const initialLocationNumber: LocationNumberType = {
        warehouseCode: "",
        locationNumberCode: "",
        productCode: ""
    };

    const [newLocationNumber, setNewLocationNumber] = useState<LocationNumberType>(initialLocationNumber);
    const [searchLocationNumberCriteria, setSearchLocationNumberCriteria] = useState<LocationNumberSearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<LocationNumberSearchCriteriaType>({});
    const [criteria, setCriteria] = useState<LocationNumberCriteriaType>({});

    const locationNumberService = LocationNumberService();

    const fetchLocationNumbers = async (pageNumber: number = 1, searchCriteria?: LocationNumberCriteriaType) => {
        setLoading(true);
        try {
            let response: LocationNumberFetchType;

            const currentCriteria = searchCriteria || criteria;
            const hasSearchCriteria = Object.values(currentCriteria).some(value =>
                value !== undefined &&
                value !== null &&
                value !== ""
            );

            if (hasSearchCriteria) {
                response = await locationNumberService.search(currentCriteria, pageNumber, 20);
            } else {
                response = await locationNumberService.select(pageNumber, 20);
            }

            setLocationNumbers(response.list.map((locationNumber: LocationNumberType) => ({ ...locationNumber, checked: false })));
            setPageNation(response);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const mappedCriteria: LocationNumberCriteriaType = {
            ...searchLocationNumberCriteria
        };
        setCriteria(mappedCriteria);
    }, [searchLocationNumberCriteria]);

    useEffect(() => {
        fetchLocationNumbers(1);
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
            initialLocationNumber,
            newLocationNumber,
            setNewLocationNumber,
            locationNumbers,
            setLocationNumbers,
            searchLocationNumberCriteria,
            setSearchLocationNumberCriteria,
            searchCriteria,
            setSearchCriteria,
            fetchLocationNumbers,
            locationNumberService,
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, isEditing, editId, newLocationNumber, locationNumbers, searchLocationNumberCriteria, searchCriteria, locationNumberService]
    );

    return <LocationNumberContext.Provider value={value}>{children}</LocationNumberContext.Provider>;
};

export const useLocationNumberContext = () => {
    const context = useContext(LocationNumberContext);
    if (context === undefined) {
        throw new Error("useLocationNumberContext must be used within a LocationNumberProvider");
    }
    return context;
};