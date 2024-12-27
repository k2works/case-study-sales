import {PageNationType} from "../../views/application/PageNation.tsx";

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

export type UserFetchType = {
    list: UserAccountType[];
} & PageNationType;

export type UserAccountResourceType = {
    userId: string;
    password: string | undefined;
    firstName: string | undefined;
    lastName: string | undefined;
    roleName: string | undefined;
}

export enum RoleNameEnumType {
    ROLE_ADMIN = 'ADMIN',
    ROLE_USER = 'USER'
}

export const mapToUserAccountResource = (user: UserAccountType): UserAccountResourceType => {
    return {
        userId: user.userId.value,
        password: user.password?.value,
        firstName: user.name.firstName,
        lastName: user.name.lastName,
        roleName: user.roleName
    };
}