import React, {useEffect, useState} from 'react';
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {useDepartment, useEmployee} from "./hooks.ts";
import {EmployeeType} from "../../models";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {useFetchUsers, useUser} from "../system/hooks.ts";
import {DepartmentCollectionSelectView, DepartmentSelectView} from "../../views/master/DepartmentSelect.tsx";
import {UserCollectionSelectView, UserSelectView} from "../../views/system/UserSelect.tsx";
import {EmployeeCollectionView} from "../../views/master/EmployeeCollection.tsx";
import {EmployeeSingleView} from "../../views/master/EmployeeSingle.tsx";

export const Employee: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {pageNation: departmentPageNation, setPageNation: setDepartmentPageNation} = usePageNation();
        const {pageNation: userPageNation, setPageNation: setUserPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {
            modalIsOpen: departmenModalIsOpen,
            setModalIsOpen: setDepartmentModalIsOpen,
            isEditing: isDepartmentEditing,
            setIsEditing: setDepartmentIsEditing,
            editId: departmentEditId,
            setEditId: setDepartmentEditId
        } = useModal();
        const {
            modalIsOpen: userModalIsOpen,
            setModalIsOpen: setUserModalIsOpen,
            isEditing: isUserEditing,
            setIsEditing: setUserIsEditing,
            editId: userEditId,
            setEditId: setUserEditId
        } = useModal();

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

        const {
            users,
            setUsers,
            userService
        } = useUser();

        const fetchUsers = useFetchUsers(
            setLoading,
            setUsers,
            setUserPageNation,
            setError,
            showErrorMessage,
            userService
        );

        useEffect(() => {
            fetchEmployees().then(() => {
                fetchDepartments().then(() => {
                    fetchUsers.load().then(() => {
                    });
                });
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

        const fetchDepartments = async (page: number = 1) => {
            setLoading(true);
            try {
                const fetchedDepartments = await departmentService.select(page);
                setDepartments(fetchedDepartments.list);
                setDepartmentPageNation({...fetchedDepartments});
                setError("");
            } catch (error: any) {
                showErrorMessage(`部門情報の取得に失敗しました: ${error?.message}`, setError);
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
                <>
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={handleCloseModal}
                        contentLabel="社員情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {singleView()}
                    </Modal>

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
                </>
            )
        };

        const singleView = () => {
            const handleCreateOrUpdateEmployee = async () => {
                const validateEmployee = (): boolean => {
                    if (!newEmployee.empCode.value.trim() || !newEmployee.empName.firstName.trim() || !newEmployee.empName.lastName.trim()) {
                        setError("社員コード、姓、名は必須項目です。");
                        return false;
                    }
                    if (!newEmployee.department) {
                        setError("部門は必須項目です。");
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
                <>
                    <EmployeeSingleView
                        error={error}
                        message={message}
                        newEmployee={newEmployee}
                        setNewEmployee={setNewEmployee}
                        isEditing={isEditing}
                        handleCreateOrUpdateEmployee={handleCreateOrUpdateEmployee}
                        handleCloseModal={handleCloseModal}
                    />

                    <Modal
                        isOpen={departmenModalIsOpen}
                        onRequestClose={() => setDepartmentModalIsOpen(false)}
                        contentLabel="部門情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {
                            <DepartmentCollectionSelectView
                                departments={departments}
                                handleSelect={(department) => {
                                    setNewEmployee({
                                        ...newEmployee,
                                        department: department
                                    });
                                    setDepartmentModalIsOpen(false);
                                }}
                                handleClose={() => setDepartmentModalIsOpen(false)}
                                pageNation={departmentPageNation}
                                fetchDepartments={fetchDepartments}
                            />
                        }
                    </Modal>

                    <DepartmentSelectView
                        handleSelect={() => setDepartmentModalIsOpen(true)}
                    />

                    <Modal
                        isOpen={userModalIsOpen}
                        onRequestClose={() => setUserModalIsOpen(false)}
                        contentLabel="ユーザー情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {
                            <UserCollectionSelectView
                                users={users}
                                handleSelect={(user) => {
                                    setNewEmployee({
                                        ...newEmployee,
                                        user: user
                                    });
                                    setUserModalIsOpen(false);
                                }}
                                handleClose={() => setUserModalIsOpen(false)}
                                pageNation={userPageNation}
                                fetchUsers={fetchUsers.load}
                            />
                        }
                    </Modal>

                    <UserSelectView
                        handleSelect={() => setUserModalIsOpen(true)}
                    />
                </>
            )
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    collectionView()
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
