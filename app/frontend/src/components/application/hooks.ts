import {useState} from "react";
import Modal from "react-modal";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import {PageNationType} from "../../views/application/PageNation.tsx";

export const useModal = () => {
    Modal.setAppElement('#root');
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);

    return {
        modalIsOpen,
        setModalIsOpen,
        isEditing,
        setIsEditing,
        editId,
        setEditId,
        Modal,
    }
}

export const useTab = () => {
    const [tabIndex, setTabIndex] = useState<number>(0);

    const handleTabSelect = (index: number) => {
        setTabIndex(index);
    };

    return {
        tabIndex,
        handleTabSelect,
        Tab,
        TabList,
        TabPanel,
        Tabs,
    }
}
export const useFetchEntities = <
    EntityType,
    ServiceType extends { select: (page: number) => Promise<any> }
>(
    setLoading: (loading: boolean) => void,
    setList: (list: EntityType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ServiceType,
    errorMessage: string
) => {
    const load = async (page: number = 1): Promise<void> => {
        setLoading(true);
        try {
            const fetchedEntities = await service.select(page);
            const {list, ...pagination} = fetchedEntities;
            setList(list);
            setPageNation(pagination);
            setError("");
        } catch (error: any) {
            showErrorMessage(`${errorMessage} ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };
    return {
        load
    };
};
