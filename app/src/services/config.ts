const Config = () => {
    let config: { apiUrl: string, authHeader: string };

    const isProduction = () => process.env.NODE_ENV === "production";
    const getApiUrl = () => isProduction() ? "https://case-study-sales-api-50defd76b8c9.herokuapp.com/api" : "http://localhost:8080/api";
    const user = window.localStorage.getItem("user");
    if (user) {
        config = {apiUrl: getApiUrl(), authHeader: "Bearer " + JSON.parse(user).token};
        return config;
    }
    config = {apiUrl: getApiUrl(), authHeader: ""};
    return config;
};

export default Config;
