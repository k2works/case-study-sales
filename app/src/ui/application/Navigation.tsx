import React from 'react';
import {Link} from 'react-router-dom';
import {FaBars, FaTimes} from "react-icons/fa";
import {RoleType} from "../../types";
import {getRoleFromUser} from "../../components/application/RouteAuthGuard.tsx";

interface NavItemProps {
    id: string;
    to: string;
    className: string;
    children: React.ReactNode;
}
const NavItem: React.FC<NavItemProps> = ({id, to, className, children}) => (
    <li className={className} id={id}>
        <Link to={to}>{children}</Link>
    </li>
);

interface SubNavItemProps {
    id: string;
    to: string;
    children: React.ReactNode;
}
const SubNavItem: React.FC<SubNavItemProps> = ({id, to, children}) => (
    <li className="nav-sub-item" id={id}>
        <Link to={to}>{children}</Link>
    </li>
);

const NaveItems: React.FC = () => {
    const role = getRoleFromUser();
    return (
        <ul>
            <NavItem id="side-nav-home-nav" to="/"
                     className="nav-item active">ホーム</NavItem>
            {role === RoleType.ADMIN && (
                <>
                    <li className="nav-item">
                        システム
                        <ul className="nav-sub-list">
                            <SubNavItem id="side-nav-user-nav" to="/user">ユーザー</SubNavItem>
                        </ul>
                    </li>
                    <li className="nav-item">
                        マスタ
                        <ul className="nav-sub-list">
                            <SubNavItem id="side-nav-department-nav" to="/department">部門</SubNavItem>
                            <SubNavItem id="side-nav-department-nav" to="/employee">社員</SubNavItem>
                        </ul>
                    </li>
                </>
            )
            }
            <NavItem id="side-nav-logout-nav" to="/logout" className="nav-item">ログアウト</NavItem>
        </ul>
    )
};

export const SideNavigation: React.FC = () => {
    return (
        <div className="nav-container">
            <nav className="side-nav" id="side-nav-menu">
                <NaveItems/>
            </nav>
        </div>
    )
};

export const HeaderNavigation: React.FC = () => {
    const toggleMenu = () => {
        const nav = document.querySelector("html");
        if (!nav) return;
        nav.classList.toggle("open");
    };

    const clearMenu = () => {
        const nav = document.querySelector("html");
        if (!nav) return;
        nav.classList.remove("open");
    };

    return (
        <div className="nav-container">
            <button className="navbtn" id="nav-button" onClick={toggleMenu}>
                <i className="fas fa-bars"><FaBars/></i>
                <i className="fas fa-times"><FaTimes/></i>
            </button>

            <nav className="nav" id="nav-menu" onClick={clearMenu}>
                <NaveItems/>
            </nav>
        </div>
    );
};