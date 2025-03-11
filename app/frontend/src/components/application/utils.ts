/**
 * エラーオブジェクトからエラーメッセージを抽出する
 * @param error エラーオブジェクト
 * @returns エラーメッセージ
 */
export const extractErrorMessage = (error: unknown): string => {
    let errorMessage: string;

    if (error && typeof error === "object" && "message" in error) {
        // error がオブジェクトで message プロパティを持つ場合
        errorMessage = (error as { message: string }).message;
    } else if (error instanceof Error) {
        // Error クラスのインスタンスの場合
        errorMessage = error.message;
    } else {
        // それ以外の場合は未知のエラーとして扱う
        errorMessage = '不明なエラーが発生しました';
    }

    return errorMessage;
};

/**
 * エラーメッセージを表示する
 * @param error エラーメッセージまたはエラーオブジェクト
 * @param callback エラーメッセージを処理するコールバック関数
 * @param prefix エラーメッセージの接頭辞（オプション）
 */
export const showErrorMessage = (error: string | unknown, callback: (error: string) => void, prefix?: string) => {
    let errorMessage: string;

    if (typeof error === 'string') {
        errorMessage = error;
    } else {
        errorMessage = extractErrorMessage(error);
        if (prefix) {
            errorMessage = `${prefix}: ${errorMessage}`;
        }
    }

    console.error(errorMessage);
    callback(errorMessage);
};

export const convertToDateInputFormat = (dateString: string): string => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
};

export const convertToDateTimeInputFormat = (dateString: string): string => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    const hours = ("0" + date.getHours()).slice(-2);
    const minutes = ("0" + date.getMinutes()).slice(-2);
    return `${year}-${month}-${day}:${hours}:${minutes}`;
};

export const toISOStringWithTimezone = (date: Date): string => {
    const pad = function (str: string): string {
        return ('0' + str).slice(-2);
    }
    const year = (date.getFullYear()).toString();
    const month = pad((date.getMonth() + 1).toString());
    const day = pad(date.getDate().toString());
    const hour = pad(date.getHours().toString());
    const min = pad(date.getMinutes().toString());
    const sec = pad(date.getSeconds().toString());
    const tz = -date.getTimezoneOffset();
    const sign = tz >= 0 ? '+' : '-';
    const tzHour = pad((tz / 60).toString());
    const tzMin = pad((tz % 60).toString());

    return `${year}-${month}-${day}T${hour}:${min}:${sec}${sign}${tzHour}:${tzMin}`;
}

export const toISOStringLocal = (date: Date): string => {
    const pad = function (str: string): string {
        return ('0' + str).slice(-2);
    }
    const year = (date.getFullYear()).toString();
    const month = pad((date.getMonth() + 1).toString());
    const day = pad(date.getDate().toString());
    const hour = pad(date.getHours().toString());
    const min = pad(date.getMinutes().toString());
    const sec = pad(date.getSeconds().toString());
    const tz = -date.getTimezoneOffset();

    return `${year}-${month}-${day}T${hour}:${min}:${sec}`;
}
