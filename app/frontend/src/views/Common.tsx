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
    value?: T;
    options: { [key: string]: T };
    onChange: (value: T) => void;
    disabled?: boolean;
}

export const FormSelect = <T extends string>({
                                                 id,
                                                 label,
                                                 className,
                                                 value,
                                                 options,
                                                 onChange,
                                                 disabled, // 受け取り
                                             }: FormSelectProps<T>) => (
    <FormItem label={label}>
        <select
            id={id}
            className={className}
            value={value}
            onChange={(e) => onChange(e.target.value as T)}
            disabled={disabled} // "select"自体にも念のため "disabled"適用
        >
            {disabled ? (
                <option value="" disabled selected hidden></option>
            ) : (
                <option value=""></option>
            )}
            {Object.entries(options).map(([key, val]) => (
                <option key={key} value={val}>
                    {val}
                </option>
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
    value: string | number | undefined;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onClick?: () => void;
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
                                                 onClick,
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
                onClick={onClick}
                disabled={disabled}
            />
        </FormItem>
    );
};

interface FormTextareaProps {
    label: string;
    id: string;
    className?: string;
    placeholder?: string;
    value: string;
    onChange: (event: React.ChangeEvent<HTMLTextAreaElement>) => void;
    disabled?: boolean;
}

export const FormTextarea: React.FC<FormTextareaProps> = ({
                                                              label,
                                                              id,
                                                              className,
                                                              placeholder,
                                                              value,
                                                              onChange,
                                                              disabled,
                                                          }) => {
    return (
        <FormItem label={label}>
            <textarea
                id={id}
                className={className}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                disabled={disabled}
            />
        </FormItem>
    );
};

interface SearchProps<T> {
    searchCriteria: T;
    setSearchCriteria: (value: T) => void;
    handleSearchAudit: () => void;
}

export const Search = <T,>({
                               handleSearchAudit,
                           }: SearchProps<T>): React.ReactElement => {
    return (
        <div className="search-container">
            <div className="single-view-content-item-form">
                <div className="button-container">
                    <button className="action-button" id="search" onClick={handleSearchAudit}>
                        検索
                    </button>
                </div>
            </div>
        </div>
    );
};
