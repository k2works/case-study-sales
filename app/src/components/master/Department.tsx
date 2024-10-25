import React, {useEffect, useState} from "react";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {useDepartment} from "./hooks.ts";
import {showErrorMessage} from "../application/utils.ts";
import {DepartmentIdType, DepartmentType, LowerType, SlitYnType} from "../../types";
import Modal from "react-modal";
import BeatLoader from "react-spinners/BeatLoader";
import {usePageNation} from "../../ui/application/PageNation.tsx";
import {SiteLayout} from "../../ui/SiteLayout.tsx";
import {DepartmentCollectionView, DepartmentSingleView} from "../../ui/master/Department.tsx";

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
                <DepartmentCollectionView
                    error={error}
                    message={message}
                    searchDepartmentId={searchDepartmentId}
                    setSearchDepartmentId={setSearchDepartmentId}
                    handleSearchDepartment={handleSearchDepartment}
                    handleOpenModal={handleOpenModal}
                    departments={departments}
                    handleDeleteDepartment={handleDeleteDepartment}
                    pageNation={pageNation}
                    fetchDepartments={fetchDepartments}
                />
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
                <DepartmentSingleView
                    error={error}
                    message={message}
                    newDepartment={newDepartment}
                    setNewDepartment={setNewDepartment}
                    isEditing={isEditing}
                    handleCreateOrUpdateDepartment={handleCreateOrUpdateDepartment}
                    handleCloseModal={handleCloseModal}
                />
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
