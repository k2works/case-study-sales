// ジェネリクスを使用した汎用的な値からシンボルを取得する関数
export const getSymbolByValue = <T extends Record<string, string | number>>(
    obj: T,
    value: T[keyof T]
): T[keyof T] | undefined => {
    const entries = Object.entries(obj) as [keyof T, T[keyof T]][];
    for (const [key, val] of entries) {
        if (val === value) {
            return obj[key];
        }
    }
    return undefined; // 該当する値がない場合
};

// ジェネリクスを使用した汎用的なシンボルからキー名を取得する関数
export const getKeyBySymbol = <T extends Record<string, string | number>>(
    obj: T,
    symbol: T[keyof T]
): keyof T | undefined => {
    const entries = Object.entries(obj) as [keyof T, T[keyof T]][];
    for (const [key, val] of entries) {
        if (val === symbol) {
            return key;
        }
    }
    return undefined; // 該当するキーがない場合
};