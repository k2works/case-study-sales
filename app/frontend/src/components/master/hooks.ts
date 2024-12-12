import {useState} from "react";
import {
    DepartmentIdType,
    DepartmentType,
    EmployeeType,
    LowerType,
    ProductCategoryType,
    ProductType,
    SlitYnType
} from "../../models";
import {DepartmentService, DepartmentServiceType} from "../../services/department.ts";
import {EmployeeService, EmployeeServiceType} from "../../services/employee.ts";
import {PageNationType} from "../../views/application/PageNation.tsx";
import {ProductCategoryService, ProductCategoryServiceType} from "../../services/product_category.ts";
import {ProductService, ProductServiceType} from "../../services/product.ts";
import {useFetchEntities} from "../application/hooks.ts";

export const useDepartment = () => {
    const initialDepartment = {
        departmentId: {deptCode: {value: ""}, departmentStartDate: {value: ""}},
        endDate: {value: ""},
        departmentName: "",
        layer: 0,
        path: {value: ""},
        lowerType: LowerType.NO,
        slitYn: SlitYnType.NO,
        employees: [],
        checked: false
    };

    const [departments, setDepartments] = useState<DepartmentType[]>([]);
    const [newDepartment, setNewDepartment] = useState<DepartmentType>(initialDepartment);
    const [searchDepartmentId, setSearchDepartmentId] = useState<DepartmentIdType>({
        deptCode: {value: ""},
        departmentStartDate: {value: ""}
    });
    const departmentService = DepartmentService();

    return {
        initialDepartment,
        departments,
        newDepartment,
        setNewDepartment,
        searchDepartmentId,
        setSearchDepartmentId,
        setDepartments,
        departmentService,
    }
}

export const useFetchDepartments = (
    setLoading: (loading: boolean) => void,
    setList: (list: DepartmentType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: DepartmentServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "部門情報の取得に失敗しました:");


export const useEmployee = () => {
    const initialEmployee: EmployeeType = {
        empCode: {value: ""},
        empName: {
            firstName: "",
            lastName: "",
            firstNameKana: "",
            lastNameKana: ""
        },
        loginPassword: "",
        tel: {
            value: "",
            areaCode: "",
            localExchange: "",
            subscriberNumber: ""
        },
        fax: {
            value: "",
            areaCode: "",
            localExchange: "",
            subscriberNumber: ""
        },
        occuCode: {
            value: ""
        },
        approvalCode: "",
        department: {
            departmentId: {deptCode: {value: ""}, departmentStartDate: {value: ""}},
            endDate: {value: ""},
            departmentName: "",
            layer: 0,
            path: {value: ""},
            lowerType: LowerType.NO,
            slitYn: SlitYnType.NO,
            employees: [],
            checked: false
        },
        user: {
            userId: {value: ""},
            name: {
                firstName: "",
                lastName: ""
            },
            password: {
                value: ""
            },
            roleName: ""
        },
        addFlag: false,
        deleteFlag: false,
        checked: false
    };

    const [employees, setEmployees] = useState<EmployeeType[]>([]);
    const [newEmployee, setNewEmployee] = useState<EmployeeType>(initialEmployee);
    const [searchEmployeeCode, setSearchEmployeeCode] = useState<string>("");

    const employeeService = EmployeeService();

    return {
        initialEmployee,
        employees,
        newEmployee,
        setNewEmployee,
        setEmployees,
        searchEmployeeCode,
        setSearchEmployeeCode,
        employeeService,
    };
}

export const useFetchEmployees = (
    setLoading: (loading: boolean) => void,
    setList: (list: EmployeeType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: EmployeeServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "社員情報の取得に失敗しました:");

export const useProductCategory = () => {
    const initialProductCategory: ProductCategoryType = {
        productCategoryCode: {value: ""},
        productCategoryName: "",
        productCategoryHierarchy: 0,
        productCategoryPath: "",
        lowestLevelDivision: 0,
        products: [],
        checked: false
    };

    const [productCategories, setProductCategories] = useState<ProductCategoryType[]>([]);
    const [newProductCategory, setNewProductCategory] = useState<ProductCategoryType>(initialProductCategory);
    const [searchProductCategoryCode, setSearchProductCategoryCode] = useState<string>("");
    const productCategoryService = ProductCategoryService();

    return {
        initialProductCategory,
        productCategories,
        newProductCategory,
        setNewProductCategory,
        setProductCategories,
        searchProductCategoryCode,
        setSearchProductCategoryCode,
        productCategoryService
    };
};

export const useFetchProductCategories = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductCategoryType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductCategoryServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "商品分類情報の取得に失敗しました:");

export const useProduct = () => {
    const initialProduct: ProductType = {
        productCode: {
            value: "",
            businessType: "",
            itemType: "",
            livestockType: "",
            serialNumber: 0,
        },
        productName: {
            productFormalName: "",
            productAbbreviation: "",
            productNameKana: "",
        },
        productType: "",
        sellingPrice: {amount: 0, currency: ""},
        purchasePrice: {amount: 0, currency: ""},
        costOfSales: {amount: 0, currency: ""},
        taxType: "",
        productCategoryCode: {value: ""},
        miscellaneousType: "",
        stockManagementTargetType: "",
        stockAllocationType: "",
        supplierCode: {
            value: "",
            branchNumber: 0,
        },
        substituteProduct: [],
        boms: [],
        customerSpecificSellingPrices: [],
        checked: false,
        addFlag: false,
        deleteFlag: false,
    };

    const [products, setProducts] = useState<ProductType[]>([]);
    const [newProduct, setNewProduct] = useState<ProductType>(initialProduct);
    const [searchProductCode, setSearchProductCode] = useState<string>("");
    const productService = ProductService();

    return {
        initialProduct,
        products,
        newProduct,
        setNewProduct,
        setProducts,
        searchProductCode,
        setSearchProductCode,
        productService
    };
};

export const useFetchProducts = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "商品情報の取得に失敗しました:");

export const useFetchBoms = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, {select: service.selectBoms}, "部品情報の取得に失敗しました:");

export const useFetchSubstitutes = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "代替商品情報の取得に失敗しました:");

