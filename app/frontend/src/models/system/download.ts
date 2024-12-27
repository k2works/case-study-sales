export enum DownloadTarget {
    部門 = "0",
    社員 = "1",
    商品分類 = "2",
    商品 = "3"
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
