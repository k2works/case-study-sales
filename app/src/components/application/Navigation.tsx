import React from 'react';
import {Link} from 'react-router-dom';
import {FaBars, FaTimes} from "react-icons/fa";
import {getRoleFromUser} from "./RouteAuthGuard.tsx";
import {RoleType} from "../../types";

const NAV_CONTAINER_CLASS = "nav-container";
const SIDE_NAV_CLASS = "side-nav";
const NAV_ITEM_CLASS = "nav-item";
const NAV_SUB_ITEM_CLASS = "nav-sub-item";
const NAV_SUB_LIST_CLASS = "nav-sub-list";
const ACTIVE_CLASS = "active";

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
    <li className={NAV_SUB_ITEM_CLASS} id={id}>
        <Link to={to}>{children}</Link>
    </li>
);

const NaveItems: React.FC = () => {
    const role = getRoleFromUser();

    return (
        <ul>
            <NavItem id="side-nav-home-nav" to="/"
                     className={`${NAV_ITEM_CLASS} ${NAV_SUB_ITEM_CLASS} ${ACTIVE_CLASS}`}>ホーム</NavItem>
            {role === RoleType.ADMIN && (
                <NavItem id="side-nav-user-nav" to="/User" className={NAV_ITEM_CLASS}>ユーザー</NavItem>
            )}
            <NavItem id="side-nav-logout-nav" to="/Logout" className={NAV_ITEM_CLASS}>ログアウト</NavItem>
        </ul>
    )
};

export const SideNavigation: React.FC = () => (
    <div className={NAV_CONTAINER_CLASS}>
        <nav className={SIDE_NAV_CLASS} id="side-nav-menu">
            <NaveItems/>
        </nav>
    </div>
);

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
