import React, { useEffect, useState } from 'react';
import { showErrorMessage } from "../application/utils.ts";
import { useMessage } from "../application/Message.tsx";
import { useModal } from "../application/hooks.ts";
import { usePageNation } from "../../views/application/PageNation.tsx";
import Modal from "react-modal";
import {CustomerCodeType, CustomerCriteriaType, CustomerType} from "../../models/master/partner";
import { useFetchCustomers, useCustomer } from "./hooks";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import { CustomerSearchView } from "../../views/master/partner/customer/CustomerSearch.tsx";
import { CustomerCollectionView } from "../../views/master/partner/customer/CustomerCollection.tsx";
import { CustomerSingleView } from "../../views/master/partner/customer/CustomerSingle.tsx";

export const Customer: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();
        const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<CustomerCriteriaType>();
        const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
        const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
        const {
            initialCustomer,
            customers,
            setCustomers,
            newCustomer,
            setNewCustomer,
            searchCustomerCriteria,
            setSearchCustomerCriteria,
            customerService
        } = useCustomer();

        const fetchCustomers = useFetchCustomers(
            setLoading,
            setCustomers,
            setPageNation,
            setError,
            showErrorMessage,
            customerService
        );

        useEffect(() => {
            fetchCustomers.load();
        }, []);

        const modalView = () => {
            const editModal = () => {
                const handleOpenModal = (customer?: CustomerType) => {
                    setMessage("");
                    setError("");
                    if (customer) {
                        setNewCustomer(customer);
                        setEditId(customer.customerCode.code.value);
                        setIsEditing(true);
                    } else {
                        setNewCustomer(initialCustomer);
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
                        contentLabel="顧客情報を入力"
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
                        contentLabel="検索情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        <CustomerSearchView
                            criteria={searchCustomerCriteria}
                            setCondition={setSearchCustomerCriteria}
                            handleSelect={async () => {
                                if (!searchCustomerCriteria) return;
                                setLoading(true);
                                try {
                                    const result = await customerService.search(searchCustomerCriteria);
                                    setCustomers(result ? result.list : []);
                                    if (result.list.length === 0) {
                                        showErrorMessage("検索結果は0件です", setError);
                                    } else {
                                        setCriteria(searchCustomerCriteria);
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

            const handleDeleteCustomer = async (customerCode: CustomerCodeType) => {
                try {
                    if (!window.confirm(`顧客コード:${customerCode.code.value} 枝番:${customerCode.branchNumber} を削除しますか？`)) return;
                    await customerService.destroy(customerCode);
                    await fetchCustomers.load();
                    setMessage("顧客を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`顧客の削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckCustomer = (customer: CustomerType) => {
                const newCustomers = customers.map((c) => {
                    if (c.customerCode.code.value === customer.customerCode.code.value) {
                        return { ...c, checked: !c.checked };
                    }
                    return c;
                });
                setCustomers(newCustomers);
            }

            const handleCheckAllCustomers = () => {
                const newCustomers = customers.map((c) => {
                    return { ...c, checked: !c.checked };
                });
                setCustomers(newCustomers);
            }

            const handleDeleteCheckCollection = async () => {
                const checkedCustomers = customers.filter((c) => c.checked);
                if (checkedCustomers.length === 0) {
                    showErrorMessage("削除する顧客を選択してください", setError);
                    return;
                }
                try {
                    if (!window.confirm("選択した顧客を削除しますか？")) return;
                    await Promise.all(
                        checkedCustomers.map((customer) =>
                            customerService.destroy(customer.customerCode)
                        )
                    );
                    await fetchCustomers.load();
                    setMessage("選択した顧客を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`選択した顧客の削除に失敗しました: ${error?.message}`, setError);
                }
            }

            return (
                <CustomerCollectionView
                    error={error}
                    message={message}
                    searchItems={{
                        searchCustomerCriteria,
                        setSearchCustomerCriteria,
                        handleOpenSearchModal,
                    }}
                    headerItems={{
                        handleOpenModal,
                        handleCheckToggleCollection: handleCheckAllCustomers,
                        handleDeleteCheckedCollection: handleDeleteCheckCollection,
                    }}
                    collectionItems={{
                        customers,
                        handleOpenModal,
                        handleDeleteCustomer,
                        handleCheckCustomer,
                    }}
                    pageNationItems={{
                        pageNation,
                        fetchCustomers: fetchCustomers.load,
                        criteria,
                    }}
                />
            );
        };

        const singleView = () => {
            const { handleCloseModal } = modalView().editModal();
            const handleCreateOrUpdateCustomer = async () => {
                if (!newCustomer.customerCode.code.value.trim() || !newCustomer.customerName.value.name.trim()) {
                    setError("顧客コードと名称は必須項目です。");
                    return;
                }
                try {
                    if (isEditing && editId) {
                        await customerService.update(newCustomer);
                    } else {
                        await customerService.create(newCustomer);
                    }
                    setNewCustomer({ ...initialCustomer });
                    await fetchCustomers.load();
                    setMessage(isEditing ? "顧客を更新しました。" : "顧客を作成しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`顧客の作成または更新に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <CustomerSingleView
                    error={error}
                    message={message}
                    isEditing={isEditing}
                    headerItems={{ handleCreateOrUpdateCustomer, handleCloseModal }}
                    formItems={{ newCustomer, setNewCustomer }}
                />
            );
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