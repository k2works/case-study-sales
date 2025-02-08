import React from "react";
import Modal from "react-modal";
import {UserCollectionSelectView} from "../../../views/system/user/UserSelect.tsx";
import {useUserContext} from "../../../providers/system/User.tsx";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";

export const UserSelectModal: React.FC = () => {
    const {
        newEmployee,
        setNewEmployee,
    } = useEmployeeContext();

    const {
        pageNation: userPageNation,
        modalIsOpen: userModalIsOpen,
        setModalIsOpen: setUserModalIsOpen,
        users,
        fetchUsers,
    } = useUserContext();

    return (
        <Modal
            isOpen={userModalIsOpen}
            onRequestClose={() => setUserModalIsOpen(false)}
            contentLabel="ユーザー情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <UserCollectionSelectView
                    users={users}
                    handleSelect={(user) => {
                        setNewEmployee({
                            ...newEmployee,
                            user: user
                        });
                        setUserModalIsOpen(false);
                    }}
                    handleClose={() => setUserModalIsOpen(false)}
                    pageNation={userPageNation}
                    fetchUsers={fetchUsers.load}
                />
            }
        </Modal>
    )
}
