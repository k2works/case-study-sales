import {EmployeeResourceType, EmployeeType} from './employee';
import {toISOStringWithTimezone} from "../components/application/utils.ts";
import {PageNationType} from "../views/application/PageNation.tsx";
import {AuditCriteriaType} from "./audit.ts";

export type DepartmentIdType = {
    deptCode: { value: string };
    departmentStartDate: { value: string };
}

export type DepartmentType = {
    departmentId: DepartmentIdType;
    endDate: { value: string }
    departmentName: string;
    layer: number;
    path: { value: string };
    lowerType: LowerType;
    slitYn: SlitYnType;
    employees: EmployeeType[];
    checked: boolean;
}

export type DepartmentFetchType = {
    list: DepartmentType[];
} & PageNationType;

export type DepartmentResourceType = {
    departmentCode: string;
    startDate: string;
    endDate: string;
    departmentName: string;
    layer: string;
    path: string;
    lowerType: string;
    slitYn: string;
    employees: EmployeeResourceType[];
}

export type DepartmentCriteriaType = {
    departmentCode?: string;
    departmentName?: string;
    startDate?: string;
    endDate?: string;
}

export const LowerType = {
    YES: "LOWER",
    NO: "NOT_LOWER",
}
export type LowerType = typeof LowerType[keyof typeof LowerType];

export const SlitYnType = {
    YES: "SLIT",
    NO: "NOT_SLIT",
}
export type SlitYnType = typeof SlitYnType[keyof typeof SlitYnType];

export const mapToDepartmentResource = (department: DepartmentType): DepartmentResourceType => {
    return {
        departmentCode: department.departmentId.deptCode.value,
        startDate: toISOStringWithTimezone(new Date(department.departmentId.departmentStartDate.value)),
        endDate: toISOStringWithTimezone(new Date(department.endDate.value)),
        departmentName: department.departmentName,
        layer: department.layer.toString(),
        path: department.path.value,
        lowerType: department.lowerType.toString(),
        slitYn: department.slitYn.toString(),
        employees: department.employees.map(employee => ({
            empCode: employee.empCode.value,
            empName: employee.empName.firstName + " " + employee.empName.lastName,
            empNameKana: employee.empName.firstNameKana + " " + employee.empName.lastNameKana,
            tel: employee.tel.value,
            fax: employee.fax.value,
            occuCode: employee.occuCode.value,
            departmentCode: department.departmentId.deptCode.value,
            departmentStartDate: toISOStringWithTimezone(new Date(department.departmentId.departmentStartDate.value)),
            userId: employee.user?.userId.value,
            addFlag: employee.addFlag,
            deleteFlag: employee.deleteFlag
        }))
    };
};

export const mapToDepartmentCriteriaResource = (criteria: DepartmentCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        departmentCode?: string;
        departmentName?: string;
        startDate?: string;
        endDate?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
        ...(isEmpty(criteria.departmentName) ? {} : { departmentName: criteria.departmentName }),
        ...(isEmpty(criteria.startDate) ? {} : { startDate: toISOStringWithTimezone(new Date(criteria.startDate))}),
        ...(isEmpty(criteria.endDate) ? {} : { endDate: toISOStringWithTimezone(new Date(criteria.endDate))}),
    };

    return resource;
};
