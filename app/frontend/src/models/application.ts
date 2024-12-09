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

