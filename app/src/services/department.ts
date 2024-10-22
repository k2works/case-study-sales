import Config from "./config";
import Utils from "./utils";
import {DepartmentResourceType, DepartmentType} from "../types";

export const DepartmentService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/departments`;

    const mapToDepartmentResource = (department: DepartmentType): DepartmentResourceType => {
        return {
            departmentCode: department.departmentId.deptCode.value,
            startDate: department.departmentId.departmentStartDate.value,
            endDate: department.endDate.value,
            departmentName: department.departmentName,
            layer: department.layer.toString(),
            path: department.path.value,
            lowerType: department.lowerType.toString(),
            slitYn: department.slitYn.toString()
        };
    };

    const select = async (page?: number, pageSize?: number): Promise<any> => {
        let url = endPoint;
        if (pageSize && page) {
            url = url + "?pageSize=" + pageSize + "&page=" + page;
        } else if (pageSize) {
            url = url + "?pageSize=" + pageSize;
        } else if (page) {
            url = url + "?page=" + page;
        }
        return await apiUtils.fetchGet(url);
    };

    const find = async (deptCode: string, departmentStartDate: string): Promise<DepartmentType> => {
        const url = `${endPoint}/${deptCode}/${departmentStartDate}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (department: DepartmentType) => {
        return await apiUtils.fetchPost(endPoint, mapToDepartmentResource(department));
    };

    const update = async (department: DepartmentType) => {
        const url = `${endPoint}/${department.departmentId.deptCode.value}/${department.departmentId.departmentStartDate.value}`;
        return await apiUtils.fetchPut(url, mapToDepartmentResource(department));
    };

    const search = async (pageSize = 10, code: string, page = 1): Promise<DepartmentType[]> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&code=${code}&page=${page}`;
        return await apiUtils.fetchGet(url);
    };

    const destroy = async (deptCode: string, departmentStartDate: string): Promise<void> => {
        const url = `${endPoint}/${deptCode}/${departmentStartDate}`;
        await apiUtils.fetchDelete(url);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search
    };
}
