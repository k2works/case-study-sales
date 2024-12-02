import Config from "./config.ts";

const Utils = (() => {
    const apiUtils = (() => {
        const fetchGet = async (url: string): Promise<any> => {
            const config = Config();
            try {
                const res = await fetch(url, {
                    method: "GET",
                    headers: {
                        "Authorization": config.authHeader
                    }
                });
                if (!res.ok) {
                    return res.json().then(e => {
                        throw e;
                    });
                }
                return await res.json();
            } catch (err: any) {
                console.log(err);
                if (err.message && err.message.includes("Unexpected token '<', \"<!DOCTYPE \"... is not valid JSON")) {
                    throw new Error("認証期限が切れました。再度ログインしてください。");
                }
                throw err;
            }
        };

        const fetchPost = async (url: string, body: any): Promise<any> => {
            const config = Config();
            try {
                const res = await fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": config.authHeader
                    },
                    body: JSON.stringify(body)
                });
                if (!res.ok) {
                    return res.json().then(e => {
                        throw e;
                    });
                }
                return await res.json();
            } catch (err) {
                console.log(err);
                throw err;
            }
        };

        const fetchPut = async (url: string, body: any): Promise<any> => {
            const config = Config();
            try {
                const res = await fetch(url, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": config.authHeader
                    },
                    body: JSON.stringify(body)
                });
                if (!res.ok) {
                    return res.json().then(e => {
                        throw e;
                    });
                }
                return await res.json();
            } catch (err) {
                console.log(err);
                throw err;
            }

        };

        const fetchDelete = async (url: string): Promise<any> => {
            const config = Config();
            try {
                const res = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        "Authorization": config.authHeader
                    }
                });
                if (!res.ok) {
                    return res.json().then(e => {
                        throw e;
                    });
                }
                return await res.json();
            } catch (err) {
                console.log(err);
                throw err;
            }
        };

        const fetchGetDownload = async (url: string): Promise<any> => {
            const config = Config();
            try {
                const res = await fetch(url, {
                    method: "GET",
                    headers: {
                        "Authorization": config.authHeader
                    }
                });
                if (!res.ok) {
                    return res.blob().then(e => {
                        throw e;
                    });
                }
                return await res.blob();
            } catch (err) {
                console.log(err);
                throw err;
            }
        };

        return {
            fetchGet,
            fetchPost,
            fetchPut,
            fetchDelete,
            fetchGetDownload
        };
    })();

    return {
        apiUtils,
    };
})();

export default Utils;
