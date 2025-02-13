import React from "react";
import { Message } from "../../../../components/application/Message.tsx";
import { PartnerGroupType } from "../../../../models/master/partner";
import { FormInput, SingleViewHeaderActions, SingleViewHeaderItem } from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdatePartnerGroup: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdatePartnerGroup,
                    handleCloseModal,
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdatePartnerGroup}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newPartnerGroup: PartnerGroupType;
    setNewPartnerGroup: React.Dispatch<React.SetStateAction<PartnerGroupType>>;
}

const Form = ({ isEditing, newPartnerGroup, setNewPartnerGroup }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="取引先グループコード"
                id="partnerGroupCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先グループコード"
                value={newPartnerGroup.partnerGroupCode?.value ?? ""}
                onChange={(e) =>
                    setNewPartnerGroup({
                        ...newPartnerGroup,
                        partnerGroupCode: { value: e.target.value },
                    })
                }
                disabled={isEditing}
            />
            <FormInput
                label="取引先グループ名"
                id="partnerGroupName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="取引先グループ名"
                value={newPartnerGroup.partnerGroupName ?? ""}
                onChange={(e) =>
                    setNewPartnerGroup({
                        ...newPartnerGroup,
                        partnerGroupName: e.target.value,
                    })
                }
            />
        </div>
    );
};

interface PartnerGroupSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdatePartnerGroup: () => void;
        handleCloseModal: () => void;
    };
    formItems: {
        newPartnerGroup: PartnerGroupType;
        setNewPartnerGroup: React.Dispatch<React.SetStateAction<PartnerGroupType>>;
    };
}

export const PartnerGroupSingleView: React.FC<PartnerGroupSingleViewProps> = ({
                                                                                  error,
                                                                                  message,
                                                                                  isEditing,
                                                                                  headerItems: {
                                                                                      handleCreateOrUpdatePartnerGroup,
                                                                                      handleCloseModal,
                                                                                  },
                                                                                  formItems: {
                                                                                      newPartnerGroup,
                                                                                      setNewPartnerGroup,
                                                                                  },
                                                                              }) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="取引先グループ"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdatePartnerGroup={handleCreateOrUpdatePartnerGroup}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newPartnerGroup={newPartnerGroup}
                        setNewPartnerGroup={setNewPartnerGroup}
                    />
                </div>
            </div>
        </div>
    </div>
);