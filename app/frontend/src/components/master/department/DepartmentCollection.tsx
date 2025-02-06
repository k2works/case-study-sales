import React from "react";
import {useDepartmentContext} from "../../../providers/Department.tsx";
import {DepartmentCollectionView} from "../../../views/master/department/DepartmentCollection.tsx";
import {DepartmentIdType, DepartmentType} from "../../../models";
import {showErrorMessage} from "../../application/utils.ts";

export const DepartmentCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialDepartment,
        departments,
        setDepartments,
        setNewDepartment,
        searchDepartmentCriteria,
        setSearchDepartmentCriteria,
        fetchDepartments,
        departmentService,
        setSearchModalIsOpen,
    } = useDepartmentContext();

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

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

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
        const newDepartments = departments.map((d: DepartmentType) => {
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
        const newDepartments = departments.map((d: DepartmentType) => {
            return {
                ...d,
                checked: !departments.every((d: DepartmentType) => d.checked)
            };
        });
        setDepartments(newDepartments);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedDepartments = departments.filter((d: DepartmentType) => d.checked);
        if (!checkedDepartments.length) {
            setError("削除する部門を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した部門を削除しますか？")) return;
            await Promise.all(checkedDepartments.map((d: DepartmentType) => departmentService.destroy(d.departmentId.deptCode.value, d.departmentId.departmentStartDate.value)));
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
}
