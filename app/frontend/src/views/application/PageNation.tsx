import React, {useState} from "react";

export type PageNationType = {
    endRow: number;
    hasNextPage: boolean;
    hasPreviousPage: boolean;
    isFirstPage: boolean;
    isLastPage: boolean;
    navigateFirstPage: number;
    navigateLastPage: number;
    navigatePages: number;
    navigatepageNums: number[];
    nextPage: number;
    pageNum: number;
    pageSize: number;
    pages: number;
    prePage: number;
    size: number;
    startRow: number;
    total: number;
};

export const usePageNation = <T = never>() => {
    const [pageNation, setPageNation] = useState<PageNationType | null>(null);
    const [criteria, setCriteria] = useState<T | null>(null);

    return {
        pageNation,
        setPageNation,
        criteria,
        setCriteria
    }
}

interface PageNationComponentProps<T = never> {
    pageNation: PageNationType | null;
    callBack: (page: number, criteria?: T) => void;
    criteria?: T;
}

export const PageNation = <T = never>({pageNation, callBack, criteria} : PageNationComponentProps<T>) => {
    const handlePageClick = (page: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
        event.preventDefault();
        callBack(page, criteria ?? undefined);
    };

    if (pageNation == null) return null;

    return (
        <div className="collection-object-container">
            <ol className="pagination">
                <li className="pagination__item" data-page={pageNation.prePage}>
                    <a className="pagination__link" href="#" onClick={handlePageClick(pageNation.prePage)}>前へ</a>
                </li>
                <li className="pagination__item" data-page={pageNation.navigateFirstPage}>
                    <span className="pagination__link" onClick={handlePageClick(pageNation.navigateFirstPage)}>
                        {pageNation.navigateFirstPage}
                    </span>
                </li>
                <li className="pagination__item" data-page={pageNation.pageNum}>
                    <span className="pagination__link pagination__link--active">{pageNation.pageNum}</span>
                </li>
                {pageNation.navigatePages > 1 && (
                    <>
                        <li className="pagination__item">
                            <span className="pagination__link pagination__link--extend">…</span>
                        </li>
                        <li className="pagination__item"
                            data-page={pageNation.navigatepageNums[pageNation.navigatepageNums.length - 1]}>
                            <a className="pagination__link" href="#"
                               onClick={handlePageClick(pageNation.navigatepageNums[pageNation.navigatepageNums.length - 1])}>
                                {pageNation.navigatepageNums[pageNation.navigatepageNums.length - 1]}
                            </a>
                        </li>
                    </>
                )}
                <li className="pagination__item" data-page={pageNation.nextPage}>
                    <a className="pagination__link" href="#" onClick={handlePageClick(pageNation.nextPage)}>次へ</a>
                </li>
            </ol>
        </div>
    );
};
