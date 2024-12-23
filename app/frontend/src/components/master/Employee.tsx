import React, {useEffect, useState} from 'react';
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {useDepartment, useEmployee, useFetchDepartments, useFetchEmployees} from "./hooks.ts";
import {EmployeeType} from "../../models";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {useFetchUsers, useUser} from "../system/hooks.ts";
import {DepartmentCollectionSelectView, DepartmentSelectView} from "../../views/master/department/DepartmentSelect.tsx";
import {UserCollectionSelectView, UserSelectView} from "../../views/system/user/UserSelect.tsx";
import {EmployeeCollectionView} from "../../views/master/employee/EmployeeCollection.tsx";
import {EmployeeSingleView} from "../../views/master/employee/EmployeeSingle.tsx";

export const Employee: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {pageNation: departmentPageNation, setPageNation: setDepartmentPageNation} = usePageNation();
        const {pageNation: userPageNation, setPageNation: setUserPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {
            modalIsOpen: departmentModalIsOpen,
            setModalIsOpen: setDepartmentModalIsOpen
        } = useModal();
        const {
            modalIsOpen: userModalIsOpen,
            setModalIsOpen: setUserModalIsOpen
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

        const fetchEmployees = useFetchEmployees(
            setLoading,
            setEmployees,
            setPageNation,
            setError,
            showErrorMessage,
            employeeService
        );

        const {
            departments,
            setDepartments,
            departmentService
        } = useDepartment();

        const fetchDepartments = useFetchDepartments(
            setLoading,
            setDepartments,
            setDepartmentPageNation,
            setError,
            showErrorMessage,
            departmentService
        );

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
            fetchEmployees.load().then(() => {
                fetchDepartments.load().then(() => {
                    fetchUsers.load().then(() => {
                    });
                });
            });
        }, []);

        const modalView = () => {
            const editModal = () => {
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

                const editModalView = () => {
                    return(
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

                                {departmentModal().departmentModalView()}
                                <DepartmentSelectView
                                    handleSelect={() => setDepartmentModalIsOpen(true)}
                                />

                                {employeeModal().employeeModalView()}
                                <UserSelectView
                                    handleSelect={() => setUserModalIsOpen(true)}
                                />
                            </Modal>
                        </>
                    )
                }

                return {
                    editModalView,
                    handleOpenModal,
                    handleCloseModal,
                }
            }

            const departmentModal = () => {
                const departmentModalView = () => {
                    return (
                        <Modal
                            isOpen={departmentModalIsOpen}
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
                                    fetchDepartments={fetchDepartments.load}
                                />
                            }
                        </Modal>
                    )
                }

                return {
                    departmentModalView,
                }
            }

            const employeeModal = () => {
                const employeeModalView = () => {
                    return (
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
                    )
                }

                return {
                    employeeModalView,
                }
            }

            const init = () => (
                <>
                    {editModal().editModalView()}
                </>
            )

            return {
                editModal,
                departmentModal,
                init,
            }
        }

        const collectionView = () => {
            const {handleOpenModal} = modalView().editModal();

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
                    if (!window.confirm(`社員コード:${empCode} を削除しますか？`)) return;
                    await employeeService.destroy(empCode);
                    await fetchEmployees.load();
                    setMessage("社員を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`社員の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckEmployee = (employee: EmployeeType) => {
                const newEmployee = employees.map((emp) => {
                    if (emp.empCode.value === employee.empCode.value) {
                        return {
                            ...emp,
                            checked: !emp.checked
                        };
                    }
                    return emp;
                });
                setEmployees(newEmployee);
            }

            const handleCheckAllEmployees = () => {
                const newEmployee = employees.map((emp) => {
                    return {
                        ...emp,
                        checked: !employees.every((emp) => emp.checked)
                    };
                });
                setEmployees(newEmployee);
            }

            const handleDeleteCheckedEmployees = async () => {
                if (!employees.some((emp) => emp.checked)) {
                    setError("削除する社員を選択してください。");
                    return;
                }

                try {
                    if (!window.confirm(`選択した社員を削除しますか？`)) return;
                    const checkedEmployees = employees.filter((emp) => emp.checked);
                    await Promise.all(checkedEmployees.map((emp) => employeeService.destroy(emp.empCode.value)));
                    await fetchEmployees.load();
                    setMessage("選択した社員を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`社員の削除に失敗しました: ${error?.message}`, setError);
                }
            }

            return (
                    <EmployeeCollectionView
                        error={error}
                        message={message}
                        searchItems={{searchEmployeeCode, setSearchEmployeeCode, handleSearchEmployee}}
                        headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllEmployees, handleDeleteCheckedCollection:handleDeleteCheckedEmployees}}
                        collectionItems={{employees, handleDeleteEmployee, handleCheckEmployee}}
                        pageNationItems={{pageNation, fetchEmployees:fetchEmployees.load}}
                    />
            )
        };

        const singleView = () => {
            const {handleCloseModal} = modalView().editModal();

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
                    await fetchEmployees.load();
                    if (isEditing) {
                        setMessage("社員を更新しました。");
                    } else {
                        setMessage("社員を作成しました。");
                    }
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`社員の作成に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                    <EmployeeSingleView
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

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <>
                        {modalView().init()}
                        {collectionView()}
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
