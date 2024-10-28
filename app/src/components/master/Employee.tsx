import React, {useEffect, useState} from 'react';
import BeatLoader from "react-spinners/BeatLoader";
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {useEmployee} from "./hooks.ts";
import {EmployeeType} from "../../types";
import {usePageNation} from "../../ui/application/PageNation.tsx";
import {SiteLayout} from "../../ui/SiteLayout.tsx";
import {EmployeeCollectionView, SingleEmployeeView} from "../../ui/master/Employee.tsx";

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
                <EmployeeCollectionView
                    error={error}
                    message={message}
                    searchEmployeeCode={searchEmployeeCode}
                    setSearchEmployeeCode={setSearchEmployeeCode}
                    handleSearchEmployee={handleSearchEmployee}
                    handleOpenModal={handleOpenModal}
                    employees={employees}
                    handleDeleteEmployee={handleDeleteEmployee}
                    pageNation={pageNation}
                    fetchEmployees={fetchEmployees}
                />
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
                <SingleEmployeeView
                    error={error}
                    message={message}
                    newEmployee={newEmployee}
                    setNewEmployee={setNewEmployee}
                    isEditing={isEditing}
                    handleCreateOrUpdateEmployee={handleCreateOrUpdateEmployee}
                    handleCloseModal={handleCloseModal}
                />
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
