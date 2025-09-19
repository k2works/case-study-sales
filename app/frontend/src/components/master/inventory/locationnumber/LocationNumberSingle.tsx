import React from "react";
import {showErrorMessage} from "../../../application/utils.ts";
import {LocationNumberSingleView} from "../../../../views/master/locationnumber/LocationNumberSingle.tsx";
import {useLocationNumberContext} from "../../../../providers/master/LocationNumber.tsx";

export const LocationNumberSingle: React.FC = () => {
    const {
        error,
        setError,
        message,
        setMessage,
        isEditing,
        newLocationNumber,
        setNewLocationNumber,
        initialLocationNumber,
        fetchLocationNumbers,
        locationNumberService,
        setModalIsOpen,
        editId,
        setEditId
    } = useLocationNumberContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateLocationNumber = async () => {
        const validateLocationNumber = (): boolean => {
            if (!newLocationNumber.warehouseCode.trim() ||
                !newLocationNumber.locationNumberCode.trim() ||
                !newLocationNumber.productCode.trim()) {
                setError("倉庫コード、棚番コード、商品コードは必須項目です。");
                return false;
            }
            return true;
        };

        if (!validateLocationNumber()) {
            return;
        }

        try {
            if (isEditing && editId) {
                await locationNumberService.update(newLocationNumber);
            } else {
                await locationNumberService.save(newLocationNumber);
            }
            setNewLocationNumber(initialLocationNumber);
            await fetchLocationNumbers.load(1);
            if (isEditing) {
                setMessage("棚番を更新しました。");
            } else {
                setMessage("棚番を作成しました。");
            }
            handleCloseModal();
        } catch (error: unknown) {
            showErrorMessage(error, setError, "棚番の作成に失敗しました");
        }
    }

    return (
        <LocationNumberSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            headerItems={{handleCreateOrUpdateLocationNumber, handleCloseModal}}
            formItems={{newLocationNumber, setNewLocationNumber}}
        />
    );
};