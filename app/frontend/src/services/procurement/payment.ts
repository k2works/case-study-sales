import Config from "../config";
import Utils from "../utils";
import {
    PaymentCriteriaType,
    PaymentFetchType,
    PaymentType,
    mapToPaymentCriteriaResource,
    mapToPaymentResource,
} from "../../models/procurement/payment.ts";

export interface PaymentServiceType {
    select: (page?: number, pageSize?: number) => Promise<PaymentFetchType>;
    find: (paymentNumber: string) => Promise<PaymentType>;
    create: (payment: PaymentType) => Promise<void>;
    update: (payment: PaymentType) => Promise<void>;
    destroy: (paymentNumber: string) => Promise<void>;
    search: (criteria: PaymentCriteriaType, page?: number, pageSize?: number) => Promise<PaymentFetchType>;
}

export const PaymentService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/purchase-payments`;

    const select = async (page?: number, pageSize?: number): Promise<PaymentFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<PaymentFetchType>(url);
    };

    const find = async (paymentNumber: string): Promise<PaymentType> => {
        const url = `${endPoint}/${paymentNumber}`;
        return await apiUtils.fetchGet<PaymentType>(url);
    };

    const create = async (payment: PaymentType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToPaymentResource(payment));
    };

    const update = async (payment: PaymentType): Promise<void> => {
        const url = `${endPoint}/${payment.paymentNumber}`;
        await apiUtils.fetchPut<void>(url, mapToPaymentResource(payment));
    };

    const search = async (criteria: PaymentCriteriaType, page?: number, pageSize?: number): Promise<PaymentFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<PaymentFetchType>(url, mapToPaymentCriteriaResource(criteria));
    };

    const destroy = async (paymentNumber: string): Promise<void> => {
        const url = `${endPoint}/${paymentNumber}`;
        await apiUtils.fetchDelete<void>(url);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search
    };
};
