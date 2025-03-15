import React from "react";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import { VendorType } from "../../../../models/master/partner";
import { Message } from "../../../../components/application/Message.tsx";
import {PrefectureEnumType} from "../../../../models";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateVendor: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateVendor,
                    handleCloseModal,
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateVendor}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newVendor: VendorType;
    setNewVendor: React.Dispatch<React.SetStateAction<VendorType>>;
}

const Form = ({ isEditing, newVendor, setNewVendor }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            {/* 仕入先コード */}
            <FormInput
                label="仕入先コード"
                id="vendorCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード"
                value={newVendor.vendorCode}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorCode: e.target.value,
                    })
                }
                disabled={isEditing}
            />

            {/* 仕入先コード枝番 */}
            <FormInput
                label="仕入先コード枝番"
                id="branchNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード枝番"
                value={newVendor.vendorBranchNumber}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorBranchNumber: parseInt(e.target.value),
                    })
                }
                disabled={isEditing}
            />

            {/* 仕入先名 */}
            <FormInput
                label="仕入先名"
                id="vendorName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先名"
                value={newVendor.vendorName}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorName: e.target.value,
                    })
                }
            />

            {/* 担当者名 */}
            <FormInput
                label="担当者名"
                id="vendorContactName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="担当者名"
                value={newVendor.vendorContactName}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorContactName: e.target.value,
                    })
                }
            />

            {/* 部門名 */}
            <FormInput
                label="部門名"
                id="vendorDepartmentName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門名"
                value={newVendor.vendorDepartmentName}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorDepartmentName: e.target.value,
                    })
                }
            />
            {/* 郵便番号 */}
            <FormInput
                label="郵便番号"
                id="postalCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="郵便番号"
                value={newVendor.vendorPostalCode}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorPostalCode: e.target.value,
                    })
                }
            />

            {/* 都道府県 */}
            <FormSelect
                id="prefecture"
                label="都道府県"
                value={newVendor.vendorPrefecture}
                options={PrefectureEnumType}
                onChange={(e) => {
                    setNewVendor({
                        ...newVendor,
                        vendorPrefecture: e,
                    })
                }}
            />

            {/* 住所 */}
            <FormInput
                label="住所1"
                id="address1"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所"
                value={newVendor.vendorAddress1}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorAddress1: e.target.value,
                    })
                }
            />

            {/* 住所2 */}
            <FormInput
                label="住所2"
                id="address2"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所2"
                value={newVendor.vendorAddress2}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorAddress2: e.target.value,
                    })
                }
            />

            {/* 電話番号 */}
            <FormInput
                label="電話番号"
                id="phoneNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="電話番号"
                value={newVendor.vendorPhoneNumber}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorPhoneNumber: e.target.value,
                    })
                }
            />

            {/* FAX番号 */}
            <FormInput
                label="FAX番号"
                id="faxNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="FAX番号"
                value={newVendor.vendorFaxNumber}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorFaxNumber: e.target.value,
                    })
                }
            />

            {/* メールアドレス */}
            <FormInput
                label="メールアドレス"
                id="emailAddress"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="メールアドレス"
                value={newVendor.vendorEmailAddress}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorEmailAddress: e.target.value,
                    })
                }
            />
        </div>
    );
};

interface VendorSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateVendor: () => void;
        handleCloseModal: () => void;
    };
    formItems: {
        newVendor: VendorType;
        setNewVendor: React.Dispatch<React.SetStateAction<VendorType>>;
    };
}

export const VendorSingleView: React.FC<VendorSingleViewProps> = ({
                                                                      error,
                                                                      message,
                                                                      isEditing,
                                                                      headerItems: {
                                                                          handleCreateOrUpdateVendor,
                                                                          handleCloseModal,
                                                                      },
                                                                      formItems: {
                                                                          newVendor,
                                                                          setNewVendor,
                                                                      },
                                                                  }) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="仕入先"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateVendor={handleCreateOrUpdateVendor}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newVendor={newVendor}
                        setNewVendor={setNewVendor}
                    />
                </div>
            </div>
        </div>
    </div>
);
