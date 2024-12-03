const Env = (() => {
    const isProduction = (): boolean => process.env.NODE_ENV === "production";
    const apiUrl: string = import.meta.env.VITE_APP_API_URL;

    return {
        isProduction,
        apiUrl
    };
})();

export default Env;