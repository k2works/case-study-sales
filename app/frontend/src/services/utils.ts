import Config from "./config.ts";

type ApiResponse<T> = T;
type ApiResult = {
    message: string;
    details?: Array<{ [key: string]: string }>;
};

const Utils = (() => {
    const apiUtils = (() => {
        const fetchGet = async <T>(url: string): Promise<ApiResponse<T>> => {
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

        const fetchPost = async <T>(url: string, body: unknown): Promise<ApiResponse<T>> => {
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

        const fetchPut = async <T>(url: string, body: unknown): Promise<ApiResponse<T>> => {
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

        const fetchDelete = async <T>(url: string): Promise<ApiResponse<T>> => {
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
                if (!(err instanceof Error)) {
                    throw new Error('Unknown error occurred');
                }
                console.log(err);
                throw err;
            }
        };

        const fetchGetDownload = async (url: string): Promise<Blob> => {
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

        const fetchPostDownload = async (url: string, body: unknown): Promise<Blob> => {
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

        const fetchPostFormData = async <T>(url: string, formData: FormData): Promise<ApiResponse<T>> => {
            const config = Config();
            try {
                const res = await fetch(url, {
                    method: "POST",
                    headers: {
                        "Authorization": config.authHeader
                    },
                    body: formData
                });
                if (!res.ok) {
                    const errorResponse: ApiResult = await res.json();
                    throw new Error(errorResponse.message);
                }
                return await res.json();
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
            fetchGetDownload,
            fetchPostDownload,
            fetchPostFormData
        };
    })();

    const buildUrlWithPaging = (baseUrl: string, page?: number, pageSize?: number): string => {
        const params = new URLSearchParams();
        if (pageSize) params.append("pageSize", pageSize.toString());
        if (page) params.append("page", page.toString());
        return params.toString() ? `${baseUrl}?${params.toString()}` : baseUrl;
    };

    return {
        apiUtils,
        buildUrlWithPaging
    };
})();

export default Utils;
