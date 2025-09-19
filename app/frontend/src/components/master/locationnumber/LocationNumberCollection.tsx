import React from "react";
import {useLocationNumberContext} from "../../../providers/master/LocationNumber.tsx";
import {LocationNumberCollectionView} from "../../../views/master/locationnumber/LocationNumberCollection.tsx";
import {LocationNumberType} from "../../../models/master/locationnumber.ts";
import {showErrorMessage} from "../../application/utils.ts";
import {LocationNumberSearchModal} from "./LocationNumberSearchModal.tsx";
import {LocationNumberEditModal} from "./LocationNumberEditModal.tsx";

export const LocationNumberCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialLocationNumber,
        locationNumbers,
        setLocationNumbers,
        setNewLocationNumber,
        searchLocationNumberCriteria,
        setSearchLocationNumberCriteria,
        fetchLocationNumbers,
        locationNumberService,
        setSearchModalIsOpen,
    } = useLocationNumberContext();

    const handleOpenModal = (locationNumber?: LocationNumberType) => {
        setMessage("");
        setError("");
        if (locationNumber) {
            setNewLocationNumber(locationNumber);
            setEditId(`${locationNumber.warehouseCode}-${locationNumber.locationNumberCode}-${locationNumber.productCode}`);
            setIsEditing(true);
        } else {
            setNewLocationNumber(initialLocationNumber);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteLocationNumber = async (warehouseCode: string, locationNumberCode: string, productCode: string) => {
        try {
            if (!window.confirm(`棚番:${warehouseCode}-${locationNumberCode}-${productCode} を削除しますか？`)) return;
            await locationNumberService.remove(warehouseCode, locationNumberCode, productCode);
            await fetchLocationNumbers.load(pageNation?.pageNum, criteria || undefined);
            setMessage("棚番を削除しました。");
        } catch (error: any) {
            showErrorMessage(`棚番の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckLocationNumber = (locationNumber: LocationNumberType) => {
        const newLocationNumbers = locationNumbers.map((ln: LocationNumberType) => {
            if (ln.warehouseCode === locationNumber.warehouseCode &&
                ln.locationNumberCode === locationNumber.locationNumberCode &&
                ln.productCode === locationNumber.productCode) {
                return {
                    ...ln,
                    checked: !ln.checked
                };
            }
            return ln;
        });
        setLocationNumbers(newLocationNumbers);
    }

    const handleCheckAllLocationNumber = () => {
        const newLocationNumbers = locationNumbers.map((ln: LocationNumberType) => {
            return {
                ...ln,
                checked: !locationNumbers.every((ln: LocationNumberType) => ln.checked)
            };
        });
        setLocationNumbers(newLocationNumbers);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedLocationNumbers = locationNumbers.filter((ln: LocationNumberType) => ln.checked);
        if (!checkedLocationNumbers.length) {
            setError("削除する棚番を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した棚番を削除しますか？")) return;
            await Promise.all(checkedLocationNumbers.map((ln: LocationNumberType) =>
                locationNumberService.remove(ln.warehouseCode, ln.locationNumberCode, ln.productCode)));
            await fetchLocationNumbers.load(pageNation?.pageNum, criteria || undefined);
            setMessage("選択した棚番を削除しました。");
        } catch (error: any) {
            showErrorMessage(`選択した棚番の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <LocationNumberSearchModal/>
            <LocationNumberEditModal/>
            <LocationNumberCollectionView
                error={error}
                message={message}
                searchItems={{searchLocationNumberCriteria, setSearchLocationNumberCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllLocationNumber, handleDeleteCheckedCollection}}
                collectionItems={{locationNumbers, handleOpenModal, handleDeleteLocationNumber, handleCheckLocationNumber}}
                pageNationItems={{pageNation, fetchLocationNumbers, criteria}}
            />
        </>
    );
};