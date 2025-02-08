import React from "react";
import {useVendorContext} from "../../../../providers/Vendor.tsx";
import {VendorCodeType, VendorType} from "../../../../models/master/partner";
import {showErrorMessage} from "../../../application/utils.ts";
import {VendorCollectionView} from "../../../../views/master/partner/vendor/VendorCollection.tsx";
import {VendorSearchModal} from "./VendorSearchModal.tsx";
import {VendorEditModal} from "./VendorEditModal.tsx";

export const VendorCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setSearchModalIsOpen,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialVendor,
        vendors,
        setVendors,
        setNewVendor,
        searchVendorCriteria,
        setSearchVendorCriteria,
        fetchVendors,
        vendorService
    } = useVendorContext();

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

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

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
        <>
            <VendorSearchModal/>
            <VendorEditModal/>
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
        </>
    );
}
