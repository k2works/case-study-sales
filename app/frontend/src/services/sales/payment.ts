import Config from "../config";
import Utils from "../utils";
import {
    PaymentCriteriaType,
    PaymentPageInfoType,
    PaymentType,
    mapToPaymentCriteriaResource,
    mapToPaymentResource
} from "../../models/sales/payment";

export interface PaymentAggregateResponse {
    message: string;
    details: string[];
}

export interface PaymentServiceType {
    select: (page?: number, pageSize?: number) => Promise<PaymentPageInfoType>;
    find: (paymentNumber: string) => Promise<PaymentType>;
    create: (payment: PaymentType) => Promise<void>;
    update: (payment: PaymentType) => Promise<void>;
    search: (criteria: PaymentCriteriaType, page?: number, pageSize?: number) => Promise<PaymentPageInfoType>;
    destroy: (paymentNumber: string) => Promise<void>;
    aggregate: () => Promise<PaymentAggregateResponse>;
}

export const PaymentService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/payments`;

    const select = async (page?: number, pageSize?: number): Promise<PaymentPageInfoType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<PaymentPageInfoType>(url);
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

    const search = async (criteria: PaymentCriteriaType, page?: number, pageSize?: number): Promise<PaymentPageInfoType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<PaymentPageInfoType>(url, mapToPaymentCriteriaResource(criteria));
    };

    const destroy = async (paymentNumber: string): Promise<void> => {
        const url = `${endPoint}/${paymentNumber}`;
        await apiUtils.fetchDelete<void>(url);
    };

    const aggregate = async (): Promise<PaymentAggregateResponse> => {
        const url = `${endPoint}/aggregate`;
        return await apiUtils.fetchPost<PaymentAggregateResponse>(url, {});
    };

    return {
        select,
        find,
        create,
        update,
        search,
        destroy,
        aggregate
    };
}
