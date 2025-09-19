import React from "react";
import Modal from "react-modal";
import {LocationNumberSearchSingleView} from "../../../views/master/locationnumber/LocationNumberSearch.tsx";
import {showErrorMessage} from "../../application/utils.ts";
import {useLocationNumberContext} from "../../../providers/master/LocationNumber.tsx";
import {LocationNumberCriteriaType} from "../../../models/master/locationnumber.ts";

export const LocationNumberSearchModal: React.FC = () => {
    const {
        searchLocationNumberCriteria,
        setSearchLocationNumberCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setMessage,
        setError,
        fetchLocationNumbers
    } = useLocationNumberContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchLocationNumberCriteria) {
            return;
        }
        try {
            const mappedCriteria: LocationNumberCriteriaType = {
                ...searchLocationNumberCriteria
            };

            await fetchLocationNumbers.load(1, mappedCriteria);
            setMessage("");
            setError("");
            setSearchModalIsOpen(false);
        } catch (error: any) {
            showErrorMessage(`棚番の検索に失敗しました: ${error?.message}`, setError);
        }
    }

    const handleClearSearch = () => {
        setSearchLocationNumberCriteria({});
    };

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <LocationNumberSearchSingleView
                criteria={searchLocationNumberCriteria}
                setCondition={setSearchLocationNumberCriteria}
                handleSelect={handleSelectSearchModal}
                handleClose={handleCloseSearchModal}
                onClearSearch={handleClearSearch}
            />
        </Modal>
    )
}