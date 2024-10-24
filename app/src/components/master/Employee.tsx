import React, {useEffect, useState} from 'react';
import BeatLoader from "react-spinners/BeatLoader";
import {SiteLayout} from "../application/SiteLayout.tsx";
import Modal from "react-modal";
import {convertToDateInputFormat, showErrorMessage} from "../application/utils.ts";
import {Message, useMessage} from "../application/Message.tsx";
import {PageNation, usePageNation} from "../application/PageNation.tsx";
import {useModal} from "../application/hooks.ts";
import {useEmployee} from "./hooks.ts";
import {EmployeeType} from "../../types"; // カスタムフックのインポート

export const Employee: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {
            initialEmployee,
            employees,
            setEmployees,
            newEmployee,
            setNewEmployee,
            searchEmployeeCode,
            setSearchEmployeeCode,
            employeeService
        } = useEmployee();

        useEffect(() => {
            fetchEmployees().then(() => {
            });
        }, []);

        const fetchEmployees = async (page: number = 1) => {
            setLoading(true);
            try {
                const fetchedEmployees = await employeeService.select(page);
                setEmployees(fetchedEmployees.list);
                setPageNation({...fetchedEmployees});
                setError("");
            } catch (error: any) {
                showErrorMessage(`社員情報の取得に失敗しました: ${error?.message}`, setError);
            } finally {
                setLoading(false);
            }
        };

        const handleOpenModal = (employee?: EmployeeType) => {
            setMessage("");
            setError("");
            if (employee) {
                employee.loginPassword = "";
                setNewEmployee(employee);
                setEditId(employee.empCode.value);
                setIsEditing(true);
            } else {
                setNewEmployee(initialEmployee);
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
            const handleSearchEmployee = async () => {
                if (!searchEmployeeCode.trim()) return;
                setLoading(true);
                try {
                    const fetchedEmployee = await employeeService.find(searchEmployeeCode.trim());
                    setEmployees(fetchedEmployee ? [fetchedEmployee] : []);
                    setMessage("");
                    setError("");
                } catch (error: any) {
                    showErrorMessage(`社員の検索に失敗しました: ${error?.message}`, setError);
                } finally {
                    setLoading(false);
                }
            };

            const handleDeleteEmployee = async (empCode: string) => {
                try {
                    await employeeService.destroy(empCode);
                    await fetchEmployees();
                    setMessage("社員を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`社員の削除に失敗しました: ${error?.message}`, setError);
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
                                <h1 className="single-view-title">社員</h1>
                            </div>
                        </div>
                        <div className="collection-view-content">
                            <div className="search-container">
                                <input type="text" id="search-input" placeholder="社員コードで検索"
                                       value={searchEmployeeCode}
                                       onChange={(e) => setSearchEmployeeCode(e.target.value)}/>
                                <button className="action-button" id="search-all" onClick={handleSearchEmployee}>検索
                                </button>
                            </div>
                            <div className="button-container">
                                <button className="action-button" onClick={() => handleOpenModal()}>新規</button>
                            </div>
                            <div className="collection-object-container">
                                <ul className="collection-object-list">
                                    {employees.map((employee) => (
                                        <li className="collection-object-item" key={employee.empCode.value}>
                                            <div className="collection-object-item-content"
                                                 data-id={employee.empCode.value}>
                                                <div className="collection-object-item-content-details">社員コード
                                                </div>
                                                <div
                                                    className="collection-object-item-content-name">{employee.empCode.value}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={employee.empCode.value}>
                                                <div className="collection-object-item-content-details">名</div>
                                                <div
                                                    className="collection-object-item-content-name">{employee.empName.firstName}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={employee.empCode.value}>
                                                <div className="collection-object-item-content-details">姓</div>
                                                <div
                                                    className="collection-object-item-content-name">{employee.empName.lastName}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={employee.empCode.value}>
                                                <div className="collection-object-item-content-details">電話番号</div>
                                                <div
                                                    className="collection-object-item-content-name">{employee.tel.value}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={employee.empCode.value}>
                                                <div className="collection-object-item-content-details">FAX番号</div>
                                                <div
                                                    className="collection-object-item-content-name">{employee.fax.value}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={employee.empCode.value}>
                                                <div className="collection-object-item-content-details">部門</div>
                                                <div
                                                    className="collection-object-item-content-name">{employee.department?.departmentName}</div>
                                            </div>
                                            <div className="collection-object-item-actions"
                                                 data-id={employee.empCode.value}>
                                                <button className="action-button"
                                                        onClick={() => handleOpenModal(employee)}>編集
                                                </button>
                                            </div>
                                            <div className="collection-object-item-actions"
                                                 data-id={employee.empCode.value}>
                                                <button className="action-button"
                                                        onClick={() => handleDeleteEmployee(employee.empCode.value)}>削除
                                                </button>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                            <PageNation pageNation={pageNation} callBack={fetchEmployees}/>
                        </div>
                    </div>
                </div>
            )
        };

        const singleView = () => {
            const handleCreateOrUpdateEmployee = async () => {
                const validateEmployee = (): boolean => {
                    if (!newEmployee.empCode.value.trim() || !newEmployee.empName.firstName.trim() || !newEmployee.empName.lastName.trim()) {
                        setError("社員コード、姓、名は必須項目です。");
                        return false;
                    }
                    return true;
                };

                if (!validateEmployee()) return;

                try {
                    if (isEditing && editId) {
                        await employeeService.update(newEmployee);
                    } else {
                        await employeeService.create(newEmployee);
                    }
                    setNewEmployee({...initialEmployee});
                    await fetchEmployees();
                    setMessage("社員を保存しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`社員の保存に失敗しました: ${error?.message}`, setError);
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
                                <h1 className="single-view-title">社員</h1>
                                <p className="single-view-subtitle">{isEditing ? "編集" : "新規作成"}</p>
                            </div>
                            <div className="collection-object-item-actions">
                                <div className="button-container">
                                    <button className="action-button"
                                            onClick={handleCreateOrUpdateEmployee}>{isEditing ? "更新" : "作成"}</button>
                                    <button className="action-button" onClick={handleCloseModal}>キャンセル</button>
                                </div>
                            </div>
                        </div>
                        <div className="single-view-content">
                            <div className="single-view-content-item">
                                <div className="single-view-content-item-form">
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">社員コード</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="社員コード"
                                               value={newEmployee.empCode.value}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   empCode: {value: e.target.value}
                                               })}
                                               disabled={isEditing}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">姓</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="姓"
                                               value={newEmployee.empName.firstName}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   empName: {...newEmployee.empName, firstName: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">名</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="名"
                                               value={newEmployee.empName.lastName}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   empName: {...newEmployee.empName, lastName: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">姓カナ</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="姓カナ"
                                               value={newEmployee.empName.firstNameKana}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   empName: {...newEmployee.empName, firstNameKana: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">名カナ</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="名カナ"
                                               value={newEmployee.empName.lastNameKana}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   empName: {...newEmployee.empName, lastNameKana: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">電話番号</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="電話番号"
                                               value={newEmployee.tel.value}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   tel: {...newEmployee.tel, value: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">FAX番号</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="FAX番号"
                                               value={newEmployee.fax.value}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   fax: {...newEmployee.fax, value: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">職種コード</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="職種コード"
                                               value={newEmployee.occuCode.value}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   occuCode: {...newEmployee.occuCode, value: e.target.value}
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">部門コード</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="部門コード"
                                               value={newEmployee.department?.departmentId.deptCode.value}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   department: {
                                                       ...newEmployee.department,
                                                       departmentId: {
                                                           ...newEmployee.department?.departmentId,
                                                           deptCode: {value: e.target.value}
                                                       }
                                                   }
                                               })}/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">開始日</label>
                                        <input
                                            type="date"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="開始日"
                                            value={convertToDateInputFormat(newEmployee.department?.departmentId.departmentStartDate.value)}
                                            onChange={(e) => setNewEmployee({
                                                ...newEmployee,
                                                department: {
                                                    ...newEmployee.department,
                                                    departmentId: {
                                                        ...newEmployee.department?.departmentId,
                                                        departmentStartDate: {value: e.target.value}
                                                    }
                                                }
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">ユーザーID</label>
                                        <input type="text" className="single-view-content-item-form-item-input"
                                               placeholder="ユーザーID"
                                               value={newEmployee.user?.userId.value}
                                               onChange={(e) => setNewEmployee({
                                                   ...newEmployee,
                                                   user: {...newEmployee.user, userId: {value: e.target.value}}
                                               })}/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )
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
