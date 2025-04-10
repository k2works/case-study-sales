import {Route, Routes} from "react-router-dom";
import React from "react";
import {RouteAuthGuard} from "./RouteAuthGuard";
import {Home} from "./Home.tsx";
import {NotFound} from "../../views/application/NotFound.tsx";
import {RoleType} from "../../models";
import {Login, Logout} from "../system/Auth.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import {DownloadContainer} from "../system/download/DownloadContainer.tsx";
import {DepartmentContainer} from "../master/department/DepartmentContainer.tsx";
import {EmployeeContainer} from "../master/employee/EmployeeContainer.tsx";
import {ProductContainer} from "../master/product/ProductContainer.tsx";
import {ProductItemContainer} from "../master/product/item/ProductItemContainer.tsx";
import {ProductCategoryContainer} from "../master/product/category/ProductCategoryContainer.tsx";
import {CodeContainer} from "../master/code/CodeContainer.tsx";
import {RegionContainer} from "../master/code/region/RegionContainer.tsx";
import {PartnerContainer} from "../master/partner/PartnerContainer.tsx";
import {PartnerCategoryContainer} from "../master/partner/category/PartnerCategoryContainer.tsx";
import {PartnerGroupContainer} from "../master/partner/group/PartnerGroupContainer.tsx";
import {PartnerListContainer} from "../master/partner/list/PartnerListContainer.tsx";
import {CustomerContainer} from "../master/partner/customer/CustomerContainer.tsx";
import {VendorContainer} from "../master/partner/vendor/VendorContainer.tsx";
import {AuditContainer} from "../system/audit/AuditContainer.tsx";
import {UserContainer} from "../system/user/UserContainer.tsx";
import {SalesOrderTabContainer} from "../sales/sales_order/SalesOrderTabContainer.tsx";
import {SalesOrderContainer} from "../sales/sales_order/list/SalesOrderContainer.tsx";
import {SalesOrderUploadContainer} from "../sales/sales_order/upload/SalesOrderUploadContainer.tsx";
import {SalesOrderRuleContainer} from "../sales/sales_order/rule/SalesOrderRuleContainer.tsx";
import {SalesTabContainer} from "../sales/sales/SalesTabContainer.tsx";
import {SalesContainer} from "../sales/sales/list/SalesContainer.tsx";
import {SalesAggregateContainer} from "../sales/sales/aggregate/SalesAggregateContainer.tsx";
import {ShippingTabContainer} from "../sales/shipping/ShippingTabContainer.tsx";
import {ShippingContainer} from "../sales/shipping/list/ShippingContainer.tsx";
import {ShippingRuleContainer} from "../sales/shipping/rule/ShippingRuleContainer.tsx";
import {ShippingOrderContainer} from "../sales/shipping/order/ShippingOrderContainer.tsx";
import {ShippingConfirmContainer} from "../sales/shipping/confirm/ShippingConfirmContainer.tsx";

export const RouteConfig: React.FC = () => {
    const ProductCategoryPage = () => {
        return (
            <SiteLayout>
                <ProductCategoryContainer/>
            </SiteLayout>
        );
    }

    const ProductDetailPage = () => {
        return (
            <SiteLayout>
                <ProductItemContainer/>
            </SiteLayout>
        );
    }

    const RegionPage = () => {
        return (
            <SiteLayout>
                <RegionContainer/>
            </SiteLayout>
        );
    }

    const PartnerCategoryPage = () => {
        return (
            <SiteLayout>
                <PartnerCategoryContainer/>
            </SiteLayout>
        );
    }

    const PartnerGroupPage = () => {
        return (
            <SiteLayout>
                <PartnerGroupContainer/>
            </SiteLayout>
        );
    }

    const PartnerListPage = () => {
        return (
            <SiteLayout>
                <PartnerListContainer/>
            </SiteLayout>
        );
    }

    const CustomerPage = () => {
        return (
            <SiteLayout>
                <CustomerContainer/>
            </SiteLayout>
        );
    }

    const VendorPage = () => {
        return (
            <SiteLayout>
                <VendorContainer/>
            </SiteLayout>
        );
    }

    const SalesOrderPage = () => {
        return (
            <SiteLayout>
                <SalesOrderContainer/>
            </SiteLayout>
        )
    }

    const SalesOrderUploadPage = () => {
        return (
            <SiteLayout>
                <SalesOrderUploadContainer/>
            </SiteLayout>
        )
    }

    const SalesOrderRulePage = () => {
        return (
            <SiteLayout>
                <SalesOrderRuleContainer/>
            </SiteLayout>
        )
    }

    const SalesPage = () => {
        return (
            <SiteLayout>
                <SalesContainer/>
            </SiteLayout>
        )
    }

    const SalesSummaryPage = () => {
        return (
            <SiteLayout>
                <SalesAggregateContainer/>
            </SiteLayout>
        )
    }

    const ShippingPage = () => {
        return (
            <SiteLayout>
                <ShippingContainer/>
            </SiteLayout>
        )
    }

    const ShippingRulePage = () => {
        return (
            <SiteLayout>
                <ShippingRuleContainer/>
            </SiteLayout>
        )
    }

    const ShippingOrderPage = () => {
        return (
            <SiteLayout>
                <ShippingOrderContainer/>
            </SiteLayout>
        )
    }

    const ShippingConfirmPage = () => {
        return (
            <SiteLayout>
                <ShippingConfirmContainer/>
            </SiteLayout>
        )
    }


    return (
            <Routes>
                <Route path="/" element={<RouteAuthGuard component={<Home/>} redirectPath="/login"/>}/>
                <Route path="/user" element={<RouteAuthGuard component={<UserContainer/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/audit" element={<RouteAuthGuard component={<AuditContainer/>} redirectPath="/"
                                                                            allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/download" element={<RouteAuthGuard component={<DownloadContainer/>} redirectPath="/"
                                                                 allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales-order" element={<RouteAuthGuard component={<SalesOrderTabContainer/>} redirectPath="/"
                                                                    allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales-order-list" element={<RouteAuthGuard component={<SalesOrderPage/>} redirectPath="/"
                                                                         allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales-order-upload" element={<RouteAuthGuard component={<SalesOrderUploadPage/>} redirectPath="/"
                                                                         allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales-order-rule" element={<RouteAuthGuard component={<SalesOrderRulePage/>} redirectPath="/"
                                                                           allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales" element={<RouteAuthGuard component={<SalesTabContainer/>} redirectPath="/"
                                                                    allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales-list" element={<RouteAuthGuard component={<SalesPage/>} redirectPath="/"
                                                                         allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/sales-summary" element={<RouteAuthGuard component={<SalesSummaryPage/>} redirectPath="/"
                                                                           allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/shipping" element={<RouteAuthGuard component={<ShippingTabContainer/>} redirectPath="/"
                                                                    allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/shipping-list" element={<RouteAuthGuard component={<ShippingPage/>} redirectPath="/"
                                                                         allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/shipping-rule" element={<RouteAuthGuard component={<ShippingRulePage/>} redirectPath="/"
                                                                           allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/shipping-order" element={<RouteAuthGuard component={<ShippingOrderPage/>} redirectPath="/"
                                                                      allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/shipping-confirm" element={<RouteAuthGuard component={<ShippingConfirmPage/>} redirectPath="/"
                                                                       allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/department" element={<RouteAuthGuard component={<DepartmentContainer/>} redirectPath="/"
                                                                   allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/employee" element={<RouteAuthGuard component={<EmployeeContainer/>} redirectPath="/"
                                                                 allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/product" element={<RouteAuthGuard component={<ProductContainer/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/product-category" element={<RouteAuthGuard component={<ProductCategoryPage/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/product-item" element={<RouteAuthGuard component={<ProductDetailPage/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/partner" element={<RouteAuthGuard component={<PartnerContainer/>} redirectPath="/"
                                                                     allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/partner-category" element={<RouteAuthGuard component={<PartnerCategoryPage/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/partner-group" element={<RouteAuthGuard component={<PartnerGroupPage/>} redirectPath="/"
                                                                         allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/partner-list" element={<RouteAuthGuard component={<PartnerListPage/>} redirectPath="/"
                                                                      allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/customer" element={<RouteAuthGuard component={<CustomerPage/>} redirectPath="/"
                                                                     allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/vendor" element={<RouteAuthGuard component={<VendorPage/>} redirectPath="/"
                                                                     allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/code" element={<RouteAuthGuard component={<CodeContainer/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/region" element={<RouteAuthGuard component={<RegionPage/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
    );
}
