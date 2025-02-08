import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { useMessage } from "../../components/application/Message.tsx";
import {DownloadTarget} from "../../models/system/download.ts";
import {DownloadService} from "../../services/system/download.ts";

type DownloadContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    selectedTarget: DownloadTarget | null;
    setSelectedTarget: Dispatch<SetStateAction<DownloadTarget | null>>;
    downloadService: ReturnType<typeof DownloadService>;
};

const DownloadContext = createContext<DownloadContextType | undefined>(undefined);

export const useDownloadContext = () => {
    const context = useContext(DownloadContext);
    if (!context) {
        throw new Error("useDownloadContext must be used within a DownloadProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const DownloadProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [selectedTarget, setSelectedTarget] = useState<DownloadTarget | null>(null);
    const downloadService = DownloadService();

    const value = useMemo(
        () => ({
            loading,
            setLoading,
            message,
            setMessage,
            error,
            setError,
            selectedTarget,
            setSelectedTarget,
            downloadService,
        }),
        [loading, message, setMessage, error, setError, selectedTarget, downloadService]
    );

    return <DownloadContext.Provider value={value}>{children}</DownloadContext.Provider>;
};