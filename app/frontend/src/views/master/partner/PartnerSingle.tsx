import React from "react";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";
import {MiscellaneousEnumType, PartnerType, TradeProhibitedFlagEnumType, VendorEnumType} from "../../../models/master/partner";
import {Message} from "../../../components/application/Message.tsx";
import { PrefectureEnumType } from "../../../models";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdatePartner: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdatePartner,
                    handleCloseModal,
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdatePartner}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newPartner: PartnerType;
    setNewPartner: React.Dispatch<React.SetStateAction<PartnerType>>;
    handleSelectPartnerGroup: () => void;
}

const Form = ({ isEditing, newPartner, setNewPartner, handleSelectPartnerGroup }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            {/* 取引先コード */}
            <FormInput
                label="取引先コード"
                id="partnerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先コード"
                value={newPartner.partnerCode ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        partnerCode: e.target.value,
                    })
                }
                disabled={isEditing}
            />

            {/* 取引先名 */}
            <FormInput
                label="取引先名"
                id="partnerName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先名"
                value={newPartner.partnerName ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        partnerName: e.target.value,
                    })
                }
            />

            {/* 取引先名カナ */}
            <FormInput
                label="取引先名カナ"
                id="partnerNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先名カナ"
                value={newPartner.partnerNameKana ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        partnerNameKana: e.target.value,
                    })
                }
            />

            {/* 仕入先区分 */}
            <FormSelect
                id="vendorType"
                label="仕入先区分"
                value={newPartner.vendorType}
                options={VendorEnumType}
                onChange={(e) => {
                    setNewPartner({
                        ...newPartner,
                        vendorType: e,
                    });
                }}
            />

            {/* 住所 */}
            <FormInput
                label="郵便番号"
                id="postalCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="郵便番号"
                value={newPartner.postalCode ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        postalCode: e.target.value,
                    })
                }
            />
            <FormSelect
                id="prefecture"
                label="都道府県"
                value={newPartner.prefecture ?? ""}
                options={PrefectureEnumType}
                onChange={(e) => {
                    setNewPartner({
                        ...newPartner,
                        prefecture: e,
                    });
                }}
            />
            <FormInput
                label="住所1"
                id="address1"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所1"
                value={newPartner.address1 ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        address1: e.target.value,
                    })
                }
            />
            <FormInput
                label="住所2"
                id="address2"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所2"
                value={newPartner.address2 ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        address2: e.target.value,
                    })
                }
            />

            {/* 取引禁止フラグ */}
            <FormSelect
                id="tradeProhibitedFlag"
                label="取引禁止フラグ"
                value={newPartner.tradeProhibitedFlag}
                options={TradeProhibitedFlagEnumType}
                onChange={(e) => {
                    setNewPartner({
                        ...newPartner,
                        tradeProhibitedFlag: e,
                    });
                }}
            />

            {/* 雑区分 */}
            <FormSelect
                id="miscellaneousType"
                label="雑区分"
                value={newPartner.miscellaneousType}
                options={MiscellaneousEnumType}
                onChange={(e) => {
                    setNewPartner({
                        ...newPartner,
                        miscellaneousType: e,
                    });
                }}
            />

            {/* 取引先グループコード */}
            <FormInput
                label="取引先グループコード"
                id="partnerGroupCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先グループコード"
                value={newPartner.partnerGroupCode ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        partnerGroupCode: e.target.value,
                    })
                }
                onClick={handleSelectPartnerGroup}
            />

            {/* 与信 */}
            <FormInput
                label="与信限度額"
                id="creditLimit"
                type="number"
                className="single-view-content-item-form-item-input"
                placeholder="与信限度額"
                value={newPartner.creditLimit ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        creditLimit: parseFloat(e.target.value),
                    })
                }
            />
            <FormInput
                label="与信一時増加枠"
                id="temporaryCreditIncrease"
                type="number"
                className="single-view-content-item-form-item-input"
                placeholder="与信一時増加枠"
                value={newPartner.temporaryCreditIncrease ?? ""}
                onChange={(e) =>
                    setNewPartner({
                        ...newPartner,
                        temporaryCreditIncrease: parseFloat(e.target.value),
                    })
                }
            />
        </div>
    );
};

interface PartnerSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdatePartner: () => void;
        handleCloseModal: () => void;
    };
    formItems: {
        newPartner: PartnerType;
        setNewPartner: React.Dispatch<React.SetStateAction<PartnerType>>;
    };
    handleSelectPartnerGroup: () => void;
}

export const PartnerSingleView: React.FC<PartnerSingleViewProps> = ({
                                                                        error,
                                                                        message,
                                                                        isEditing,
                                                                        headerItems: {
                                                                            handleCreateOrUpdatePartner,
                                                                            handleCloseModal,
                                                                        },
                                                                        formItems: {
                                                                            newPartner,
                                                                            setNewPartner,
                                                                        },
                                                                        handleSelectPartnerGroup,
                                                                    }) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="取引先"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdatePartner={handleCreateOrUpdatePartner}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newPartner={newPartner}
                        setNewPartner={setNewPartner}
                        handleSelectPartnerGroup={handleSelectPartnerGroup}
                    />
                </div>
            </div>
        </div>
    </div>
);