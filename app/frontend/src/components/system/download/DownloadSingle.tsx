import React from "react";
import {useDownloadContext} from "../../../providers/Download.tsx";
import {getKeyBySymbol, getSymbolByValue} from "../../../models/utils.ts";
import {DownloadTarget} from "../../../models/system/download.ts";
import {showErrorMessage} from "../../application/utils.ts";
import {SingleView} from "../../../views/system/download/DownloadSingle.tsx";

export const DownloadSingle: React.FC = () => {
    const {
        setLoading,
        message,
        setMessage,
        error,
        setError,
        selectedTarget,
        setSelectedTarget,
        downloadService,
    } = useDownloadContext();

    const handleDownload = async () => {
        if (!selectedTarget) {
            setError("ダウンロード対象を選択してください。");
            return;
        }
        setLoading(true);
        setMessage("");
        setError("");

        try {
            const condition = { target: selectedTarget };
            const downloadCount = await downloadService.count(condition);
            const isProceed = confirm(`${downloadCount}件ダウンロードします。よろしいですか？`);
            if (!isProceed) return;

            const blob = await downloadService.download(condition);
            const currentDate = new Date().toISOString().split("T")[0];
            const symbol = getSymbolByValue(DownloadTarget, selectedTarget);
            const symbolName = getKeyBySymbol(DownloadTarget, symbol?.toString() as DownloadTarget) ?? "unknown";
            const downloadFileName = `${symbolName}-${currentDate}.csv`;

            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = downloadFileName;
            a.click();
            window.URL.revokeObjectURL(url);

            setMessage(`${symbolName} データをダウンロードしました。`);
        } catch (error: any) {
            showErrorMessage(
                `ダウンロードに失敗しました: ${error?.message}`,
                setError
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <SingleView
            error={error}
            message={message}
            formItems={{
                selectedTarget,
                setSelectedTarget,
            }}
            headerActions={{
                handleDownload,
            }}
        />
    );
}
