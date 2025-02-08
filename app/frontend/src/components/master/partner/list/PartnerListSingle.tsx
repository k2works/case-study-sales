import React from "react";
import {usePartnerListContext} from "../../../../providers/PartnerList.tsx";
import {usePartnerGroupContext} from "../../../../providers/PartnerGroup.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerSingleView} from "../../../../views/master/partner/PartnerSingle.tsx";
import {PartnerGroupSelectView} from "../../../../views/master/partner/group/PartnerGroupSelect.tsx";
import {PartnerGroupSelectModal} from "./PartnerGroupSelectModal.tsx";

export const PartnerListSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialPartner,
        newPartner,
        setNewPartner,
        fetchPartners,
        partnerService
    } = usePartnerListContext();

    const {
        setModalIsOpen: setPartnerGroupModalIsOpen,
    } = usePartnerGroupContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePartner = async () => {
        if (!newPartner.partnerCode.value.trim() || !newPartner.partnerName.name.trim()) {
            setError("取引先コードと名称は必須項目です。");
            return;
        }
        try {
            if (isEditing && editId) {
                await partnerService.update(newPartner);
            } else {
                await partnerService.create(newPartner);
            }
            setNewPartner({ ...initialPartner });
            await fetchPartners.load();
            setMessage(isEditing ? "取引先を更新しました。" : "取引先を作成しました。");
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`取引先の作成または更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <>
            <PartnerSingleView
                error={error}
                message={message}
                isEditing={isEditing}
                headerItems={{ handleCreateOrUpdatePartner, handleCloseModal }}
                formItems={{ newPartner, setNewPartner }}
            />

            <PartnerGroupSelectModal type={"edit"}/>
            <PartnerGroupSelectView
                handleSelect={() => setPartnerGroupModalIsOpen(true)}
            />
        </>
    );
}
