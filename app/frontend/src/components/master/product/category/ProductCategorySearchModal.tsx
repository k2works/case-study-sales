import React from "react";
import {useProductCategoryContext} from "../../../../providers/ProductCategory.tsx";
import Modal from "react-modal";
import {ProductCategorySearchSingleView} from "../../../../views/master/product/ProductCategorySearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";

export const ProductCategorySearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setProductCategories,
        searchProductCategoryCriteria,
        setSearchProductCategoryCriteria,
        productCategoryService,
    } = useProductCategoryContext();

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
                <ProductCategorySearchSingleView
                    criteria={searchProductCategoryCriteria}
                    setCondition={setSearchProductCategoryCriteria}
                    handleSelect={async () => {
                        if (!searchProductCategoryCriteria) {
                            return;
                        }
                        setLoading(true);
                        try {
                            const result = await productCategoryService.search(searchProductCategoryCriteria);
                            setProductCategories(result ? result.list : []);
                            if (result.list.length === 0) {
                                showErrorMessage(`検索結果は0件です`, setError);
                            } else {
                                setCriteria(searchProductCategoryCriteria);
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
                />
            }
        </Modal>
    )
}
