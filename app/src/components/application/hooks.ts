import {useState} from "react";
import Modal from "react-modal";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

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
