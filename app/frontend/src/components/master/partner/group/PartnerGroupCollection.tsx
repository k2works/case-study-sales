import React from "react";
import {usePartnerGroupContext} from "../../../../providers/master/partner/PartnerGroup.tsx";
import {PartnerGroupType} from "../../../../models/master/partner";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerGroupCollectionView} from "../../../../views/master/partner/group/PartnerGroupCollection.tsx";
import {PartnerGroupSearchModal} from "./PartnerGroupSearchModal.tsx";
import {PartnerGroupEditModal} from "./PartnerGroupEditModal.tsx";

export const PartnerGroupCollection: React.FC = () => {
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
        initialPartnerGroup,
        partnerGroups,
        setPartnerGroups,
        setNewPartnerGroup,
        searchPartnerGroupCriteria,
        setSearchPartnerGroupCriteria,
        fetchPartnerGroups,
        partnerGroupService,
    } = usePartnerGroupContext();

    const handleOpenModal = (partnerGroup?: PartnerGroupType) => {
        setMessage("");
        setError("");
        if (partnerGroup) {
            setNewPartnerGroup(partnerGroup);
            setEditId(partnerGroup.partnerGroupCode);
            setIsEditing(true);
        } else {
            setNewPartnerGroup(initialPartnerGroup);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

    const handleDeletePartnerGroup = async (partnerGroupTypeCode: string) => {
        try {
            if (!window.confirm(`取引先グループコード:${partnerGroupTypeCode} を削除しますか？`))
                return;
            await partnerGroupService.destroy(partnerGroupTypeCode);
            await fetchPartnerGroups.load();
            setMessage("取引先グループを削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : String(error);
            showErrorMessage(`取引先グループの削除に失敗しました: ${errorMessage}`, setError);
        }
    };

    const handleCheckPartnerGroup = (partnerGroup: PartnerGroupType) => {
        const newPartnerGroups = partnerGroups.map((group) => {
            if (group.partnerGroupCode === partnerGroup.partnerGroupCode) {
                return { ...group, checked: !group.checked };
            }
            return group;
        });
        setPartnerGroups(newPartnerGroups);
    };

    const handleCheckAllPartnerGroup = () => {
        const newPartnerGroups = partnerGroups.map((group) => ({
            ...group,
            checked: !partnerGroups.every((group) => group.checked),
        }));
        setPartnerGroups(newPartnerGroups);
    };

    const handleDeleteCheckedCollection = async () => {
        const checkedPartnerGroups = partnerGroups.filter((group) => group.checked);
        if (!checkedPartnerGroups.length) {
            setError("削除する取引先グループを選択してください。");
            return;
        }
        try {
            if (!window.confirm("選択した取引先グループを削除しますか？")) return;
            await Promise.all(
                checkedPartnerGroups.map((group) =>
                    partnerGroupService.destroy(group.partnerGroupCode)
                )
            );
            await fetchPartnerGroups.load();
            setMessage("選択した取引先グループを削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : String(error);
            showErrorMessage(
                `選択した取引先グループの削除に失敗しました: ${errorMessage}`,
                setError
            );
        }
    };

    return (
        <>
            <PartnerGroupSearchModal/>
            <PartnerGroupEditModal/>
            <PartnerGroupCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchPartnerGroupCriteria,
                    setSearchPartnerGroupCriteria,
                    handleOpenSearchModal,
                }}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllPartnerGroup,
                    handleDeleteCheckedCollection,
                }}
                collectionItems={{
                    partnerGroups,
                    handleOpenModal,
                    handleDeletePartnerGroup,
                    handleCheckPartnerGroup,
                }}
                pageNationItems={{
                    pageNation,
                    fetchGroups: fetchPartnerGroups.load,
                    criteria,
                }}
            />
        </>
    );
}
