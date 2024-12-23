import React, {useState} from "react";
import {useMessage} from "../application/Message.tsx";
import {DownloadService} from "../../services/download.ts";
import {DownloadTarget} from "../../models/download.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {SingleView} from "../../views/system/download/DownloadSingle.tsx";
import {getKeyBySymbol, getSymbolByValue} from "../../models/utils.ts";

export const Download: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError, showErrorMessage} = useMessage();
        const [selectedTarget, setSelectedTarget] = useState<DownloadTarget | null>(null);
        const downloadService = DownloadService();

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
                const symbolName = getKeyBySymbol(DownloadTarget, symbol?.toString() as DownloadTarget) || "unknown";
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

        const singleView = () => {
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
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <>
                        {singleView()}
                    </>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <Content />
        </SiteLayout>
    );
};
