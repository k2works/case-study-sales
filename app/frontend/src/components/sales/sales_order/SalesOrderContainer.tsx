import React, { useEffect } from "react";
import { showErrorMessage } from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { SalesOrderProvider, useSalesOrderContext } from "../../../providers/sales/SalesOrder.tsx";
import {SalesOrderCollection} from "./SalesOrderCollection.tsx";
import {DepartmentProvider, useDepartmentContext} from "../../../providers/master/Department.tsx";
import {EmployeeProvider, useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {CustomerProvider, useCustomerContext} from "../../../providers/master/partner/Customer.tsx";

export const SalesOrderContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchSalesOrders,
        } = useSalesOrderContext();

        const {
            fetchDepartments,
        } = useDepartmentContext();

        const {
            fetchEmployees,
        } = useEmployeeContext();

        const {
            fetchCustomers,
        } = useCustomerContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        await fetchSalesOrders.load(),
                        fetchDepartments.load(),
                        fetchEmployees.load(),
                        fetchCustomers.load(),
                    ]);
                } catch (error: any) {
                    showErrorMessage(`受注情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <SalesOrderCollection/>
                )}
            </>
        );
    };

    return (
        <SalesOrderProvider>
            <DepartmentProvider>
                <EmployeeProvider>
                    <CustomerProvider>
                        <Content />
                    </CustomerProvider>
                </EmployeeProvider>
            </DepartmentProvider>
        </SalesOrderProvider>
    );
};