import React, {ReactNode} from 'react';
import './index.css'
import {Header} from "./application/Header.tsx";
import {Footer} from "./application/Footer.tsx";
import {HeaderNavigation, SideNavigation} from "./application/Navigation.tsx";

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
