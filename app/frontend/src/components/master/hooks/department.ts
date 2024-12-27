import {useState} from "react";
import {
    DepartmentIdType,
    DepartmentType,
    LowerType,
    SlitYnType
} from "../../../models";
import {DepartmentService, DepartmentServiceType} from "../../../services/department.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../application/hooks.ts";

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
