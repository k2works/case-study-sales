import Config from "./config";
import Utils from "./utils";
import {DownloadConditionType, mapToDownloadResource} from "../models/download.ts";

export interface DownloadServiceType {
    download: (condition: DownloadConditionType) => Promise<Blob>;
    count: (condition: DownloadConditionType) => Promise<number>;
}

export const DownloadService = (): DownloadServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/downloads`;

    const download = (condition: DownloadConditionType): Promise<Blob> => {
        const url = `${endPoint}/download`;
        const resource = mapToDownloadResource(condition);
        return apiUtils.fetchPostDownload(url, resource);
    };

    const count = (condition: DownloadConditionType): Promise<number> => {
        const url = `${endPoint}/count`;
        const resource = mapToDownloadResource(condition);
        return apiUtils.fetchPost(url, resource);
    };

    return {
        download,
        count,
    };
};