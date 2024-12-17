export enum DownloadTarget {
    DEPARTMENT = "DEPARTMENT",
    EMPLOYEE = "EMPLOYEE",
    PRODUCT_CATEGORY = "PRODUCT_CATEGORY",
    PRODUCT = "PRODUCT",
}

export type DownloadConditionType = {
    target: DownloadTarget;
};

export const mapToDownloadResource = (condition: DownloadConditionType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;

    type Resource = {
        target: string;
    };

    if (isEmpty(condition.target)) {
        throw new Error("Target is required.");
    }

    const resource: Resource = {
        target: condition.target,
    };

    return resource;
};