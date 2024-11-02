import {DepartmentType} from './department';
import {UserAccountType} from './user';

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
}

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

