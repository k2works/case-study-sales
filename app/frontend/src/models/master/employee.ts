import {DepartmentType} from './department.ts';
import {toISOStringWithTimezone} from "../../components/application/utils.ts";
import {PageNationType} from "../../views/application/PageNation.tsx";
import {UserAccountType} from "../system/user.ts";

export type EmployeeType = {
    empCode: string;
    empName: string;
    empNameKana: string;
    loginPassword: string;
    tel: string;
    fax: string;
    occuCode: string;
    approvalCode: string;
    department: DepartmentType;
    user: UserAccountType;
    addFlag: boolean;
    deleteFlag: boolean;
    checked?: boolean;
}

export type EmployeeFetchType = {
    list: EmployeeType[];
} & PageNationType;

export type EmployeeResourceType = {
    empCode: string;
    empName: string;
    empNameKana: string;
    tel: string;
    fax: string;
    occuCode: string;
    departmentCode: string;
    departmentStartDate: string;
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
        empName: employee.empName,
        empNameKana: employee.empNameKana,
        tel: employee.tel,
        fax: employee.fax,
        occuCode: employee.occuCode,
        departmentCode: employee.department?.departmentCode,
        departmentStartDate: toISOStringWithTimezone(new Date(employee.department?.startDate)),
        userId: employee.user?.userId.value,
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
