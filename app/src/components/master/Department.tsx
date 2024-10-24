import React, {useEffect, useState} from "react";
import {Message, useMessage} from "../application/Message.tsx";
import {PageNation, usePageNation} from "../application/PageNation.tsx";
import {useModal} from "../application/hooks.ts";
import {useDepartment} from "./hooks.ts";
import {convertToDateInputFormat, showErrorMessage} from "../application/utils.ts";
import {DepartmentIdType, DepartmentType, LowerType, SlitYnType} from "../../types";
import Modal from "react-modal";
import BeatLoader from "react-spinners/BeatLoader";
import {SiteLayout} from "../application/SiteLayout.tsx";

export const Department: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {
            initialDepartment,
            departments,
            setDepartments,
            newDepartment,
            setNewDepartment,
            searchDepartmentId,
            setSearchDepartmentId,
            departmentService
        } = useDepartment();

        useEffect(() => {
            fetchDepartments().then(() => {
            });
        }, []);

        const fetchDepartments = async (page: number = 1) => {
            setLoading(true);
            try {
                const fetchedDepartments = await departmentService.select(page);
                setDepartments(fetchedDepartments.list);
                setPageNation({...fetchedDepartments});
                setError("");
            } catch (error: any) {
                showErrorMessage(`部門情報の取得に失敗しました: ${error?.message}`, setError);
            } finally {
                setLoading(false);
            }
        };

        const handleOpenModal = (department?: DepartmentType) => {
            setMessage("");
            setError("");
            if (department) {
                setNewDepartment(department);
                setEditId(department.departmentId.deptCode.value);
                setIsEditing(true);
            } else {
                setNewDepartment(initialDepartment);
                setIsEditing(false);
            }
            setModalIsOpen(true);
        };

        const handleCloseModal = () => {
            setError("");
            setModalIsOpen(false);
            setEditId(null);
        };

        const collectionView = () => {
            const handleSearchDepartment = async () => {
                if (!searchDepartmentId.deptCode.value.trim() && !searchDepartmentId.departmentStartDate.value.trim()) {
                    return;
                }
                setLoading(true);
                let fetchedDepartment: DepartmentType[] = [];

                try {
                    if (searchDepartmentId.deptCode.value === "") {
                        setError("部門コードは必須項目です。");
                        return;
                    }
                    if (searchDepartmentId.departmentStartDate.value === "") {
                        fetchedDepartment = await departmentService.find(searchDepartmentId.deptCode.value, "9999-12-29T12:00:00+09:00");
                    } else {
                        fetchedDepartment = await departmentService.find(searchDepartmentId.deptCode.value, searchDepartmentId.departmentStartDate.value);
                    }
                    setDepartments(fetchedDepartment ? [...fetchedDepartment] : []);
                    setMessage("");
                    setError("");
                } catch (error: any) {
                    showErrorMessage(`部門の検索に失敗しました: ${error?.message}`, setError);
                } finally {
                    setLoading(false);
                }
            };

            const handleDeleteDepartment = async (departmentId: DepartmentIdType) => {
                try {
                    await departmentService.destroy(departmentId.deptCode.value, departmentId.departmentStartDate.value);
                    await fetchDepartments();
                    setMessage("部門を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`部門の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <div className="collection-view-object-container">
                    <div className="view-message-container" id="message">
                        <Message error={error} message={message}/>
                    </div>
                    <div className="collection-view-container">
                        <div className="collection-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title">部門</h1>
                            </div>
                        </div>
                        <div className="collection-view-content">
                            <div className="search-container">
                                <input type="text" id="search-input" placeholder="部門コードで検索"
                                       value={searchDepartmentId.deptCode.value}
                                       onChange={(e) => setSearchDepartmentId({
                                           ...searchDepartmentId,
                                           deptCode: {value: e.target.value}
                                       })}/>
                                <button className="action-button" id="search-all"
                                        onClick={handleSearchDepartment}>検索
                                </button>
                            </div>
                            <div className="button-container">
                                <button className="action-button" onClick={() => handleOpenModal()}>新規作成</button>
                            </div>
                            <div className="collection-object-container">
                                <ul className="collection-object-list">
                                    {departments.map((department) => (
                                        <li className="collection-object-item"
                                            key={department.departmentId.deptCode.value}>
                                            <div className="collection-object-item-content"
                                                 data-id={department.departmentId.deptCode.value}>
                                                <div className="collection-object-item-content-details">部門コード</div>
                                                <div
                                                    className="collection-object-item-content-name">{department.departmentId.deptCode.value}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={department.departmentId.deptCode.value}>
                                                <div className="collection-object-item-content-details">部門名</div>
                                                <div
                                                    className="collection-object-item-content-name">{department.departmentName}</div>
                                            </div>
                                            <div className="collection-object-item-actions"
                                                 data-id={department.departmentId.deptCode.value}>
                                                <button className="action-button"
                                                        onClick={() => handleOpenModal(department)}>編集
                                                </button>
                                            </div>
                                            <div className="collection-object-item-actions"
                                                 data-id={department.departmentId.deptCode.value}>
                                                <button className="action-button"
                                                        onClick={() => handleDeleteDepartment(department.departmentId)}>削除
                                                </button>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                            <PageNation pageNation={pageNation} callBack={fetchDepartments}/>
                        </div>
                    </div>
                </div>
            );
        };

        const singleView = () => {
            const handleCreateOrUpdateDepartment = async () => {
                const validateDepartment = (): boolean => {
                    if (!newDepartment.departmentId.deptCode.value.trim() || !newDepartment.departmentName.trim()) {
                        setError("部門コード、部門名は必須項目です。");
                        return false;
                    }
                    return true;
                };

                if (!validateDepartment()) {
                    return;
                }

                try {
                    if (isEditing && editId) {
                        await departmentService.update(newDepartment);
                    } else {
                        await departmentService.create(newDepartment);
                    }
                    setNewDepartment({
                        departmentId: {deptCode: {value: ""}, departmentStartDate: {value: ""}},
                        endDate: {value: ""},
                        departmentName: "",
                        layer: 0,
                        path: {value: ""},
                        lowerType: LowerType.NO,
                        slitYn: SlitYnType.NO,
                        employees: []
                    });
                    await fetchDepartments();
                    setMessage("部門を保存しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`部門の保存に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <div className="single-view-object-container">
                    <div className="view-message-container" id="message">
                        <Message error={error} message={message}/>
                    </div>
                    <div className="single-view-container">
                        <div className="single-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title">部門</h1>
                                <p className="single-view-subtitle">{isEditing ? "編集" : "新規作成"}</p>
                            </div>
                            <div className="collection-object-item-actions">
                                <div className="button-container">
                                    <button className="action-button"
                                            onClick={handleCreateOrUpdateDepartment}>{isEditing ? "更新" : "作成"}</button>
                                    <button className="action-button" onClick={handleCloseModal}>キャンセル</button>
                                </div>
                            </div>
                        </div>
                        <div className="single-view-content">
                            <div className="single-view-content-item">
                                <div className="single-view-content-item-form">
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">部門コード</label>
                                        <input
                                            type="text"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="部門コード"
                                            value={newDepartment.departmentId.deptCode.value}
                                            onChange={(e) => setNewDepartment({
                                                ...newDepartment,
                                                departmentId: {
                                                    ...newDepartment.departmentId,
                                                    deptCode: {value: e.target.value}
                                                }
                                            })}
                                            disabled={isEditing}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">部門名</label>
                                        <input
                                            type="text"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="部門名"
                                            value={newDepartment.departmentName}
                                            onChange={(e) => setNewDepartment({
                                                ...newDepartment,
                                                departmentName: e.target.value
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">開始日</label>
                                        <input
                                            type="date"
                                            className="single-view-content-item-form-item-input"
                                            value={convertToDateInputFormat(newDepartment.departmentId.departmentStartDate.value)}
                                            onChange={(e) => setNewDepartment({
                                                ...newDepartment,
                                                departmentId: {
                                                    ...newDepartment.departmentId,
                                                    departmentStartDate: {value: e.target.value}
                                                }
                                            })}
                                            disabled={isEditing}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">終了日</label>
                                        <input
                                            type="date"
                                            className="single-view-content-item-form-item-input"
                                            value={convertToDateInputFormat(newDepartment.endDate.value)}
                                            onChange={(e) => setNewDepartment({
                                                ...newDepartment,
                                                endDate: {value: e.target.value}
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">組織階層</label>
                                        <input
                                            type="number"
                                            className="single-view-content-item-form-item-input"
                                            value={newDepartment.layer}
                                            onChange={(e) => setNewDepartment({
                                                ...newDepartment,
                                                layer: +e.target.value
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">部門パス</label>
                                        <input
                                            type="text"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="部門パス"
                                            value={newDepartment.path.value}
                                            onChange={(e) => setNewDepartment({
                                                ...newDepartment,
                                                path: {value: e.target.value}
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">最下層区分</label>
                                        <div className="single-view-content-item-form-radios">
                                            <label>
                                                <input
                                                    type="radio"
                                                    name="lowerType"
                                                    value="LOWER"
                                                    checked={newDepartment.lowerType === LowerType.YES}
                                                    onChange={(e) => setNewDepartment({
                                                        ...newDepartment,
                                                        lowerType: e.target.value
                                                    })}
                                                />
                                                YES
                                            </label>
                                            <label>
                                                <input
                                                    type="radio"
                                                    name="lowerType"
                                                    value="NOT_LOWER"
                                                    checked={newDepartment.lowerType === LowerType.NO}
                                                    onChange={(e) => setNewDepartment({
                                                        ...newDepartment,
                                                        lowerType: e.target.value
                                                    })}
                                                />
                                                NO
                                            </label>
                                        </div>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">伝票入力可否</label>
                                        <div className="single-view-content-item-form-radios">
                                            <label>
                                                <input
                                                    type="radio"
                                                    name="slitType"
                                                    value="SLIT"
                                                    checked={newDepartment.slitYn === SlitYnType.YES}
                                                    onChange={(e) => setNewDepartment({
                                                        ...newDepartment,
                                                        slitYn: e.target.value
                                                    })}
                                                />
                                                YES
                                            </label>
                                            <label>
                                                <input
                                                    type="radio"
                                                    name="slitType"
                                                    value="NOT_SLIT"
                                                    checked={newDepartment.slitYn === SlitYnType.NO}
                                                    onChange={(e) => setNewDepartment({
                                                        ...newDepartment,
                                                        slitYn: e.target.value
                                                    })}
                                                />
                                                NO
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        };

        const modalView = () => {
            return (
                <Modal
                    isOpen={modalIsOpen}
                    onRequestClose={handleCloseModal}
                    contentLabel="部門情報を入力"
                    className="modal"
                    overlayClassName="modal-overlay"
                    bodyOpenClassName="modal-open"
                >
                    {singleView()}
                </Modal>
            );
        };

        return (
            <>
                {loading ? (
                    <div className="loading">
                        <BeatLoader color="#36D7B7"/>
                    </div>
                ) : (
                    <>
                        {!modalIsOpen && collectionView()}
                        {modalIsOpen && modalView()}
                    </>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <Content/>
        </SiteLayout>
    );
};
