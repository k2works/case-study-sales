import React from "react";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {useRegionContext} from "../../../../providers/master/code/Region.tsx";
import {RegionType} from "../../../../models/master/code";
import Modal from "react-modal";
import {RegionCodeCollectionSelectView} from "../../../../views/master/code/region/RegionSelect.tsx";

export const RegionSelectModal: React.FC = () => {
    const {
        setError,
        newCustomer,
        setNewCustomer,
        newShipping,
    } = useCustomerContext();

    const {
        pageNation: regionPageNation,
        modalIsOpen: regionModalIsOpen,
        setModalIsOpen: setRegionModalIsOpen,
        regions,
        setNewRegion,
        fetchRegions,
    } = useRegionContext();

    const handleCloseRegionModal = () => {
        setError(""); // エラーをリセット
        setRegionModalIsOpen(false); // モーダルを閉じる
    };

    const handleRegionCollectionSelect = (region: RegionType) => {
        setNewRegion(region);
        handleCloseRegionModal();
        const updateShippings = newCustomer.shippings.filter((shipping) => 
            shipping.customerCode !== newShipping.customerCode || 
            shipping.customerBranchNumber !== newShipping.customerBranchNumber || 
            shipping.destinationNumber !== newShipping.destinationNumber
        )
        updateShippings.push({
            ...newShipping,
            regionCode: region.regionCode
        })
        setNewCustomer({
            ...newCustomer,
            shippings: updateShippings
        });
    };

    return (
        <Modal
            isOpen={regionModalIsOpen}
            onRequestClose={handleCloseRegionModal}
            contentLabel="地域情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <RegionCodeCollectionSelectView
                    regions={regions} // 地域リスト
                    handleSelect={handleRegionCollectionSelect} // 地域選択ハンドラ
                    handleClose={handleCloseRegionModal} // 閉じるハンドラ
                    pageNation={regionPageNation} // ページネーション情報
                    fetchRegions={fetchRegions.load} // リージョンデータの取得関数
                />
            }
        </Modal>
    );
}
