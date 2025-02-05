import React, {useEffect, useState} from "react";
import {useMessage} from "../../application/Message.tsx";
import {useModal} from "../../application/hooks.ts";
import {useDepartment, useFetchDepartments} from "./hooks";
import {showErrorMessage} from "../../application/utils.ts";
import {
    DepartmentCriteriaType,
    DepartmentIdType,
    DepartmentType,
    EmployeeCriteriaType,
    EmployeeType
} from "../../../models";
import Modal from "react-modal";
import {usePageNation} from "../../../views/application/PageNation.tsx";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {EmployeeCollectionSelectView} from "../../../views/master/employee/EmployeeSelect.tsx";
import {DepartmentCollectionView} from "../../../views/master/department/DepartmentCollection.tsx";
import {DepartmentSingleView} from "../../../views/master/department/DepartmentSingle.tsx";
import {EmployeeCollectionAddListView} from "../../../views/master/employee/EmployeeCollection.tsx";
import {DepartmentSearchSingleView} from "../../../views/master/department/DepartmentSearch.tsx";
import {useEmployee, useFetchEmployees} from "../employee/hooks";

export const Department: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation, criteria, setCriteria} = usePageNation<DepartmentCriteriaType>();
        const {pageNation: employeePageNation, setPageNation: setEmployeePageNation} = usePageNation<EmployeeCriteriaType>();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen,} = useModal();
        const {
            modalIsOpen: employeeModalIsOpen,
            setModalIsOpen: setEmployeeModalIsOpen,
            setIsEditing: setEmployeeIsEditing,
            setEditId: setEmployeeEditId
        } = useModal();

        const {
            initialDepartment,
            departments,
            setDepartments,
            newDepartment,
            setNewDepartment,
            searchDepartmentCriteria,
            setSearchDepartmentCriteria,
            departmentService
        } = useDepartment();

        const fetchDepartments = useFetchDepartments(
            setLoading,
            setDepartments,
            setPageNation,
            setError,
            showErrorMessage,
            departmentService
        );

        const {
            employees,
            setEmployees,
            employeeService
        } = useEmployee();

        const fetchEmployees = useFetchEmployees(
            setLoading,
            setEmployees,
            setEmployeePageNation,
            setError,
            showErrorMessage,
            employeeService
        );

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
                            {singleView()}
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
                    const newEmployees = newDepartment.employees.filter((e) => e.empCode.value !== employee.empCode.value);
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
                    const newEmployees = newDepartment.employees.filter((e) => e.empCode.value !== employee.empCode.value);
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

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {employeeModal().employeeModalView()}
                    {searchModal().searchModalView()}
                </>
            )

            return {
                editModal,
                employeeModal,
                searchModal,
                init,
            }
        }

        const collectionView = () => {
            const {handleOpenModal} = modalView().editModal();
            const {handleOpenSearchModal} = modalView().searchModal();

            const handleDeleteDepartment = async (departmentId: DepartmentIdType) => {
                try {
                    if (!window.confirm(`部門コード:${departmentId.deptCode.value} を削除しますか？`)) return;
                    await departmentService.destroy(departmentId.deptCode.value, departmentId.departmentStartDate.value);
                    await fetchDepartments.load();
                    setMessage("部門を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`部門の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckDepartment = (department: DepartmentType) => {
                const newDepartments = departments.map((d) => {
                    if (d.departmentId.deptCode.value === department.departmentId.deptCode.value && d.departmentId.departmentStartDate.value === department.departmentId.departmentStartDate.value) {
                        return {
                            ...d,
                            checked: !d.checked
                        };
                    }
                    return d;
                });
                setDepartments(newDepartments);
            }

            const handleCheckAllDepartment = () => {
                const newDepartments = departments.map((d) => {
                    return {
                        ...d,
                        checked: !departments.every((d) => d.checked)
                    };
                });
                setDepartments(newDepartments);
            }

            const handleDeleteCheckedCollection = async () => {
                const checkedDepartments = departments.filter((d) => d.checked);
                if (!checkedDepartments.length) {
                    setError("削除する部門を選択してください。");
                    return;
                }

                try {
                    if (!window.confirm("選択した部門を削除しますか？")) return;
                    await Promise.all(checkedDepartments.map((d) => departmentService.destroy(d.departmentId.deptCode.value, d.departmentId.departmentStartDate.value)));
                    await fetchDepartments.load();
                    setMessage("選択した部門を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`選択した部門の削除に失敗しました: ${error?.message}`, setError);
                }
            }

            return (
                    <DepartmentCollectionView
                        error={error}
                        message={message}
                        searchItems={{searchDepartmentCriteria, setSearchDepartmentCriteria, handleOpenSearchModal,}}
                        headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllDepartment, handleDeleteCheckedCollection}}
                        collectionItems={{departments, handleOpenModal, handleDeleteDepartment, handleCheckDepartment}}
                        pageNationItems={{pageNation, fetchDepartments: fetchDepartments.load, criteria}}
                    />
            );
        };

        const singleView = () => {
            const {handleCloseModal} = modalView().editModal();

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
                    setNewDepartment(initialDepartment);
                    await fetchDepartments.load();
                    if (isEditing) {
                        setMessage("部門を更新しました。");
                    } else {
                        setMessage("部門を作成しました。");
                    }
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`部門の作成に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <>
                    <DepartmentSingleView
                        error={error}
                        message={message}
                        isEditing={isEditing}
                        headerItems={{handleCreateOrUpdateDepartment, handleCloseModal}}
                        formItems={{newDepartment, setNewDepartment}}
                    />

                    {isEditing && (
                        <EmployeeCollectionAddListView
                            employees={newDepartment.employees.filter((e) => !e.deleteFlag)}
                            handleAdd={modalView().employeeModal().handleOpenEmployeeModal}
                            handleDelete={modalView().employeeModal().handleDeleteEmployee}
                        />
                    )
                    }
                </>
            );
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
