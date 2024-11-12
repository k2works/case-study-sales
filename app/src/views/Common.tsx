import React from "react";

export const SingleViewHeaderItem: React.FC<{ title: string, subtitle: string }> = ({title, subtitle}) => (
    <div className="single-view-header-item">
        <h1 className="single-view-title">{title}</h1>
        <p className="single-view-subtitle">{subtitle}</p>
    </div>
);

export const SingleViewHeaderActions: React.FC<{
    isEditing: boolean,
    handleCreateOrUpdateUser: () => void,
    handleCloseModal: () => void
}> = ({isEditing, handleCreateOrUpdateUser, handleCloseModal}) => (
    <div className="single-view-header-item">
        <div className="button-container">
            <button className="action-button" onClick={handleCreateOrUpdateUser} id="save">
                {isEditing ? "更新" : "作成"}
            </button>
            <button className="action-button" onClick={handleCloseModal} id="cancel">キャンセル</button>
        </div>
    </div>
);

interface FormItemProps {
    label: string;
    children: React.ReactNode;
}

export const FormItem = ({label, children}: FormItemProps) => (
    <div className="single-view-content-item-form-item">
        <label className="single-view-content-item-form-item-label">{label}</label>
        {children}
    </div>
);

interface FormSelectProps<T> {
    id: string;
    label: string;
    className?: string;
    value: T;
    options: { [key: string]: T };
    onChange: (value: T) => void;
}

export const FormSelect = <T extends string>({id, label, className, value, options, onChange}: FormSelectProps<T>) => (
    <FormItem label={label}>
        <select
            id={id}
            className={className}
            value={value}
            onChange={(e) => onChange(e.target.value as T)}
        >
            <option value="" disabled selected hidden></option>
            {Object.entries(options).map(([key, val]) => (
                <option key={key} value={val}>{val}</option>
            ))}
        </select>
    </FormItem>
);

interface FormInputProps {
    label: string;
    id: string;
    type: string;
    className: string;
    placeholder?: string;
    value: string | number;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    disabled?: boolean;
}

export const FormInput: React.FC<FormInputProps> = ({
                                                 label,
                                                 id,
                                                 type,
                                                 className,
                                                 placeholder,
                                                 value,
                                                 onChange,
                                                 disabled,
                                             }) => {
    return (
        <FormItem label={label}>
            <input
                id={id}
                type={type}
                className={className}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                disabled={disabled}
            />
        </FormItem>
    );
};

