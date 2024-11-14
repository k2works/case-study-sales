import React, {ReactNode} from "react";

interface HeaderProps {
    menu: ReactNode;
}

export const Header: React.FC<HeaderProps> = ({menu = null}) => {
    return (
        <header className="header" id="header">
            <div className="header-container w-container">
                <div className="site">
                    <a className="logo" href="">SMS</a>
                </div>

                <div id="nav">{menu}</div>
            </div>
        </header>
    );
}
