import React from "react";
import {useRegionContext} from "../../../../providers/master/code/Region.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {RegionSingleView} from "../../../../views/master/code/region/RegionSingle.tsx";

export const RegionSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialRegion,
        newRegion,
        setNewRegion,
        fetchRegions,
        regionService,
    } = useRegionContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateRegion = async () => {
        const validateRegion = (): boolean => {
            if (
                !newRegion.regionCode.trim() ||
                !newRegion.regionName.trim()
            ) {
                setError("地域コード、地域名は必須項目です。");
                return false;
            }
            return true;
        };
        if (!validateRegion()) {
            return;
        }
        try {
            if (isEditing && editId) {
                await regionService.update(newRegion);
            } else {
                await regionService.create(newRegion);
            }
            setNewRegion(initialRegion);
            await fetchRegions.load();
            if (isEditing) {
                setMessage("地域を更新しました。");
            } else {
                setMessage("地域を作成しました。");
            }
            handleCloseModal();
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : String(error);
            showErrorMessage(`地域の作成に失敗しました: ${errorMessage}`, setError);
        }
    };
    return (
        <RegionSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            headerItems={{ handleCreateOrUpdateRegion, handleCloseModal }}
            formItems={{ newRegion, setNewRegion }}
        />
    );
}
