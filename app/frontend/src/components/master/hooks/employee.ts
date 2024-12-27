import {useState} from "react";
import {
    EmployeeType,
    LowerType,
    SlitYnType
} from "../../../models";
import {EmployeeService, EmployeeServiceType} from "../../../services/employee.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../application/hooks.ts";

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