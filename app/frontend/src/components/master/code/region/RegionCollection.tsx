import React from "react";
import {useRegionContext} from "../../../../providers/master/code/Region.tsx";
import {RegionCodeType, RegionType} from "../../../../models/master/code";
import {showErrorMessage} from "../../../application/utils.ts";
import {RegionCollectionView} from "../../../../views/master/code/region/RegionCollection.tsx";
import {RegionSearchModal} from "./RegionSearchModal.tsx";
import {RegionEditModal} from "./RegionEditModal.tsx";

export const RegionCollection: React.FC = () => {
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
        initialRegion,
        regions,
        setRegions,
        setNewRegion,
        searchRegionCriteria,
        setSearchRegionCriteria,
        fetchRegions,
        regionService,
    } = useRegionContext();

    const handleOpenModal = (region?: RegionType) => {
        setMessage("");
        setError("");
        if (region) {
            setNewRegion(region);
            setEditId(region.regionCode.value);
            setIsEditing(true);
        } else {
            setNewRegion(initialRegion);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

    const handleDeleteRegion = async (regionCode: RegionCodeType) => {
        try {
            if (!window.confirm(`地域コード:${regionCode.value} を削除しますか？`))
                return;
            await regionService.destroy(regionCode.value);
            await fetchRegions.load();
            setMessage("地域を削除しました。");
        } catch (error: any) {
            showErrorMessage(`地域の削除に失敗しました: ${error?.message}`, setError);
        }
    };
    const handleCheckRegion = (region: RegionType) => {
        const newRegions = regions.map((r) => {
            if (r.regionCode.value === region.regionCode.value) {
                return { ...r, checked: !r.checked };
            }
            return r;
        });
        setRegions(newRegions);
    };
    const handleCheckAllRegion = () => {
        const newRegions = regions.map((r) => ({
            ...r,
            checked: !regions.every((r) => r.checked),
        }));
        setRegions(newRegions);
    };
    const handleDeleteCheckedCollection = async () => {
        const checkedRegions = regions.filter((r) => r.checked);
        if (!checkedRegions.length) {
            setError("削除する地域を選択してください。");
            return;
        }
        try {
            if (!window.confirm("選択した地域を削除しますか？")) return;
            await Promise.all(
                checkedRegions.map((r) =>
                    regionService.destroy(r.regionCode.value)
                )
            );
            await fetchRegions.load();
            setMessage("選択した地域を削除しました。");
        } catch (error: any) {
            showErrorMessage(
                `選択した地域の削除に失敗しました: ${error?.message}`,
                setError
            );
        }
    };
    return (
        <>
            <RegionSearchModal/>
            <RegionEditModal/>
            <RegionCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchRegionCriteria,
                    setSearchRegionCriteria,
                    handleOpenSearchModal
                }}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllRegion,
                    handleDeleteCheckedCollection,
                }}
                collectionItems={{
                    regions,
                    handleOpenModal,
                    handleDeleteRegion,
                    handleCheckRegion,
                }}
                pageNationItems={{
                    pageNation,
                    fetchRegions: fetchRegions.load,
                    criteria,
                }}
            />
        </>
    );
}
