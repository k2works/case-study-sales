export type UserType = {
    userId: string;
    token: string;
    roles: RoleType[];
}

export const RoleType = {
    ADMIN: 'ROLE_ADMIN',
    USER: 'ROLE_USER'
}
export type RoleType = typeof RoleType[keyof typeof RoleType];
export const AllRoles = Object.values(RoleType);

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

