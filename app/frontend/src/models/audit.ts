import {UserAccountType} from "./user.ts";

export enum ApplicationExecutionProcessType {
    USER_CREATE = "USER_CREATE",
    USER_UPDATE = "USER_UPDATE",
    USER_DELETE = "USER_DELETE",
    DEPARTMENT_CREATE = "DEPARTMENT_CREATE",
    DEPARTMENT_UPDATE = "DEPARTMENT_UPDATE",
    DEPARTMENT_DELETE = "DEPARTMENT_DELETE",
    EMPLOYEE_CREATE = "EMPLOYEE_CREATE",
    EMPLOYEE_UPDATE = "EMPLOYEE_UPDATE",
    EMPLOYEE_DELETE = "EMPLOYEE_DELETE",
    PRODUCT_CREATE = "PRODUCT_CREATE",
    PRODUCT_UPDATE = "PRODUCT_UPDATE",
    PRODUCT_DELETE = "PRODUCT_DELETE",
    OTHER = "OTHER",
}

export enum ApplicationExecutionHistoryType {
    SYNC = "SYNC",
    ASYNC = "ASYNC",
}

export enum ApplicationExecutionProcessFlag {
    START = "START",
    END = "END",
    ERROR = "ERROR",
    NOT_EXECUTED = "NOT_EXECUTED",
}

type Process = {
    processType: ApplicationExecutionProcessType;
    name: string;
    code: string;
};

export type searchAuditCondition = {
    processType: ApplicationExecutionProcessType;
    processFlag: ApplicationExecutionProcessFlag;
    type: ApplicationExecutionHistoryType;
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