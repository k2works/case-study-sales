import React from "react";
import {useVendorContext} from "../../../../providers/Vendor.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {VendorSingleView} from "../../../../views/master/partner/vendor/VendorSingle.tsx";
import {VendorInvoiceSingleView} from "../../../../views/master/partner/vendor/VendorInvoiceSingle.tsx";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";

export const VendorSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialVendor,
        newVendor,
        setNewVendor,
        fetchVendors,
        vendorService
    } = useVendorContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

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
}
