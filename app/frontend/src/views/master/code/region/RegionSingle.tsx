import React from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {RegionType} from "../../../../models/master/code";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateRegion: () => void;
    handleCloseModal: () => void;
}
const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateRegion,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateRegion}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newRegion: RegionType;
    setNewRegion: React.Dispatch<React.SetStateAction<RegionType>>;
}
const Form = ({ isEditing, newRegion, setNewRegion }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="地域コード"
                id="regionCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="地域コード"
                value={newRegion.regionCode}
                onChange={(e) => setNewRegion({
                    ...newRegion,
                    regionCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="地域名"
                id="regionName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="地域名"
                value={newRegion.regionName}
                onChange={(e) => setNewRegion({
                    ...newRegion,
                    regionName: e.target.value
                })}
            />
        </div>
    );
};

interface RegionSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateRegion: () => void;
        handleCloseModal: () => void;
    }
    formItems: {
        newRegion: RegionType;
        setNewRegion: React.Dispatch<React.SetStateAction<RegionType>>;
    }
}

export const RegionSingleView = ({
                                     error,
                                     message,
                                     isEditing,
                                     headerItems: {
                                         handleCreateOrUpdateRegion,
                                         handleCloseModal,
                                     },
                                     formItems: {
                                         newRegion,
                                         setNewRegion,
                                     }
                                 }: RegionSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="地域"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateRegion={handleCreateOrUpdateRegion}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newRegion={newRegion}
                        setNewRegion={setNewRegion}
                    />
                </div>
            </div>
        </div>
    </div>
);
