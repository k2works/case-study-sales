import React, {useEffect, useState} from 'react';
import {showErrorMessage} from "../../application/utils.ts";
import {useMessage} from "../../application/Message.tsx";
import {useModal} from "../../application/hooks.ts";
import {usePageNation} from "../../../views/application/PageNation.tsx";
import Modal from "react-modal";
import {
    CustomerCodeType,
    CustomerCriteriaType,
    CustomerType,
    ShippingType
} from "../../../models/master/partner";
import {useCustomer, useFetchCustomers } from "./hooks";
import {useFetchRegions, useRegion} from "../code/hooks";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {CustomerSearchView} from "../../../views/master/partner/customer/CustomerSearch.tsx";
import {
    CustomerCollectionView,
    CustomerShippingCollectionAddListView
} from "../../../views/master/partner/customer/CustomerCollection.tsx";
import {CustomerSingleView} from "../../../views/master/partner/customer/CustomerSingle.tsx";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import {CustomerInvoiceSingleView} from "../../../views/master/partner/customer/CustomerInvoiceSingle.tsx";
import {RegionCriteriaType, RegionType} from "../../../models/master/code";
import {RegionCodeCollectionSelectView} from "../../../views/master/code/region/RegionSelect.tsx";
import {PrefectureEnumType} from "../../../models";

export const Customer: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();
        const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<CustomerCriteriaType>();
        const {pageNation: regionPageNation, setPageNation: setRegionPageNation} = usePageNation<RegionCriteriaType>();
        const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
        const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
        const {
            modalIsOpen: regionModalIsOpen,
            setModalIsOpen: setRegionModalIsOpen,
            setIsEditing: setRegionIsEditing,
            setEditId: setRegionEditId
        } = useModal();

        const {
            initialCustomer,
            customers,
            setCustomers,
            newCustomer,
            setNewCustomer,
            searchCustomerCriteria,
            setSearchCustomerCriteria,
            customerService,
            newShipping,
            setNewShipping
        } = useCustomer();

        const fetchCustomers = useFetchCustomers(
            setLoading,
            setCustomers,
            setPageNation,
            setError,
            showErrorMessage,
            customerService
        );

        const {
            regions,
            setRegions,
            setNewRegion,
            regionService
        } = useRegion();

        const fetchRegions = useFetchRegions(
            setLoading,
            setRegions,
            setRegionPageNation,
            setError,
            showErrorMessage,
            regionService
        );

        useEffect(() => {
            fetchCustomers.load().then(() => {
                fetchRegions.load().then(() => {
                });
            });
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
                        <Tabs>
                            <TabList>
                                <Tab>基本情報</Tab>
                                <Tab>請求情報</Tab>
                                <Tab>出荷先情報</Tab>
                            </TabList>
                            <TabPanel>
                                {singleView().basicInfo()}
                            </TabPanel>
                            <TabPanel>
                                {singleView().invoiceInfo()}
                            </TabPanel>
                            <TabPanel>
                                <CustomerShippingCollectionAddListView
                                    setNewShipping={setNewShipping}
                                    shippings={newCustomer.shippings}
                                    handleAddShipping={() => {
                                        setMessage("");
                                        setError("");
                                        const maxDestinationNumber = Math.max(...newCustomer.shippings.map((shipping) => shipping.shippingCode.destinationNumber));
                                        const initialDestinationNumber = maxDestinationNumber === -Infinity ? 1 : maxDestinationNumber + 1;
                                        const shipping : ShippingType = {
                                            shippingCode: {
                                                customerCode: newCustomer.customerCode,
                                                destinationNumber: initialDestinationNumber,
                                            },
                                            destinationName: "出荷先名",
                                            regionCode: {
                                                value: "R001",
                                            },
                                            shippingAddress: {
                                                postalCode: {
                                                    value: "1234567",
                                                    regionCode: "",
                                                },
                                                prefecture: PrefectureEnumType.東京都,
                                                address1: "住所1",
                                                address2: "住所2",
                                            },
                                        }

                                        setNewCustomer({
                                            ...newCustomer,
                                            shippings: [...newCustomer.shippings, shipping]
                                        });
                                    }}
                                    handleDeleteShipping={(shippingAddress) => {
                                        setNewCustomer({
                                            ...newCustomer,
                                            shippings: newCustomer.shippings.filter(
                                                (shippingAddressItem) => shippingAddressItem.shippingCode.destinationNumber !== shippingAddress.shippingCode.destinationNumber
                                            )
                                        });
                                    }}
                                    handleAddRegion={() => {
                                        regionModal().handleOpenRegionModal()
                                    }}
                                />
                            </TabPanel>
                        </Tabs>
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

            const regionModal = () => {
                const handleOpenRegionModal = () => {
                    setMessage(""); // メッセージをリセット
                    setError(""); // エラーをリセット
                    setRegionIsEditing(true); // 編集可能と設定
                    setRegionModalIsOpen(true); // モーダルを開く
                };

                const handleCloseRegionModal = () => {
                    setError(""); // エラーをリセット
                    setRegionModalIsOpen(false); // モーダルを閉じる
                };

                const handleRegionCollectionSelect = (region: RegionType) => {
                    setNewRegion(region);
                    handleCloseRegionModal();
                    const updateShippings = newCustomer.shippings.filter((shipping) => shipping.shippingCode !== newShipping.shippingCode)
                    updateShippings.push({
                        ...newShipping,
                        regionCode: {
                            value: region.regionCode.value,
                        }
                    })
                    setNewCustomer({
                        ...newCustomer,
                        shippings: updateShippings
                    });
                };

                const regionModalView = () => {
                    return (
                        <Modal
                            isOpen={regionModalIsOpen}
                            onRequestClose={handleCloseRegionModal}
                            contentLabel="地域情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                <RegionCodeCollectionSelectView
                                    regions={regions} // 地域リスト
                                    handleSelect={handleRegionCollectionSelect} // 地域選択ハンドラ
                                    handleClose={handleCloseRegionModal} // 閉じるハンドラ
                                    pageNation={regionPageNation} // ページネーション情報
                                    fetchRegions={fetchRegions.load} // リージョンデータの取得関数
                                />
                            }
                        </Modal>
                    );
                };

                return {
                    handleOpenRegionModal,
                    handleCloseRegionModal,
                    regionModalView,
                };
            };

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {searchModal().searchModalView()}
                    {regionModal().regionModalView()}
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

            const basicInfo = () => {
                return (
                    <CustomerSingleView
                        error={error}
                        message={message}
                        isEditing={isEditing}
                        headerItems={{ handleCreateOrUpdateCustomer, handleCloseModal }}
                        formItems={{ newCustomer, setNewCustomer }}
                    />
                );
            }

            const invoiceInfo = () => {
                return (
                    <CustomerInvoiceSingleView
                        error={error}
                        message={message}
                        isEditing={isEditing}
                        headerItems={{ handleCreateOrUpdateCustomer, handleCloseModal }}
                        formItems={{ newCustomer, setNewCustomer }}
                    />
                );
            }

            return {
                basicInfo,
                invoiceInfo
            };
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