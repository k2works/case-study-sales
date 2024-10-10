import React from 'react';
import {Link} from "react-router-dom";

export default function Navigation() {
    return (
        <div className="nav-container">
            <nav className="side-nav" id="side-nav-menu">
                <ul>
                    <li className="nav-item nav-sub-item active" id="side-nav-home-nav">
                        <Link to="/">HOME</Link>
                    </li>
                    <li className="nav-item" id="side-nav-logout-nav">
                        <Link to="/login">ログアウト</Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
}

