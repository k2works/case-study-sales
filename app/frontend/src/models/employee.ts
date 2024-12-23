import {DepartmentType} from './department';
import {UserAccountType} from './user';
import {toISOStringWithTimezone} from "../components/application/utils.ts";
import {PageNationType} from "../views/application/PageNation.tsx";

export type EmployeeType = {
    empCode: { value: string };
    empName: {
        firstName: string;
        lastName: string;
        firstNameKana: string;
        lastNameKana: string;
    }
    loginPassword: string;
    tel: {
        value: string;
        areaCode: string;
        localExchange: string;
        subscriberNumber: string;
    };
    fax: {
        value: string;
        areaCode: string;
        localExchange: string;
        subscriberNumber: string;
    };
    occuCode: {
        value: string;
    };
    approvalCode: string;
    department: DepartmentType;
    user: UserAccountType;
    addFlag: boolean;
    deleteFlag: boolean;
    checked: boolean;
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

export const mapToEmployeeResource = (employee: EmployeeType): EmployeeResourceType => {
    return {
        empCode: employee.empCode.value,
        empName: `${employee.empName.firstName} ${employee.empName.lastName}`,
        empNameKana: `${employee.empName.firstNameKana} ${employee.empName.lastNameKana}`,
        tel: employee.tel.value,
        fax: employee.fax.value,
        occuCode: employee.occuCode.value,
        departmentCode: employee.department?.departmentId.deptCode.value,
        departmentStartDate: toISOStringWithTimezone(new Date(employee.department?.departmentId.departmentStartDate.value)),
        userId: employee.user?.userId.value,
        addFlag: employee.addFlag,
        deleteFlag: employee.deleteFlag
    };
};
