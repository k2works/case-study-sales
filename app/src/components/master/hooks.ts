import {useState} from "react";
import {DepartmentIdType, DepartmentType, EmployeeType, LowerType, ProductCategoryType, SlitYnType} from "../../models";
import {DepartmentService, DepartmentServiceType} from "../../services/department.ts";
import {EmployeeService, EmployeeServiceType} from "../../services/employee.ts";
import {PageNationType} from "../../views/application/PageNation.tsx";
import {ProductCategoryService, ProductCategoryServiceType} from "../../services/product_category.ts";

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
    showErrorMessage: (message: string, callback: (error: string) => void) => void, service: DepartmentServiceType) => {
    const load = async (page: number = 1): Promise<void> => {
        const ERROR_MESSAGE = "部門情報の取得に失敗しました:";
        setLoading(true);

        try {
            const fetchedUsers = await service.select(page);
            const {list, ...pagination} = fetchedUsers;

            setList(list);
            setPageNation(pagination);
            setError("");
        } catch (error: any) {
            showErrorMessage(`${ERROR_MESSAGE} ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return {
        load
    };
};

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
    showErrorMessage: (message: string, callback: (error: string) => void) => void, service: EmployeeServiceType) => {
    const load = async (page: number = 1): Promise<void> => {
        const ERROR_MESSAGE = "社員情報の取得に失敗しました:";
        setLoading(true);

        try {
            const fetchedUsers = await service.select(page);
            const {list, ...pagination} = fetchedUsers;

            setList(list);
            setPageNation(pagination);
            setError("");
        } catch (error: any) {
            showErrorMessage(`${ERROR_MESSAGE} ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return {
        load
    };
};

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
    showErrorMessage: (message: string, callback: (error: string) => void) => void, service: ProductCategoryServiceType) => {
    const load = async (page: number = 1): Promise<void> => {
        const ERROR_MESSAGE = "製品カテゴリ情報の取得に失敗しました:";
        setLoading(true);
        try {
            const fetchedCategories = await service.select(page);
            const {list, ...pagination} = fetchedCategories;
            setList(list);
            setPageNation(pagination);
            setError("");
        } catch (error: any) {
            showErrorMessage(`${ERROR_MESSAGE} ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };
    return {
        load
    };
};
