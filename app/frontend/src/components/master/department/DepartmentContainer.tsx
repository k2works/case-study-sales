import React, {useEffect} from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {DepartmentProvider, useDepartmentContext} from "../../../providers/master/Department.tsx";
import {EmployeeProvider, useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {DepartmentCollection} from "./DepartmentCollection.tsx";

export const DepartmentContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchDepartments,
        } = useDepartmentContext();

        const {
            fetchEmployees,
        } = useEmployeeContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchDepartments.load(),
                        fetchEmployees.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`部門情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <DepartmentCollection/>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <DepartmentProvider>
                <EmployeeProvider>
                    <Content/>
                </EmployeeProvider>
            </DepartmentProvider>
        </SiteLayout>
    );
};