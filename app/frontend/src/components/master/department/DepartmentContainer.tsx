import React, {useEffect} from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {
    DepartmentType, EmployeeType
} from "../../../models";
import Modal from "react-modal";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {EmployeeCollectionSelectView} from "../../../views/master/employee/EmployeeSelect.tsx";
import {DepartmentSearchSingleView} from "../../../views/master/department/DepartmentSearch.tsx";
import {DepartmentProvider, useDepartmentContext} from "../../../providers/Department.tsx";
import {EmployeeProvider, useEmployeeContext} from "../../../providers/Employee.tsx";
import {DepartmentCollection} from "./DepartmentCollection.tsx";
import {DepartmentSingle} from "./DepartmentSingle.tsx";

export const DepartmentContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setLoading,
            setMessage,
            setError,
            setPageNation,
            setCriteria,
            searchModalIsOpen,
            setSearchModalIsOpen,
            modalIsOpen,
            setModalIsOpen,
            setIsEditing,
            setEditId,
            initialDepartment,
            setDepartments,
            newDepartment,
            setNewDepartment,
            searchDepartmentCriteria,
            setSearchDepartmentCriteria,
            fetchDepartments,
            departmentService
        } = useDepartmentContext();

        const {
            modalIsOpen: employeeModalIsOpen,
            setModalIsOpen: setEmployeeModalIsOpen,
            setIsEditing: setEmployeeIsEditing,
            setEditId: setEmployeeEditId,
            employees,
            fetchEmployees,
            pageNation: employeePageNation,
        } = useEmployeeContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchDepartments.load(),
                        fetchEmployees.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`部門情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        const modalView = () => {
            const searchModal = () => {
                const handleOpenSearchModal = () => {
                    setSearchModalIsOpen(true);
                }

                const handleCloseSearchModal = () => {
                    setSearchModalIsOpen(false);
                }

                const searchModalView = () => {
                    return (
                        <Modal
                            isOpen={searchModalIsOpen}
                            onRequestClose={handleCloseSearchModal}
                            contentLabel="検索情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                <DepartmentSearchSingleView
                                    criteria={searchDepartmentCriteria}
                                    setCondition={setSearchDepartmentCriteria}
                                    handleSelect={async () => {
                                        if (!searchDepartmentCriteria) {
                                            return;
                                        }
                                        setLoading(true);
                                        try {
                                            const result = await departmentService.search(searchDepartmentCriteria);
                                            setDepartments(result ? result.list : []);
                                            if (result.list.length === 0) {
                                                showErrorMessage(`検索結果は0件です`, setError);
                                            } else {
                                                setCriteria(searchDepartmentCriteria);
                                                setPageNation(result);
                                                setMessage("");
                                                setError("");
                                            }
                                        } catch (error: any) {
                                            showErrorMessage(`実行履歴情報の検索に失敗しました: ${error?.message}`, setError);
                                        } finally {
                                            setLoading(false);
                                        }
                                    }}
                                    handleClose={handleCloseSearchModal}
                                />
                            }
                        </Modal>
                    )
                }

                return {
                    searchModalView,
                    handleOpenSearchModal,
                    handleCloseSearchModal
                }
            }

            const editModal = () => {
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

                const editModalView = () => {
                    return (
                        <Modal
                            isOpen={modalIsOpen}
                            onRequestClose={handleCloseModal}
                            contentLabel="部門情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            <DepartmentSingle/>
                        </Modal>
                    )
                }

                return {
                    editModalView,
                    handleOpenModal,
                    handleCloseModal,
                }
            }

            const employeeModal = () => {
                const handleOpenEmployeeModal = () => {
                    setMessage("");
                    setError("");
                    setEmployeeIsEditing(true);
                    setEmployeeModalIsOpen(true);
                };

                const handleCloseEmployeeModal = () => {
                    setError("");
                    setEmployeeModalIsOpen(false);
                    setEmployeeEditId(null);
                };

                const handleEmployeeCollectionSelect = (employee: EmployeeType) => {
                    const newEmployees = newDepartment.employees.filter((e: EmployeeType) => e.empCode.value !== employee.empCode.value);
                    if (employee.empCode.value) {
                        newEmployees.push({
                            ...employee,
                            addFlag: true,
                            deleteFlag: false
                        });
                    }
                    setNewDepartment({
                        ...newDepartment,
                        employees: newEmployees
                    });
                }

                const handleDeleteEmployee = (employee: EmployeeType) => {
                    const newEmployees = newDepartment.employees.filter((e: EmployeeType) => e.empCode.value !== employee.empCode.value);
                    if (employee.empCode.value) {
                        newEmployees.push({
                            ...employee,
                            addFlag: false,
                            deleteFlag: true
                        });
                    }
                    setNewDepartment({
                        ...newDepartment,
                        employees: newEmployees
                    });
                }

                const employeeModalView = () => {
                    return (
                        <Modal
                            isOpen={employeeModalIsOpen}
                            onRequestClose={handleCloseEmployeeModal}
                            contentLabel="社員情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                <EmployeeCollectionSelectView
                                    employees={employees}
                                    handleSelect={handleEmployeeCollectionSelect}
                                    handleClose={handleCloseEmployeeModal}
                                    pageNation={employeePageNation}
                                    fetchEmployees={fetchEmployees.load}
                                />
                            }
                        </Modal>

                    )
                }

                return {
                    handleOpenEmployeeModal,
                    handleDeleteEmployee,
                    employeeModalView,
                }
            }

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {employeeModal().employeeModalView()}
                    {searchModal().searchModalView()}
                </>
            )

            return {
                init,
            }
        }

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <>
                        {modalView().init()}
                        <DepartmentCollection/>
                    </>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <DepartmentProvider>
                <EmployeeProvider>
                    <Content/>
                </EmployeeProvider>
            </DepartmentProvider>
        </SiteLayout>
    );
};