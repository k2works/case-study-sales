import {Route, Routes} from "react-router-dom";
import React from "react";
import {RouteAuthGuard} from "./RouteAuthGuard";
import {Home} from "./Home.tsx";
import {User} from "../system/User.tsx";
import {NotFound} from "../../views/application/NotFound.tsx";
import {RoleType} from "../../models";
import {Login, Logout} from "../system/Auth.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import {Audit} from "../system/Audit.tsx";
import {Download} from "../system/Download.tsx";
import {Partner} from "../master/partner/Partner.tsx";
import {Code} from "../master/code/Code.tsx";
import {Region} from "../master/code/Region.tsx";
import {PartnerCategory} from "../master/partner/PartnerCategory.tsx";
import {PartnerGroup} from "../master/partner/PartnerGroup.tsx";
import {PartnerList} from "../master/partner/PartnerList.tsx";
import {Customer} from "../master/partner/Customer.tsx";
import {Vendor} from "../master/partner/Vendor.tsx";
import {DepartmentContainer} from "../master/department/DepartmentContainer.tsx";
import {EmployeeContainer} from "../master/employee/EmployeeContainer.tsx";
import {ProductContainer} from "../master/product/ProductContainer.tsx";
import {ProductItemContainer} from "../master/product/item/ProductItemContainer.tsx";
import {ProductCategoryContainer} from "../master/product/category/ProductCategoryContainer.tsx";

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
                <Region/>
            </SiteLayout>
        );
    }

    const PartnerCategoryPage = () => {
        return (
            <SiteLayout>
                <PartnerCategory/>
            </SiteLayout>
        );
    }

    const PartnerGroupPage = () => {
        return (
            <SiteLayout>
                <PartnerGroup/>
            </SiteLayout>
        );
    }

    const PartnerListPage = () => {
        return (
            <SiteLayout>
                <PartnerList/>
            </SiteLayout>
        );
    }

    const CustomerPage = () => {
        return (
            <SiteLayout>
                <Customer/>
            </SiteLayout>
        );
    }

    const VendorPage = () => {
        return (
            <SiteLayout>
                <Vendor/>
            </SiteLayout>
        );
    }

    return (
            <Routes>
                <Route path="/" element={<RouteAuthGuard component={<Home/>} redirectPath="/login"/>}/>
                <Route path="/user" element={<RouteAuthGuard component={<User/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/audit" element={<RouteAuthGuard component={<Audit/>} redirectPath="/"
                                                                            allowedRoles={[RoleType.ADMIN, RoleType.USER]}/>}/>
                <Route path="/download" element={<RouteAuthGuard component={<Download/>} redirectPath="/"
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
                <Route path="/partner" element={<RouteAuthGuard component={<Partner/>} redirectPath="/"
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
                <Route path="/code" element={<RouteAuthGuard component={<Code/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/region" element={<RouteAuthGuard component={<RegionPage/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
    );
}
