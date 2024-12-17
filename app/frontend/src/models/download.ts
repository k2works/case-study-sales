export enum DownloadTarget {
    部門 = "0",
    社員 = "1",
    商品分類 = "2",
    商品 = "3"
}

export type DownloadConditionType = {
    target: DownloadTarget;
};

// 値からシンボルを取得する関数
export const getSymbolByValue = (value: string): DownloadTarget | undefined => {
    // すべてのキーを取得して逆引き
    const entries = Object.entries(DownloadTarget);
    for (const [key, val] of entries) {
        if (val === value) {
            return DownloadTarget[key as keyof typeof DownloadTarget];
        }
    }
    return undefined; // 該当する値がない場合
};

// シンボルからキー名を取得する関数
export const getKeyBySymbol = (symbol: DownloadTarget): string | undefined => {
    const entries = Object.entries(DownloadTarget);
    for (const [key, val] of entries) {
        if (val === symbol) {
            return key;
        }
    }
    return undefined; // 該当するキーがない場合
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
