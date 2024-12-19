import {UserAccountType} from "./user.ts";

export enum ApplicationExecutionProcessType {
    ユーザー登録 = "ユーザー登録",
    ユーザー更新 = "ユーザー更新",
    ユーザー削除 = "ユーザー削除",
    部門登録 = "部門登録",
    部門更新 = "部門更新",
    部門削除 = "部門削除",
    社員登録 = "社員登録",
    社員更新 = "社員更新",
    社員削除 = "社員削除",
    商品分類登録 = "商品分類登録",
    商品分類更新 = "商品分類更新",
    商品分類削除 = "商品分類削除",
    データダウンロード = "データダウンロード",
    その他 = "その他",
}

export enum ApplicationExecutionHistoryType {
    同期 = "同期",
    非同期 = "非同期",
}

export enum ApplicationExecutionProcessFlag {
    実行中 = "実行中",
    実行済 = "実行済",
    エラー = "エラー",
    未実行 = "未実行",
}

type Process = {
    processType: ApplicationExecutionProcessType;
    name: string;
    code: string;
};

export type SearchAuditConditionType = {
    processType?: ApplicationExecutionProcessType;
    processFlag?: ApplicationExecutionProcessFlag;
    type?: ApplicationExecutionHistoryType;
}

export type AuditType = {
    id: number;
    process: Process;
    type: ApplicationExecutionHistoryType;
    processStart: string; // ISO 8601フォーマットの日時文字列
    processEnd: string;
    processFlag: ApplicationExecutionProcessFlag;
    processDetails: string | null;
    user: UserAccountType;
    checked: boolean;
};

export const mapToAuditSearchResource = (condition: SearchAuditConditionType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        process?: {
            processType?: string;
        };
        processFlag?: string;
        type?: string;
    };
    const resource: Resource = {
        ...(isEmpty(condition.processFlag) ? {} : { processFlag: condition.processFlag }),
        process: {
            ...(isEmpty(condition.processType) ? {} : { processType: condition.processType }),
        },
        ...(isEmpty(condition.type) ? {} : { type: condition.type }),
    };

    return resource;
};

