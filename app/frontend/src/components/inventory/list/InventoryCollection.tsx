import React from "react";

export const InventoryCollection: React.FC = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-12">
                    <div className="card">
                        <div className="card-header">
                            <h5 className="card-title">在庫検索</h5>
                        </div>
                        <div className="card-body">
                            <form>
                                <div className="row">
                                    <div className="col-md-3">
                                        <label htmlFor="warehouseCode" className="form-label">倉庫コード</label>
                                        <input type="text" className="form-control" id="warehouseCode" />
                                    </div>
                                    <div className="col-md-3">
                                        <label htmlFor="productCode" className="form-label">商品コード</label>
                                        <input type="text" className="form-control" id="productCode" />
                                    </div>
                                    <div className="col-md-3">
                                        <label htmlFor="lotNumber" className="form-label">ロット番号</label>
                                        <input type="text" className="form-control" id="lotNumber" />
                                    </div>
                                    <div className="col-md-3">
                                        <button type="submit" className="btn btn-primary mt-4">検索</button>
                                    </div>
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
                            <h5 className="card-title">在庫一覧</h5>
                            <div>
                                <button className="btn btn-success me-2">新規登録</button>
                                <button className="btn btn-info">ダウンロード</button>
                            </div>
                        </div>
                        <div className="card-body">
                            <div className="table-responsive">
                                <table className="table table-striped table-hover">
                                    <thead className="table-light">
                                        <tr>
                                            <th>倉庫コード</th>
                                            <th>倉庫名</th>
                                            <th>商品コード</th>
                                            <th>商品名</th>
                                            <th>ロット番号</th>
                                            <th>在庫区分</th>
                                            <th>良品区分</th>
                                            <th>実在庫数</th>
                                            <th>有効在庫数</th>
                                            <th>最終出荷日</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td colSpan={11} className="text-center text-muted">
                                                データがありません。検索条件を設定して検索してください。
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};