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
    token: string;
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
