import React, {
    createContext,
    useContext,
    ReactNode,
    useState,
    useMemo,
    Dispatch,
    SetStateAction,
} from "react";
import { PageNationType, usePageNation } from "../../../views/application/PageNation.tsx";
import { useModal } from "../../../components/application/hooks.ts";
import { showErrorMessage } from "../../../components/application/utils.ts";
import { useMessage } from "../../../components/application/Message.tsx";
import { RegionServiceType } from "../../../services/master/region.ts";
import {RegionCriteriaType, RegionType} from "../../../models/master/code";
import {useFetchRegions, useRegion} from "../../../components/master/code/hooks";

// RegionContext の型を定義
type RegionContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: RegionCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<RegionCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialRegion: RegionType;
    regions: RegionType[];
    setRegions: Dispatch<SetStateAction<RegionType[]>>;
    newRegion: RegionType;
    setNewRegion: Dispatch<SetStateAction<RegionType>>;
    searchRegionCriteria: RegionCriteriaType;
    setSearchRegionCriteria: Dispatch<SetStateAction<RegionCriteriaType>>;
    fetchRegions: { load: (page?: number, criteria?: RegionCriteriaType) => Promise<void> };
    regionService: RegionServiceType;
};

// Context を初期化
const RegionContext = createContext<RegionContextType | undefined>(undefined);

// useRegionContext フックを作成
export const useRegionContext = () => {
    const context = useContext(RegionContext);
    if (!context) {
        throw new Error("useRegionContext must be used within a RegionProvider");
    }
    return context;
};

// Provider の Props を定義
type Props = {
    children: ReactNode;
};

// RegionProvider コンポーネントの作成
export const RegionProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<RegionCriteriaType>();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialRegion,
        regions,
        setRegions,
        newRegion,
        setNewRegion,
        searchRegionCriteria,
        setSearchRegionCriteria,
        regionService,
    } = useRegion();

    const fetchRegions = useFetchRegions(
        setLoading,
        setRegions,
        setPageNation,
        setError,
        showErrorMessage,
        regionService
    );

    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

    const defaultCriteria: RegionCriteriaType = {}; // 必要に応じてデフォルト値を設定
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
            initialRegion,
            regions,
            setRegions,
            newRegion,
            setNewRegion,
            searchRegionCriteria,
            setSearchRegionCriteria,
            fetchRegions,
            regionService,
        }),
        [
            loading,
            setLoading,
            message,
            setMessage,
            error,
            setError,
            pageNation,
            setPageNation,
            criteria,
            defaultCriteria,
            setCriteria,
            searchModalIsOpen,
            setSearchModalIsOpen,
            modalIsOpen,
            setModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId,
            initialRegion,
            regions,
            setRegions,
            newRegion,
            setNewRegion,
            searchRegionCriteria,
            setSearchRegionCriteria,
            fetchRegions,
            regionService,
        ]
    );

    return <RegionContext.Provider value={value}>{children}</RegionContext.Provider>;
};