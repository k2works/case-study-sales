import {EmployeeResourceType, EmployeeType} from './employee';

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
}

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

