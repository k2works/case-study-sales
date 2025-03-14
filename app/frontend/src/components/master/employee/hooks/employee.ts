import {useState} from "react";
import {
    EmployeeCriteriaType,
    EmployeeType,
    LowerType,
    SlitYnType
} from "../../../../models";
import {EmployeeService, EmployeeServiceType} from "../../../../services/master/employee.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";

export const useEmployee = () => {
    const initialEmployee: EmployeeType = {
        empCode: "",
        empFirstName: "",
        empLastName: "",
        empFirstNameKana: "",
        empLastNameKana: "",
        loginPassword: "",
        tel: "",
        fax: "",
        occuCode: "",
        approvalCode: "",
        departmentCode: "",
        departmentStartDate: "",
        departmentName: "",
        userId: "",
        addFlag: false,
        deleteFlag: false,
        checked: false
    };

    const [employees, setEmployees] = useState<EmployeeType[]>([]);
    const [newEmployee, setNewEmployee] = useState<EmployeeType>(initialEmployee);
    const [searchEmployeeCriteria, setSearchEmployeeCriteria] = useState<EmployeeCriteriaType>({});

    const employeeService = EmployeeService();

    return {
        initialEmployee,
        employees,
        newEmployee,
        setNewEmployee,
        setEmployees,
        searchEmployeeCriteria,
        setSearchEmployeeCriteria,
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
) => useFetchEntities<EmployeeType, EmployeeServiceType, EmployeeCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "社員情報の取得に失敗しました:");
