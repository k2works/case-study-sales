import { PageNationType } from "../../views/application/PageNation";

// 出荷情報
export interface ShippingType {
    orderNumber: string;
    orderDate: string;
    departmentCode: string;
    departmentStartDate: string;
    customerCode: string;
    customerBranchNumber: number;
    employeeCode: string;
    desiredDeliveryDate: string;
    customerOrderNumber: string;
    warehouseCode: string;
    totalOrderAmount: number;
    totalConsumptionTax: number;
    remarks: string;
    orderLineNumber: number;
    productCode: string;
    productName: string;
    salesUnitPrice: number;
    orderQuantity: number;
    taxRate: number;
    allocationQuantity: number;
    shipmentInstructionQuantity: number;
    shippedQuantity: number;
    completionFlag: boolean;
    discountAmount: number;
    deliveryDate: string;
    shippingDate?: string;
    salesAmount: number;
    consumptionTaxAmount: number;
    checked?: boolean;
}

// 出荷検索条件
export interface ShippingCriteriaType {
    orderNumber?: string;
    orderDate?: string;
    departmentCode?: string;
    departmentStartDate?: string;
    customerCode?: string;
    customerBranchNumber?: number;
    employeeCode?: string;
    desiredDeliveryDate?: string;
    customerOrderNumber?: string;
    warehouseCode?: string;
    remarks?: string;
    orderLineNumber?: number;
    productCode?: string;
    productName?: string;
    deliveryDate?: string;
    completionFlag?: boolean;
}

// 出荷リソースをAPIリクエスト用に変換
export const mapToShippingResource = (shipping: ShippingType) => {
    return {
        orderNumber: shipping.orderNumber,
        orderDate: shipping.orderDate,
        departmentCode: shipping.departmentCode,
        departmentStartDate: shipping.departmentStartDate,
        customerCode: shipping.customerCode,
        customerBranchNumber: shipping.customerBranchNumber,
        employeeCode: shipping.employeeCode,
        desiredDeliveryDate: shipping.desiredDeliveryDate,
        customerOrderNumber: shipping.customerOrderNumber,
        warehouseCode: shipping.warehouseCode,
        totalOrderAmount: shipping.totalOrderAmount,
        totalConsumptionTax: shipping.totalConsumptionTax,
        remarks: shipping.remarks,
        orderLineNumber: shipping.orderLineNumber,
        productCode: shipping.productCode,
        productName: shipping.productName,
        salesUnitPrice: shipping.salesUnitPrice,
        orderQuantity: shipping.orderQuantity,
        taxRate: shipping.taxRate,
        allocationQuantity: shipping.allocationQuantity,
        shipmentInstructionQuantity: shipping.shipmentInstructionQuantity,
        shippedQuantity: shipping.shippedQuantity,
        completionFlag: shipping.completionFlag,
        discountAmount: shipping.discountAmount,
        deliveryDate: shipping.deliveryDate,
        salesAmount: shipping.salesAmount,
        consumptionTaxAmount: shipping.consumptionTaxAmount
    };
};

// 検索条件をAPIリクエスト用に変換
export const mapToShippingCriteriaResource = (criteria: ShippingCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.orderNumber) ? {} : { orderNumber: criteria.orderNumber }),
        ...(isEmpty(criteria.orderDate) ? {} : { orderDate: criteria.orderDate }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
        ...(isEmpty(criteria.departmentStartDate) ? {} : { departmentStartDate: criteria.departmentStartDate }),
        ...(isEmpty(criteria.customerCode) ? {} : { customerCode: criteria.customerCode }),
        ...(isEmpty(criteria.customerBranchNumber) ? {} : { customerBranchNumber: criteria.customerBranchNumber }),
        ...(isEmpty(criteria.employeeCode) ? {} : { employeeCode: criteria.employeeCode }),
        ...(isEmpty(criteria.desiredDeliveryDate) ? {} : { desiredDeliveryDate: criteria.desiredDeliveryDate }),
        ...(isEmpty(criteria.customerOrderNumber) ? {} : { customerOrderNumber: criteria.customerOrderNumber }),
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.remarks) ? {} : { remarks: criteria.remarks }),
        ...(isEmpty(criteria.orderLineNumber) ? {} : { orderLineNumber: criteria.orderLineNumber }),
        ...(isEmpty(criteria.productCode) ? {} : { productCode: criteria.productCode }),
        ...(isEmpty(criteria.productName) ? {} : { productName: criteria.productName }),
        ...(isEmpty(criteria.deliveryDate) ? {} : { deliveryDate: criteria.deliveryDate }),
        // completionFlagはブーリアン値なので、特別な処理を行う
        completionFlag: criteria.completionFlag ?? false
    };
};

// 出荷一覧のページネーション情報
export interface ShippingPageInfoType {
    list: ShippingType[];
    pageNum: number;
    pageSize: number;
    size: number;
    startRow: number;
    endRow: number;
    pages: number;
    prePage: number;
    nextPage: number;
    isFirstPage: boolean;
    isLastPage: boolean;
    hasPreviousPage: boolean;
    hasNextPage: boolean;
    navigatePages: number;
    navigatepageNums: number[];
    navigateFirstPage: number;
    navigateLastPage: number;
    total: number;
}

// 初期値
export const initialShipping: ShippingType = {
    orderNumber: "",
    orderDate: "",
    departmentCode: "",
    departmentStartDate: "",
    customerCode: "",
    customerBranchNumber: 0,
    employeeCode: "",
    desiredDeliveryDate: "",
    customerOrderNumber: "",
    warehouseCode: "",
    totalOrderAmount: 0,
    totalConsumptionTax: 0,
    remarks: "",
    orderLineNumber: 0,
    productCode: "",
    productName: "",
    salesUnitPrice: 0,
    orderQuantity: 0,
    taxRate: 0,
    allocationQuantity: 0,
    shipmentInstructionQuantity: 0,
    shippedQuantity: 0,
    completionFlag: false,
    discountAmount: 0,
    deliveryDate: "",
    salesAmount: 0,
    consumptionTaxAmount: 0,
    checked: false
};

export const initialShippingCriteria: ShippingCriteriaType = {
    orderNumber: "",
    orderDate: "",
    departmentCode: "",
    customerCode: "",
    productCode: "",
    productName: "",
    completionFlag: undefined
};

export const initialShippingPageNation: PageNationType = {
    pageNum: 1,
    pageSize: 10,
    size: 0,
    startRow: 0,
    endRow: 0,
    total: 0,
    pages: 0,
    list: [],
    prePage: 0,
    nextPage: 0,
    isFirstPage: true,
    isLastPage: true,
    hasPreviousPage: false,
    hasNextPage: false,
    navigatePages: 8,
    navigatepageNums: [1],
    navigateFirstPage: 1,
    navigateLastPage: 1
};