import React from "react";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {useRegionContext} from "../../../../providers/master/code/Region.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {CustomerSingleView} from "../../../../views/master/partner/customer/CustomerSingle.tsx";
import {CustomerInvoiceSingleView} from "../../../../views/master/partner/customer/CustomerInvoiceSingle.tsx";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import {CustomerShippingCollectionAddListView} from "../../../../views/master/partner/customer/CustomerCollection.tsx";
import {ShippingType} from "../../../../models/master/partner";
import {PrefectureEnumType} from "../../../../models";
import {RegionSelectModal} from "./RegionSelectModal.tsx";

export const CustomerSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialCustomer,
        newCustomer,
        setNewCustomer,
        setNewShipping,
        fetchCustomers,
        customerService
    } = useCustomerContext();

    const {
        setModalIsOpen: setRegionModalIsOpen,
        setIsEditing: setRegionIsEditing,
    } = useRegionContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

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

    const handleOpenRegionModal = () => {
        setMessage(""); // メッセージをリセット
        setError(""); // エラーをリセット
        setRegionIsEditing(true); // 編集可能と設定
        setRegionModalIsOpen(true); // モーダルを開く
    };

    const handleAddShipping = () => {
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
    }

    const handleDeleteShipping = (shippingAddress: ShippingType) => {
        setNewCustomer({
            ...newCustomer,
            shippings: newCustomer.shippings.filter(
                (shippingAddressItem) => shippingAddressItem.shippingCode.destinationNumber !== shippingAddress.shippingCode.destinationNumber
            )
        });
    }

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

    return (
        <>
            <RegionSelectModal/>
            <Tabs>
                <TabList>
                    <Tab>基本情報</Tab>
                    <Tab>請求情報</Tab>
                    <Tab>出荷先情報</Tab>
                </TabList>
                <TabPanel>
                    {basicInfo()}
                </TabPanel>
                <TabPanel>
                    {invoiceInfo()}
                </TabPanel>
                <TabPanel>
                    <CustomerShippingCollectionAddListView
                        setNewShipping={setNewShipping}
                        shippings={newCustomer.shippings}
                        handleAddShipping={handleAddShipping}
                        handleDeleteShipping={handleDeleteShipping}
                        handleAddRegion={handleOpenRegionModal}
                    />
                </TabPanel>
            </Tabs>
        </>
    )
}
