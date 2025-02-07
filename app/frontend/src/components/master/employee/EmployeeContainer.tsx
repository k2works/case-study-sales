import React, {useEffect} from 'react';
import {showErrorMessage} from "../../application/utils.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {EmployeeProvider, useEmployeeContext} from "../../../providers/Employee.tsx";
import {DepartmentProvider, useDepartmentContext} from "../../../providers/Department.tsx";
import {UserProvider, useUserContext} from "../../../providers/User.tsx";
import {EmployeeCollection} from "./EmployeeCollection.tsx";

export const EmployeeContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchEmployees,
        } = useEmployeeContext();

        const {
            fetchDepartments,
        } = useDepartmentContext();

        const {
            fetchUsers,
        } = useUserContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchEmployees.load(),
                        fetchDepartments.load(),
                        fetchUsers.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`社員情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <EmployeeCollection/>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <EmployeeProvider>
                <DepartmentProvider>
                    <UserProvider>
                        <Content/>
                    </UserProvider>
                </DepartmentProvider>
            </EmployeeProvider>
        </SiteLayout>
    );
};
