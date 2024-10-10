import React, {ReactNode} from 'react';
import Header from "./Header";
import Footer from "./Footer";

interface SiteLayoutProps {
    children: ReactNode;
    menu?: ReactNode;
}

export default function SiteLayout({children, menu = null}: SiteLayoutProps) {
    return (
        <div className="root">
            <div className="root-container w-container">
                <Header/>
                <div className="section-container">
                    <section className="sidebar" id="side-nav">
                        {menu}
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
