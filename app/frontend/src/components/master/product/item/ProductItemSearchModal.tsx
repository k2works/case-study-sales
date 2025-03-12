import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import Modal from "react-modal";
import {ProductSearchSingleView} from "../../../../views/master/product/ProductSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {VendorSelectModal} from "./VendorSelectModal.tsx";
import {ProductCategorySelectModal} from "./ProductCategorySelectModal.tsx";

export const ProductItemSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setProducts,
        searchProductCriteria,
        setSearchProductCriteria,
        productService
    } = useProductItemContext();


    const {
        setModalIsOpen: setProductCategoryModalIsOpen,
    } = useProductCategoryContext();


    const {
        setSearchModalIsOpen: setVendorSearchModalIsOpen,
    } = useVendorContext();


    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <>
                    <VendorSelectModal type={"search"}/>
                    <ProductCategorySelectModal type={"search"}/>
                    <ProductSearchSingleView
                        criteria={searchProductCriteria}
                        setCondition={setSearchProductCriteria}
                        handleSelect={async () => {
                            if (!searchProductCriteria) {
                                return;
                            }
                            setLoading(true);
                            try {
                                const result = await productService.search(searchProductCriteria);
                                setProducts(result ? result.list : []);
                                if (result.list.length === 0) {
                                    showErrorMessage(`検索結果は0件です`, setError);
                                } else {
                                    setCriteria(searchProductCriteria);
                                    setPageNation(result);
                                    setMessage("");
                                    setError("");
                                }
                            } catch (error: any) {
                                showErrorMessage(`実行履歴情報の検索に失敗しました: ${error?.message}`, setError);
                            } finally {
                                setLoading(false);
                            }
                        }}
                        handleClose={handleCloseSearchModal}
                        handleSelectVendor={() => setVendorSearchModalIsOpen(true)}
                        handleSelectProductCategory={() => setProductCategoryModalIsOpen(true)}
                    />
                </>
            }
        </Modal>
    )
}
