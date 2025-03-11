import {toISOStringWithTimezone} from "../../components/application/utils.ts";
import {PageNationType} from "../../views/application/PageNation.tsx";

export type EmployeeType = {
    empCode: string;
    empFirstName: string;
    empLastName: string;
    empFirstNameKana: string;
    empLastNameKana: string;
    loginPassword: string;
    tel: string;
    fax: string;
    occuCode: string;
    approvalCode: string;
    departmentCode: string;
    departmentStartDate: string;
    departmentName: string;
    userId: string;
    addFlag: boolean;
    deleteFlag: boolean;
    checked?: boolean;
}

export type EmployeeFetchType = {
    list: EmployeeType[];
} & PageNationType;

export type EmployeeResourceType = {
    empCode: string;
    empFirstName: string;
    empLastName: string;
    empFirstNameKana: string;
    empLastNameKana: string;
    tel: string;
    fax: string;
    occuCode: string;
    departmentCode: string;
    departmentStartDate: string;
    departmentName: string;
    userId: string;
    addFlag: boolean;
    deleteFlag: boolean;
}

export type EmployeeCriteriaType = {
    empCode?: string;
    empNameFirst?: string;
    empNameLast?: string;
    empNameFirstKana?: string;
    empNameLastKana?: string;
    phoneNumber?: string;
    faxNumber?: string;
    departmentCode?: string;
}

export const mapToEmployeeResource = (employee: EmployeeType): EmployeeResourceType => {
    return {
        empCode: employee.empCode,
        empFirstName: employee.empFirstName,
        empLastName: employee.empLastName,
        empFirstNameKana: employee.empFirstNameKana,
        empLastNameKana: employee.empLastNameKana,
        tel: employee.tel,
        fax: employee.fax,
        occuCode: employee.occuCode,
        departmentCode: employee.departmentCode,
        departmentStartDate: toISOStringWithTimezone(new Date(employee.departmentStartDate)),
        departmentName: employee.departmentName,
        userId: employee.userId,
        addFlag: employee.addFlag,
        deleteFlag: employee.deleteFlag
    };
};

export const mapToEmployeeCriteriaResource = (criteria: EmployeeCriteriaType) : EmployeeCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.empCode) ? {} : {employeeCode: criteria.empCode}),
        ...(isEmpty(criteria.empNameFirst) ? {} : {employeeFirstName: criteria.empNameFirst}),
        ...(isEmpty(criteria.empNameLast) ? {} : {employeeLastName: criteria.empNameLast}),
        ...(isEmpty(criteria.empNameFirstKana) ? {} : {employeeFirstNameKana: criteria.empNameFirstKana}),
        ...(isEmpty(criteria.empNameLastKana) ? {} : {employeeLastNameKana: criteria.empNameLastKana}),
        ...(isEmpty(criteria.phoneNumber) ? {} : {phoneNumber: criteria.phoneNumber}),
        ...(isEmpty(criteria.faxNumber) ? {} : {faxNumber: criteria.faxNumber}),
        ...(isEmpty(criteria.departmentCode) ? {} : {departmentCode: criteria.departmentCode}),
    };
}
