import React, {useEffect, useState} from "react";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {useDepartment, useEmployee, useFetchDepartments, useFetchEmployees} from "./hooks.ts";
import {showErrorMessage} from "../application/utils.ts";
import {DepartmentIdType, DepartmentType, EmployeeType} from "../../models";
import Modal from "react-modal";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {EmployeeCollectionListView, EmployeeCollectionSelectView} from "../../views/master/EmployeeSelect.tsx";
import {DepartmentCollectionView} from "../../views/master/DepartmentCollection.tsx";
import {DepartmentSingleView} from "../../views/master/DepartmentSingle.tsx";

export const Department: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {pageNation: employeePageNation, setPageNation: setEmployeePageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {
            modalIsOpen: employeeModalIsOpen,
            setModalIsOpen: setEmployeeModalIsOpen,
            isEditing: isEmployeeEditing,
            setIsEditing: setEmployeeIsEditing,
            editId: EmployeeEditId,
            setEditId: setEmployeeEditId
        } = useModal();

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
            fetchDepartments.load().then(() => {
                fetchEmployees.load().then(() => {
                });
            });
        }, []);

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
                <>
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

                    <DepartmentCollectionView
                        error={error}
                        message={message}
                        searchDepartmentId={searchDepartmentId}
                        setSearchDepartmentId={setSearchDepartmentId}
                        handleSearchDepartment={handleSearchDepartment}
                        handleOpenModal={handleOpenModal}
                        departments={departments}
                        handleDeleteDepartment={handleDeleteDepartment}
                        handleCheckDepartment={handleCheckDepartment}
                        handleCheckToggleCollection={handleCheckAllDepartment}
                        handleDeleteCheckedCollection={handleDeleteCheckedCollection}
                        pageNation={pageNation}
                        fetchDepartments={fetchDepartments.load}
                    />
                </>
            );
        };

        const singleView = () => {
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
                    setMessage("部門を保存しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`部門の保存に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <>
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

                    <DepartmentSingleView
                        error={error}
                        message={message}
                        newDepartment={newDepartment}
                        setNewDepartment={setNewDepartment}
                        isEditing={isEditing}
                        handleCreateOrUpdateDepartment={handleCreateOrUpdateDepartment}
                        handleCloseModal={handleCloseModal}
                    />

                    {isEditing && (
                        <EmployeeCollectionListView
                            employees={newDepartment.employees.filter((e) => !e.deleteFlag)}
                            handleAdd={handleOpenEmployeeModal}
                            handleDelete={handleDeleteEmployee}
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
