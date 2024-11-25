import Config from "./config";
import {APIResponse} from "../models";

const AuthService = () => {
    const config = Config();

    const signIn = async (userId: string, password: string): Promise<APIResponse> => {
        const url = `${config.apiUrl}/auth/signin`;
        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    userId,
                    password
                })
            });

            return await response.json();
        } catch (error) {
            throw new Error(`サービスから応答がありません ${error}`);
        }
    };

    return {
        signIn
    };
};

export default AuthService;
