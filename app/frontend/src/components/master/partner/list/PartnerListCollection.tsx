import React from "react";
import {usePartnerListContext} from "../../../../providers/PartnerList.tsx";
import {PartnerType} from "../../../../models/master/partner";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerCollectionView} from "../../../../views/master/partner/PartnerCollection.tsx";
import {PartnerListSearchModal} from "./PartnerListSearchModal.tsx";
import {PartnerListEditModal} from "./PartnerListEditModal.tsx";

export const PartnerListCollection: React.FC = () => {
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
        initialPartner,
        partners,
        setPartners,
        setNewPartner,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
        fetchPartners,
        partnerService
    } = usePartnerListContext();

    const handleOpenModal = (partner?: PartnerType) => {
        setMessage("");
        setError("");
        if (partner) {
            setNewPartner(partner);
            setEditId(partner.partnerCode.value);
            setIsEditing(true);
        } else {
            setNewPartner(initialPartner);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

    const handleDeletePartner = async (partnerCode: string) => {
        try {
            if (!window.confirm(`取引先コード:${partnerCode} を削除しますか？`)) return;
            await partnerService.destroy(partnerCode);
            await fetchPartners.load();
            setMessage("取引先を削除しました。");
        } catch (error: any) {
            showErrorMessage(`取引先の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckPartner = (partner: PartnerType) => {
        const newPartners = partners.map((p) => {
            if (p.partnerCode.value === partner.partnerCode.value) {
                return { ...p, checked: !p.checked };
            }
            return p;
        });
        setPartners(newPartners);
    }

    const handleCheckAllPartners = () => {
        const newPartners = partners.map((p) => {
            return { ...p, checked: !p.checked };
        });
        setPartners(newPartners);
    }

    const handleDeleteCheckCollection = async () => {
        const checkedPartners = partners.filter((p) => p.checked);
        if (checkedPartners.length === 0) {
            showErrorMessage("削除する取引先を選択してください", setError);
            return;
        }
        try {
            if (!window.confirm("選択した取引先を削除しますか？")) return;
            await Promise.all(
                checkedPartners.map((partners) =>
                    partnerService.destroy(partners.partnerCode.value)
                )
            );
            await fetchPartners.load();
            setMessage("選択した取引先を削除しました。");
        } catch (error: any) {
            showErrorMessage(`選択した取引先の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <PartnerListSearchModal/>
            <PartnerListEditModal/>
            <PartnerCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchPartnerCriteria,
                    setSearchPartnerCriteria,
                    handleOpenSearchModal,
                }}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllPartners,
                    handleDeleteCheckedCollection: handleDeleteCheckCollection,
                }}
                collectionItems={{
                    partners,
                    handleOpenModal,
                    handleDeletePartner,
                    handleCheckPartner,
                }}
                pageNationItems={{
                    pageNation,
                    fetchPartners: fetchPartners.load,
                    criteria,
                }}
            />
        </>
    );
}
