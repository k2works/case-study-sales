import React, {useEffect, useState} from 'react';
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {useDepartment, useEmployee} from "./hooks.ts";
import {EmployeeType} from "../../types";
import {PageNation, usePageNation} from "../../ui/application/PageNation.tsx";
import {SiteLayout} from "../../ui/SiteLayout.tsx";
import {EmployeeCollectionView, SingleEmployeeView} from "../../ui/master/Employee.tsx";
import LoadingIndicator from "../../ui/application/LoadingIndicatior.tsx";
import {FaTimes} from "react-icons/fa";
import {useUser} from "../system/hooks.ts";
import {DepartmentCollectionSelectView, DepartmentSelectView} from "../../ui/master/DepartmentSelect.tsx";

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
            initialUser,
            users,
            setUsers,
            newUser,
            setNewUser,
            searchUserId,
            setSearchUserId,
            userService
        } = useUser();

        useEffect(() => {
            fetchEmployees().then(() => {
                fetchDepartments().then(() => {
                    fetchUsers().then(() => {
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

        const fetchUsers = async (page: number = 1) => {
            setLoading(true);
            try {
                const fetchedUsers = await userService.select(page);
                setUsers(fetchedUsers.list);
                setUserPageNation({...fetchedUsers});
                setError("");
            } catch (error: any) {
                showErrorMessage(`ユーザー情報の取得に失敗しました: ${error?.message}`, setError);
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
                    <SingleEmployeeView
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

                        <div className="collection-view-object-container">
                            <div className="collection-view-container">
                                <button className="close-modal-button" onClick={() => setUserModalIsOpen(false)}>
                                    <FaTimes aria-hidden="true"/>
                                </button>
                                <div className="collection-view-header">
                                    <div className="single-view-header-item">
                                        <h2 className="single-view-title">ユーザー</h2>
                                    </div>
                                </div>
                                <div className="collection-view-content">
                                    <div className="collection-object-container-modal">
                                        <ul className="collection-object-list">
                                            {users.map(user => (
                                                <li className="collection-object-item"
                                                    key={user.userId.value}>
                                                    <div className="collection-object-item-content"
                                                         data-id={user.userId.value}>
                                                        <div
                                                            className="collection-object-item-content-details">ユーザーID
                                                        </div>
                                                        <div
                                                            className="collection-object-item-content-name">{user.userId.value}</div>
                                                    </div>
                                                    <div className="collection-object-item-content"
                                                         data-id={user.userId.value}>
                                                        <div
                                                            className="collection-object-item-content-details">ユーザー名
                                                        </div>
                                                        <div
                                                            className="collection-object-item-content-name">{user.name.firstName + " " + user.name.lastName}</div>
                                                    </div>
                                                    <div className="collection-object-item-content"
                                                         data-id={user.userId.value}>
                                                        <div
                                                            className="collection-object-item-content-details">権限
                                                        </div>
                                                        <div
                                                            className="collection-object-item-content-name">{user.roleName}</div>
                                                    </div>
                                                    <div className="collection-object-item-actions"
                                                         data-id={user.userId.value}>
                                                        <button className="action-button"
                                                                onClick={() => {
                                                                    setNewEmployee({
                                                                        ...newEmployee,
                                                                        user: user
                                                                    });
                                                                    setUserModalIsOpen(false);
                                                                }}>選択
                                                        </button>
                                                    </div>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                </div>
                                <PageNation pageNation={userPageNation} callBack={fetchUsers}/>
                            </div>
                        </div>
                    </Modal>

                    <div className="collection-view-object-container">
                        <div className="collection-view-container">
                            <div className="collection-view-header">
                                <div className="single-view-header-item">
                                    <h2 className="single-view-title">ユーザー</h2>
                                </div>
                            </div>
                            <div className="collection-view-content">
                                <div className="button-container">
                                    <button className="action-button" onClick={
                                        () => {
                                            setUserModalIsOpen(true)
                                        }}>選択
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
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
