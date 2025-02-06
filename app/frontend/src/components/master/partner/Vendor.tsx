import React, { useEffect, useState } from 'react';
import { showErrorMessage } from "../../application/utils.ts";
import { useMessage } from "../../application/Message.tsx";
import { useModal } from "../../application/hooks.ts";
import { usePageNation } from "../../../views/application/PageNation.tsx";
import Modal from "react-modal";
import {VendorCodeType, VendorCriteriaType, VendorType} from "../../../models/master/partner";
import { useFetchVendors, useVendor } from "./hooks";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { VendorSearchView } from "../../../views/master/partner/vendor/VendorSearch.tsx";
import { VendorCollectionView } from "../../../views/master/partner/vendor/VendorCollection.tsx";
import { VendorSingleView } from "../../../views/master/partner/vendor/VendorSingle.tsx";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import {VendorInvoiceSingleView} from "../../../views/master/partner/vendor/VendorInvoiceSingle.tsx";

export const Vendor: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();

        const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<VendorCriteriaType>();
        const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

        const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
        const {
            initialVendor,
            vendors,
            setVendors,
            newVendor,
            setNewVendor,
            searchVendorCriteria,
            setSearchVendorCriteria,
            vendorService
        } = useVendor();
        const fetchVendors = useFetchVendors(
            setLoading,
            setVendors,
            setPageNation,
            setError,
            showErrorMessage,
            vendorService
        );

        useEffect(() => {
            fetchVendors.load();
        }, []);

        const modalView = () => {
            const editModal = () => {
                const handleOpenModal = (vendor?: VendorType) => {
                    setMessage("");
                    setError("");
                    if (vendor) {
                        setNewVendor(vendor);
                        setEditId(`${vendor.vendorCode.code}-${vendor.vendorCode.branchNumber}`);
                        setIsEditing(true);
                    } else {
                        setNewVendor(initialVendor);
                        setIsEditing(false);
                    }
                    setModalIsOpen(true);
                };
                const handleCloseModal = () => {
                    setError("");
                    setModalIsOpen(false);
                    setEditId(null);
                };
                const editModalView = () => (
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={handleCloseModal}
                        contentLabel="仕入先情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {singleView()}
                    </Modal>
                );
                return { editModalView, handleOpenModal, handleCloseModal };
            };
            const searchModal = () => {
                const handleOpenSearchModal = () => {
                    setSearchModalIsOpen(true);
                };
                const handleCloseSearchModal = () => {
                    setSearchModalIsOpen(false);
                };
                const searchModalView = () => (
                    <Modal
                        isOpen={searchModalIsOpen}
                        onRequestClose={handleCloseSearchModal}
                        contentLabel="検索条件を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        <VendorSearchView
                            criteria={searchVendorCriteria}
                            setCondition={setSearchVendorCriteria}
                            handleSelect={async () => {
                                if (!searchVendorCriteria) return;
                                setLoading(true);
                                try {
                                    const result = await vendorService.search(searchVendorCriteria);
                                    setVendors(result ? result.list : []);
                                    if (result.list.length === 0) {
                                        showErrorMessage("検索結果は0件です", setError);
                                    } else {
                                        setCriteria(searchVendorCriteria);
                                        setPageNation(result);
                                        setMessage("");
                                        setError("");
                                    }
                                } catch (error: any) {
                                    showErrorMessage(`検索に失敗しました: ${error?.message}`, setError);
                                } finally {
                                    setLoading(false);
                                }
                            }}
                            handleClose={handleCloseSearchModal}
                        />
                    </Modal>
                );
                return { searchModalView, handleOpenSearchModal, handleCloseSearchModal };
            };
            const init = () => (
                <>
                    {editModal().editModalView()}
                    {searchModal().searchModalView()}
                </>
            );
            return { editModal, searchModal, init };
        };
        const collectionView = () => {
            const { handleOpenModal } = modalView().editModal();
            const { handleOpenSearchModal } = modalView().searchModal();
            const handleDeleteVendor = async (vendorCode: VendorCodeType) => {
                try {
                    if (!window.confirm(`仕入先コード:${vendorCode.code.value} 枝番:${vendorCode.branchNumber} を削除しますか？`)) return;
                    await vendorService.destroy(vendorCode);
                    await fetchVendors.load();
                    setMessage("仕入先を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`仕入先の削除に失敗しました: ${error?.message}`, setError);
                }
            };
            const handleCheckVendor = (vendor: VendorType) => {
                const newVendors = vendors.map((v) => {
                    if (v.vendorCode.code === vendor.vendorCode.code &&
                        v.vendorCode.branchNumber === vendor.vendorCode.branchNumber) {
                        return { ...v, checked: !v.checked };
                    }
                    return v;
                });
                setVendors(newVendors);
            }
            const handleCheckAllVendors = () => {
                const newVendors = vendors.map((v) => {
                    return { ...v, checked: !v.checked };
                });
                setVendors(newVendors);
            }
            const handleDeleteCheckCollection = async () => {
                const checkedVendors = vendors.filter((v) => v.checked);
                if (checkedVendors.length === 0) {
                    showErrorMessage("削除する仕入先を選択してください", setError);
                    return;
                }
                try {
                    if (!window.confirm("選択した仕入先を削除しますか？")) return;
                    await Promise.all(
                        checkedVendors.map((vendor) =>
                            vendorService.destroy(vendor.vendorCode)
                        )
                    );
                    await fetchVendors.load();
                    setMessage("選択した仕入先を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`選択した仕入先の削除に失敗しました: ${error?.message}`, setError);
                }
            }
            return (
                <VendorCollectionView
                    error={error}
                    message={message}
                    searchItems={{
                        searchVendorCriteria,
                        setSearchVendorCriteria,
                        handleOpenSearchModal,
                    }}
                    headerItems={{
                        handleOpenModal,
                        handleCheckToggleCollection: handleCheckAllVendors,
                        handleDeleteCheckedCollection: handleDeleteCheckCollection,
                    }}
                    collectionItems={{
                        vendors,
                        handleOpenModal,
                        handleDeleteVendor,
                        handleCheckVendor,
                    }}
                    pageNationItems={{
                        pageNation,
                        fetchVendors: fetchVendors.load,
                        criteria,
                    }}
                />
            );
        };
        const singleView = () => {
            const { handleCloseModal } = modalView().editModal();
            const handleCreateOrUpdateVendor = async () => {
                if (!newVendor.vendorCode.code.value.trim() || !newVendor.vendorName.value.name.trim()) {
                    setError("仕入先コードと名称は必須項目です。");
                    return;
                }
                try {
                    if (isEditing && editId) {
                        await vendorService.update(newVendor);
                    } else {
                        await vendorService.create(newVendor);
                    }
                    setNewVendor({ ...initialVendor });
                    await fetchVendors.load();
                    setMessage(isEditing ? "仕入先を更新しました。" : "仕入先を作成しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`仕入先の作成または更新に失敗しました: ${error?.message}`, setError);
                }
            };

            const basicInfo = () => {
                return (
                    <VendorSingleView
                        error={error}
                        message={message}
                        isEditing={isEditing}
                        headerItems={{ handleCreateOrUpdateVendor, handleCloseModal }}
                        formItems={{ newVendor, setNewVendor }}
                    />
                );
            };

            const invoiceInfo = () => {
                return (
                    <VendorInvoiceSingleView
                        error={error}
                        message={message}
                        isEditing={isEditing}
                        headerItems={{ handleCreateOrUpdateVendor, handleCloseModal }}
                        formItems={{ newVendor, setNewVendor }}
                    />
                );
            };

            return(
                    <Tabs>
                        <TabList>
                            <Tab>基本情報</Tab>
                            <Tab>請求情報</Tab>
                        </TabList>
                        <TabPanel>
                            {basicInfo()}
                        </TabPanel>
                        <TabPanel>
                            {invoiceInfo()}
                        </TabPanel>
                    </Tabs>
            )
        };
        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <>
                        {modalView().init()}
                        {collectionView()}
                    </>
                )}
            </>
        );
    };
    return <Content />;
};