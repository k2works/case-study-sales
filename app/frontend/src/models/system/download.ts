export enum DownloadTarget {
    部門 = "0",
    社員 = "1",
    商品分類 = "2",
    商品 = "3",
    取引先グループ = "4",
    取引先  = "5",
    顧客 = "6",
    仕入先 = "7",
    受注 = "8",
    出荷 = "9",
    売上 = "10",
    請求 = "11",
    入金 = "12",
    口座 = "13",
    発注 = "14",
    仕入 = "15",
    支払 = "16",
    在庫 = "17",
    倉庫 = "18",
    棚番 = "19"
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
