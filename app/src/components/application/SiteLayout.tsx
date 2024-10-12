import React, {ReactNode} from 'react';
import {Header} from "./Header";
import {Footer} from "./Footer";
import {HeaderNavigation, SideNavigation} from "./Navigation";

interface SiteLayoutProps {
    children: ReactNode;
}

export const SiteLayout: React.FC<SiteLayoutProps> = ({children}: SiteLayoutProps) => {
    return (
        <div className="root">
            <div className="root-container w-container">
                <Header menu={<HeaderNavigation/>}/>
                <div className="section-container">
                    <section className="sidebar" id="side-nav">
                        {<SideNavigation/>}
                    </section>

                    <section className="contents" id="contents">
                        {children}
                    </section>
                </div>
                <Footer/>
            </div>
        </div>
    );
}
