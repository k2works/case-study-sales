import React, {ReactNode} from "react";
import Env from "../../env.ts";

interface HeaderProps {
    menu: ReactNode;
}

export const Header: React.FC<HeaderProps> = ({menu = null}) => {
    const logo = (() => {
        if (Env.isProduction()) {
            return <a className="logo" href="">SMS</a>
        } else {
            return <a className="logo-dev" href="">SMS {Env.currentEnv()}</a>
        }
    })();

    return (
        <header className="header" id="header">
            <div className="header-container w-container">
                <div className="site">
                    {logo}
                </div>

                <div id="nav">{menu}</div>
            </div>
        </header>
    );
}
