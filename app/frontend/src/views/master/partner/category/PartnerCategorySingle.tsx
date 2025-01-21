import React from 'react';
import { Message } from "../../../../components/application/Message.tsx";
import { PartnerCategoryType } from "../../../../models";
import { FormInput, SingleViewHeaderActions, SingleViewHeaderItem } from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdatePartnerCategory: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdatePartnerCategory,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdatePartnerCategory}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newPartnerCategory: PartnerCategoryType;
    setNewPartnerCategory: React.Dispatch<React.SetStateAction<PartnerCategoryType>>;
}

const Form = ({ isEditing, newPartnerCategory, setNewPartnerCategory }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="取引先分類種別コード"
                id="partnerCategoryTypeCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先分類種別コード"
                value={newPartnerCategory.partnerCategoryTypeCode}
                onChange={(e) => setNewPartnerCategory({
                    ...newPartnerCategory,
                    partnerCategoryTypeCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="取引先分類種別名"
                id="partnerCategoryTypeName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先分類種別名"
                value={newPartnerCategory.partnerCategoryTypeName}
                onChange={(e) => setNewPartnerCategory({
                    ...newPartnerCategory,
                    partnerCategoryTypeName: e.target.value
                })}
            />
        </div>
    );
};

interface PartnerCategorySingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdatePartnerCategory: () => void;
        handleCloseModal: () => void;
    };
    formItems: {
        newPartnerCategory: PartnerCategoryType;
        setNewPartnerCategory: React.Dispatch<React.SetStateAction<PartnerCategoryType>>;
    };
}

export const PartnerCategorySingleView = ({
                                              error,
                                              message,
                                              isEditing,
                                              headerItems: {
                                                  handleCreateOrUpdatePartnerCategory,
                                                  handleCloseModal,
                                              },
                                              formItems: {
                                                  newPartnerCategory,
                                                  setNewPartnerCategory,
                                              }
                                          }: PartnerCategorySingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="取引先分類"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdatePartnerCategory={handleCreateOrUpdatePartnerCategory}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newPartnerCategory={newPartnerCategory}
                        setNewPartnerCategory={setNewPartnerCategory}
                    />
                </div>
            </div>
        </div>
    </div>
);