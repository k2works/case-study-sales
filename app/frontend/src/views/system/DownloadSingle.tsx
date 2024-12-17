import {DownloadTarget, getKeyBySymbol} from "../../models/download.ts";
import React from "react";
import {Message} from "../../components/application/Message.tsx";

interface FormProps {
    selectedTarget: DownloadTarget | null;
    setSelectedTarget: React.Dispatch<React.SetStateAction<DownloadTarget | null>>;
}

const Form: React.FC<FormProps> = ({selectedTarget, setSelectedTarget}) => {
    return (
        <div className="single-view-content-item-form">
            <label htmlFor="downloadTarget" className="form-label">
                ダウンロード対象
            </label>
            <select
                id="downloadTarget"
                value={selectedTarget || ""}
                onChange={(e) => setSelectedTarget(e.target.value as DownloadTarget)}
                className="dropdown"
            >
                <option value="" disabled>
                    対象を選択してください
                </option>
                <option value={DownloadTarget.部門}>{getKeyBySymbol(DownloadTarget.部門)}</option>
                <option value={DownloadTarget.社員}>{getKeyBySymbol(DownloadTarget.社員)}</option>
                <option value={DownloadTarget.商品分類}>{getKeyBySymbol(DownloadTarget.商品分類)}</option>
                <option value={DownloadTarget.商品}>{getKeyBySymbol(DownloadTarget.商品)}</option>
            </select>
        </div>
    );
};

interface SingleViewProps {
    error: string | null;
    message: string | null;
    formItems: {
        selectedTarget: DownloadTarget | null;
        setSelectedTarget: React.Dispatch<React.SetStateAction<DownloadTarget | null>>;
    };
    headerActions: {
        handleDownload: () => Promise<void>;
    };
}

export const SingleView: React.FC<SingleViewProps> = ({
                                                          error,
                                                          message,
                                                          formItems,
                                                          headerActions,
                                                      }) => {
    const { selectedTarget, setSelectedTarget } = formItems;
    const { handleDownload } = headerActions;

    return (
        <>

            <div className="single-view-object-container">
                <Message error={error} message={message}/>
                <div className="single-view-header">
                    <div>
                        <div className="single-view-header-item">
                            <h1 className="single-view-title">{"ダウンロード"}</h1>
                            <p className="single-view-subtitle">{""}</p>
                        </div>
                        <div className="single-view-header-item">
                            <div className="button-container">
                                <button className="action-button" onClick={handleDownload} id="download">
                                    {"実行"}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="single-view-container">
                    <div className="single-view-content">
                        <div className="single-view-content-item">
                            <Form
                                selectedTarget={selectedTarget}
                                setSelectedTarget={setSelectedTarget}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};
