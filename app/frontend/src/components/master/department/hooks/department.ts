import {useState} from "react";
import {
    DepartmentCriteriaType,
    DepartmentType,
    LowerType,
    SlitYnType
} from "../../../../models";
import {DepartmentService, DepartmentServiceType} from "../../../../services/master/department.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";

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
    const [searchDepartmentCriteria, setSearchDepartmentCriteria] = useState<DepartmentCriteriaType>({});
    const departmentService = DepartmentService();

    return {
        initialDepartment,
        departments,
        newDepartment,
        setNewDepartment,
        searchDepartmentCriteria,
        setSearchDepartmentCriteria,
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
) => useFetchEntities<DepartmentType, DepartmentServiceType, DepartmentCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "部門情報の取得に失敗しました:");
