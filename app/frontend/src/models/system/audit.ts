import {PageNationType} from "../../views/application/PageNation.tsx";
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
    商品登録 = "商品登録",
    商品更新 = "商品更新",
    商品削除 = "商品削除",
    取引先グループ登録 = "取引先グループ登録",
    取引先グループ更新 = "取引先グループ更新",
    取引先グループ削除 = "取引先グループ削除",
    取引先分類種別登録 = "取引先分類登録",
    取引先分類種別更新 = "取引先分類更新",
    取引先分類種別削除 = "取引先分類削除",
    地域登録 = "地域登録",
    地域更新 = "地域更新",
    地域削除 = "地域削除",
    取引先登録 = "取引先登録",
    取引先更新 = "取引先更新",
    取引先削除 = "取引先削除",
    顧客登録 = "顧客登録",
    顧客更新 = "顧客更新",
    顧客削除 = "顧客削除",
    仕入先登録 = "仕入先登録",
    仕入先更新 = "仕入先更新",
    仕入先削除 = "仕入先削除",
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

export type AuditCriteriaType = {
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

export type AuditFetchType = {
    list: AuditType[];
} & PageNationType;

export const mapToCriteriaResource = (criteria: AuditCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        process?: {
            processType?: string;
        };
        processFlag?: string;
        type?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.processFlag) ? {} : { processFlag: criteria.processFlag }),
        process: {
            ...(isEmpty(criteria.processType) ? {} : { processType: criteria.processType }),
        },
        ...(isEmpty(criteria.type) ? {} : { type: criteria.type }),
    };

    return resource;
};

