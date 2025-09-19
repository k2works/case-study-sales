import React from "react";

export const InventoryRuleCollection: React.FC = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-12">
                    <div className="card">
                        <div className="card-header">
                            <h5 className="card-title">在庫管理ルール</h5>
                        </div>
                        <div className="card-body">
                            <div className="alert alert-info" role="alert">
                                <h6 className="alert-heading">在庫管理の基本ルール</h6>
                                <hr />
                                <ul className="mb-0">
                                    <li><strong>在庫区分：</strong>1=通常在庫、2=安全在庫、3=廃棄予定</li>
                                    <li><strong>良品区分：</strong>G=良品、B=不良品、R=返品</li>
                                    <li><strong>ロット番号：</strong>英数字とハイフンのみ、最大20文字</li>
                                    <li><strong>実在庫数：</strong>物理的な在庫数量</li>
                                    <li><strong>有効在庫数：</strong>販売可能な在庫数量（実在庫数以下）</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row mt-3">
                <div className="col-12">
                    <div className="card">
                        <div className="card-header">
                            <h5 className="card-title">CSVインポートフォーマット</h5>
                        </div>
                        <div className="card-body">
                            <p className="mb-3">CSVファイルは以下の形式でアップロードしてください：</p>
                            <div className="table-responsive">
                                <table className="table table-bordered table-sm">
                                    <thead className="table-light">
                                        <tr>
                                            <th>列番号</th>
                                            <th>項目名</th>
                                            <th>形式</th>
                                            <th>必須</th>
                                            <th>制約</th>
                                            <th>例</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>1</td>
                                            <td>倉庫コード</td>
                                            <td>文字列</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>最大10文字</td>
                                            <td>WH001</td>
                                        </tr>
                                        <tr>
                                            <td>2</td>
                                            <td>商品コード</td>
                                            <td>文字列</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>最大20文字</td>
                                            <td>PRD001</td>
                                        </tr>
                                        <tr>
                                            <td>3</td>
                                            <td>ロット番号</td>
                                            <td>文字列</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>英数字とハイフンのみ、最大20文字</td>
                                            <td>LOT-2024-01</td>
                                        </tr>
                                        <tr>
                                            <td>4</td>
                                            <td>在庫区分</td>
                                            <td>文字列</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>1桁（1,2,3のいずれか）</td>
                                            <td>1</td>
                                        </tr>
                                        <tr>
                                            <td>5</td>
                                            <td>良品区分</td>
                                            <td>文字列</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>1桁（G,B,Rのいずれか）</td>
                                            <td>G</td>
                                        </tr>
                                        <tr>
                                            <td>6</td>
                                            <td>実在庫数</td>
                                            <td>数値</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>0以上の整数</td>
                                            <td>100</td>
                                        </tr>
                                        <tr>
                                            <td>7</td>
                                            <td>有効在庫数</td>
                                            <td>数値</td>
                                            <td><span className="badge bg-danger">必須</span></td>
                                            <td>0以上、実在庫数以下の整数</td>
                                            <td>90</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div className="mt-3">
                                <h6>CSVサンプル</h6>
                                <div className="bg-light p-2 border rounded">
                                    <code>
                                        WH001,PRD001,LOT-2024-01,1,G,100,90<br/>
                                        WH001,PRD002,LOT-2024-02,1,G,50,45<br/>
                                        WH002,PRD001,LOT-2024-03,2,G,200,200
                                    </code>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row mt-3">
                <div className="col-12">
                    <div className="card">
                        <div className="card-header">
                            <h5 className="card-title">エラーパターン</h5>
                        </div>
                        <div className="card-body">
                            <div className="table-responsive">
                                <table className="table table-striped table-sm">
                                    <thead className="table-light">
                                        <tr>
                                            <th>エラータイプ</th>
                                            <th>説明</th>
                                            <th>対処方法</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><span className="badge bg-warning">フォーマットエラー</span></td>
                                            <td>列数が不正、必須項目が空白</td>
                                            <td>CSVの形式を確認してください</td>
                                        </tr>
                                        <tr>
                                            <td><span className="badge bg-warning">データエラー</span></td>
                                            <td>在庫区分・良品区分の値が不正</td>
                                            <td>指定された値のみ使用してください</td>
                                        </tr>
                                        <tr>
                                            <td><span className="badge bg-warning">ビジネスエラー</span></td>
                                            <td>有効在庫数が実在庫数を超過</td>
                                            <td>有効在庫数を実在庫数以下に修正してください</td>
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