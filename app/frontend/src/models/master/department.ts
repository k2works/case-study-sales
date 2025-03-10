import {EmployeeResourceType, EmployeeType} from "./employee.ts";
import {PageNationType} from "../../views/application/PageNation.tsx";
import {toISOStringWithTimezone} from "../../components/application/utils.ts";

export type DepartmentType = {
    departmentCode: string;
    startDate: string;
    endDate: string;
    departmentName: string;
    layer: number;
    path: string;
    lowerType: LowerType;
    slitYn: SlitYnType;
    employees: EmployeeType[];
    checked?: boolean;
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
        departmentCode: department.departmentCode,
        startDate: toISOStringWithTimezone(new Date(department.startDate)),
        endDate: toISOStringWithTimezone(new Date(department.endDate)),
        departmentName: department.departmentName,
        layer: department.layer.toString(),
        path: department.path,
        lowerType: department.lowerType.toString(),
        slitYn: department.slitYn.toString(),
        employees: department.employees.map(employee => ({
            empCode: employee.empCode,
            empName: employee.empName,
            empNameKana: employee.empNameKana,
            tel: employee.tel,
            fax: employee.fax,
            occuCode: employee.occuCode,
            departmentCode: department.departmentCode,
            departmentStartDate: toISOStringWithTimezone(new Date(department.startDate)),
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
