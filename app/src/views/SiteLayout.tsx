import React, {ReactNode} from 'react';
import './index.css'
import {Header} from "./application/Header";
import {Footer} from "./application/Footer";
import {HeaderNavigation, SideNavigation} from "./application/Navigation";

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
