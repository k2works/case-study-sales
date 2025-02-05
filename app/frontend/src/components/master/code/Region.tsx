import React, { useEffect, useState } from "react";
import { useMessage } from "../../application/Message.tsx";
import { useModal } from "../../application/hooks.ts";
import { useFetchRegions, useRegion } from "../code/hooks";
import { showErrorMessage } from "../../application/utils.ts";
import { RegionCriteriaType, RegionCodeType, RegionType } from "../../../models/master/code";
import Modal from "react-modal";
import { usePageNation } from "../../../views/application/PageNation.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { RegionCollectionView } from "../../../views/master/code/region/RegionCollection.tsx";
import { RegionSingleView } from "../../../views/master/code/region/RegionSingle.tsx";
import {RegionSearchSingleView} from "../../../views/master/code/region/RegionSearch.tsx";

export const Region: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const { message, setMessage, error, setError } = useMessage();
        const { pageNation, setPageNation, criteria, setCriteria } =
            usePageNation<RegionCriteriaType>();
        const {
            modalIsOpen,
            setModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId,
        } = useModal();
        const {
            initialRegion,
            regions,
            setRegions,
            newRegion,
            setNewRegion,
            searchRegionCriteria,
            setSearchRegionCriteria,
            regionService,
        } = useRegion();
        const fetchRegions = useFetchRegions(
            setLoading,
            setRegions,
            setPageNation,
            setError,
            showErrorMessage,
            regionService
        );
        const {modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen,} = useModal();

        useEffect(() => {
            fetchRegions.load().then(() => {});
        }, []);

        const modalView = () => {
            const editModal = () => {
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

                const handleCloseModal = () => {
                    setError("");
                    setModalIsOpen(false);
                    setEditId(null);
                };

                const editModalView = () => {
                    return (
                        <Modal
                            isOpen={modalIsOpen}
                            onRequestClose={handleCloseModal}
                            contentLabel="地域情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {singleView()}
                        </Modal>
                    );
                };

                return {
                    editModalView,
                    handleOpenModal,
                    handleCloseModal,
                };
            };

            const searchModal = () => {
                const handleOpenSearchModal = () => {
                    setSearchModalIsOpen(true);
                };

                const handleCloseSearchModal = () => {
                    setSearchModalIsOpen(false);
                };

                const searchModalView = () => {
                    return (
                        <Modal
                            isOpen={searchModalIsOpen}
                            onRequestClose={handleCloseSearchModal}
                            contentLabel="検索情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            <RegionSearchSingleView
                                criteria={searchRegionCriteria}
                                setCondition={setSearchRegionCriteria}
                                handleSelect={async () => {
                                    if (!searchRegionCriteria) {
                                        return;
                                    }
                                    setLoading(true);
                                    try {
                                        const result = await regionService.search(searchRegionCriteria);
                                        setRegions(result ? result.list : []);
                                        if (result.list.length === 0) {
                                            showErrorMessage("検索結果は0件です", setError);
                                        } else {
                                            setCriteria(searchRegionCriteria);
                                            setPageNation(result);
                                            setMessage("");
                                            setError("");
                                        }
                                    } catch (error: any) {
                                        showErrorMessage(`検索に失敗しました: ${error?.message}`, setError);
                                    } finally {
                                        setLoading(false);
                                    }
                                }}
                                handleClose={handleCloseSearchModal}
                            />
                        </Modal>
                    );
                };

                return {
                    searchModalView,
                    handleOpenSearchModal,
                    handleCloseSearchModal,
                };
            };

            const init = () => (
                <>
                    {editModal().editModalView()}
                    {searchModal().searchModalView()}
                </>
            );

            return {
                editModal,
                searchModal,
                init,
            };
        };

        const collectionView = () => {
            const { handleOpenModal } = modalView().editModal();
            const { handleOpenSearchModal } = modalView().searchModal();
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
            );
        };

        const singleView = () => {
            const { handleCloseModal } = modalView().editModal();
            const handleCreateOrUpdateRegion = async () => {
                const validateRegion = (): boolean => {
                    if (
                        !newRegion.regionCode.value.trim() ||
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
                } catch (error: any) {
                    showErrorMessage(`地域の作成に失敗しました: ${error?.message}`, setError);
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
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <>
                        {modalView().init()}
                        {collectionView()}
                    </>
                )}
            </>
        );
    };

    return (
            <Content />
    );
};