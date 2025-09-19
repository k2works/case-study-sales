import React from "react";

export const InventoryUploadCollection: React.FC = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-12">
                    <div className="card">
                        <div className="card-header">
                            <h5 className="card-title">在庫データ一括登録</h5>
                        </div>
                        <div className="card-body">
                            <div className="alert alert-info" role="alert">
                                <h6 className="alert-heading">ご注意</h6>
                                <ul className="mb-0">
                                    <li>CSVファイルの形式は<a href="#" className="alert-link">ルール</a>タブでご確認ください。</li>
                                    <li>一度に登録できるデータは最大1000件までです。</li>
                                    <li>エラーがある場合は、登録処理は行われません。</li>
                                </ul>
                            </div>

                            <form>
                                <div className="mb-3">
                                    <label htmlFor="csvFile" className="form-label">CSVファイル</label>
                                    <input type="file" className="form-control" id="csvFile" accept=".csv" />
                                    <div className="form-text">
                                        CSVファイルを選択してください。
                                    </div>
                                </div>
                                <div className="d-flex gap-2">
                                    <button type="submit" className="btn btn-primary">
                                        <i className="fas fa-upload me-1"></i>
                                        アップロード
                                    </button>
                                    <button type="button" className="btn btn-outline-secondary">
                                        <i className="fas fa-download me-1"></i>
                                        テンプレートダウンロード
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row mt-3">
                <div className="col-12">
                    <div className="card">
                        <div className="card-header d-flex justify-content-between">
                            <h5 className="card-title">処理結果</h5>
                            <button className="btn btn-sm btn-outline-danger" disabled>
                                <i className="fas fa-trash me-1"></i>
                                エラーをクリア
                            </button>
                        </div>
                        <div className="card-body">
                            <div className="alert alert-secondary" role="alert">
                                ファイルをアップロードすると、処理結果がここに表示されます。
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};