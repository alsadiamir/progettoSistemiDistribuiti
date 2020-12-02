import { useContext, useState} from 'react';
import UserContext from '../../components/UserContext/component';
import styled from 'styled-components'
import LeftBar from './LeftBar/component';
import RoomListPage from '../RoomListPage/component';
import ReservationListPage from '../ReservationListPage/component';

const ContainerDiv = styled.div`
    width: 90%;
    height: 90%;
    margin: 1rem;
    display: flex;
    flex-direction: row;
`;

const LeftBarDiv = styled.div`
    display: flex;
`;

const MainContentDiv = styled.div`
    width: 80%;
`;

const menuOptions = [
    { code: "rooms", title: "Available Rooms" },
    { code: "reservations", title: "Your Reservations" },
];

function HomePage() {
    const authedUser = useContext(UserContext)
    const [openPage, setOpenPage] = useState(null);

    return (
        <ContainerDiv>
            <LeftBarDiv>
                <LeftBar
                    userName={authedUser.displayName}
                    onSelection={setOpenPage}
                    menuOptions={menuOptions}
                />
            </LeftBarDiv>
            <MainContentDiv>
                {openPage && openPage === "rooms" && (
                    <RoomListPage />
                )}
                {openPage && openPage === "reservations" && (
                    <ReservationListPage />
                )}
            </MainContentDiv>
        </ContainerDiv>
    );
}
  
export default HomePage;
  