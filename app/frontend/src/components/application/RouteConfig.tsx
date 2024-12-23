import {Route, Routes} from "react-router-dom";
import React from "react";
import {RouteAuthGuard} from "./RouteAuthGuard";
import {Login} from "../system/Login.tsx";
import {Logout} from "../system/Logout.tsx";
import {User} from "../system/User.tsx";
import {RoleType} from "../../models";
import {Department} from "../master/Department.tsx";
import {Employee} from "../master/Employee.tsx";
import {NotFound} from "../../views/application/NotFound.tsx";
import {Home} from "./Home.tsx";
import {Product} from "../master/Product.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import {ProductCategory} from "../master/ProductCategory.tsx";
import {ProductItem} from "../master/ProductItem.tsx";

export const RouteConfig: React.FC = () => {
    const ProductCategoryPage = () => {
        return (
            <SiteLayout>
                <ProductCategory/>
            </SiteLayout>
        );
    }

    const ProductDetailPage = () => {
        return (
            <SiteLayout>
                <ProductItem/>
            </SiteLayout>
        );
    }

    return (
        <>
            <Routes>
                <Route path="/" element={<RouteAuthGuard component={<Home/>} redirectPath="/login"/>}/>
                <Route path="/user" element={<RouteAuthGuard component={<User/>} redirectPath="/"
                                                             allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/department" element={<RouteAuthGuard component={<Department/>} redirectPath="/"
                                                                   allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/employee" element={<RouteAuthGuard component={<Employee/>} redirectPath="/"
                                                                 allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/product" element={<RouteAuthGuard component={<Product/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/product-category" element={<RouteAuthGuard component={<ProductCategoryPage/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/product-item" element={<RouteAuthGuard component={<ProductDetailPage/>} redirectPath="/"
                                                                allowedRoles={[RoleType.ADMIN]}/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
        </>
    );
}
