import React from "react";
import {usePartnerGroupContext} from "../../../../providers/master/partner/PartnerGroup.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerGroupSingleView} from "../../../../views/master/partner/group/PartnerGroupSingle.tsx";

export const PartnerGroupSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialPartnerGroup,
        newPartnerGroup,
        setNewPartnerGroup,
        fetchPartnerGroups,
        partnerGroupService,
    } = usePartnerGroupContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePartnerGroup = async () => {
        const validatePartnerGroup = (): boolean => {
            if (!newPartnerGroup.partnerGroupCode.trim() ||
                !newPartnerGroup.partnerGroupName.trim()) {
                setError("取引先グループコードと名称は必須項目です。");
                return false;
            }
            return true;
        };

        if (!validatePartnerGroup()) return;

        try {
            if (isEditing && editId) {
                await partnerGroupService.update(newPartnerGroup);
            } else {
                await partnerGroupService.create(newPartnerGroup);
            }
            setNewPartnerGroup({ ...initialPartnerGroup });
            await fetchPartnerGroups.load();
            setMessage(isEditing ? "取引先グループを更新しました。" : "取引先グループを作成しました。");
            handleCloseModal();
        } catch (error: unknown) {
            showErrorMessage(error, setError, "取引先グループの作成または更新に失敗しました");
        }
    };

    return (
        <PartnerGroupSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            headerItems={{ handleCreateOrUpdatePartnerGroup, handleCloseModal }}
            formItems={{ newPartnerGroup, setNewPartnerGroup }}
        />
    );
}
