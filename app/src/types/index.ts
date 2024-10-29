export const RoleType = {
    ADMIN: 'ROLE_ADMIN',
    USER: 'ROLE_USER'
}
export type RoleType = typeof RoleType[keyof typeof RoleType];
export const AllRoles = Object.values(RoleType);

export type UserType = {
    userId: string;
    token: string;
    roles: RoleType[];
}

export type CustomLocation = {
    state: { from: { pathname: string } }
};

export type DataType = {
    userId: string;
    type: string;
    accessToken: string;
    roles: string[];
}

export type APIResponse = {
    token?: string;
    message?: string;
};

export type UserAccountType = {
    userId: { value: string };
    name: {
        firstName?: string;
        lastName?: string;
    }
    password?: {
        value: string
    };
    roleName?: string;
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
}

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
}
