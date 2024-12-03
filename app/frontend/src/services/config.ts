import Env from "../env.ts";

const Config = () => {
    let config: { apiUrl: string, authHeader: string };
    const getApiUrl = () => Env.isProduction() ? Env.apiUrl : "http://localhost:8080/api";
    const user = window.localStorage.getItem("user");
    if (user) {
        config = {apiUrl: getApiUrl(), authHeader: "Bearer " + JSON.parse(user).token};
        return config;
    }
    config = {apiUrl: getApiUrl(), authHeader: ""};
    return config;
};

export default Config;
