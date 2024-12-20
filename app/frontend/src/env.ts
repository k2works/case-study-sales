const Env = (() => {
    const isProduction = (): boolean => process.env.NODE_ENV === "production";
    const isStaging = (): boolean => process.env.VITE_MODE === "staging";
    const prdApiUrl = ((): string => {
        if (isProduction()) {
            return process.env.VITE_APP_API_URL as string;
        }
        return "" as string
    })();
    const devApiUrl = ((): string => {
        if (isStaging()) {
            return process.env.VITE_STAGING_API_URL as string;
        }
        return "http://localhost:8080/api" as string
    })();
    const currentEnv = (): string => {
        if (isProduction()) {
            return "Production";
        }
        if (isStaging()) {
            return "Staging";
        }
        return "Development";
    }

    return {
        isProduction,
        prdApiUrl,
        devApiUrl,
        currentEnv
    };
})();

export default Env;