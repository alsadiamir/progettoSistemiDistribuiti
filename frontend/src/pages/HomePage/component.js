import { useContext, useState} from 'react';
import UserContext from '../../components/UserContext/component';
import styled from 'styled-components'
import LeftBar from './LeftBar/component';
import RoomListPage from '../RoomListPage/component';

const ContainerDiv = styled.div`
    width: 100%;
    height: 100%;
    margin: 2rem;
    display: flex;
    flex-direction: row;
`;

const LeftBarDiv = styled.div`
    display: flex;
`;

const MainContentDiv = styled.div`
    width: 100%;
`;

const PageTitleDiv = styled.div`
    text-align: center;
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
                {openPage && (
                    <PageTitleDiv>
                        <h1>
                            {menuOptions.find((o) => o.code === openPage).title}
                        </h1>
                    </PageTitleDiv>
                )}
                {openPage && openPage === "rooms" && (
                    <RoomListPage />
                )}
            </MainContentDiv>
        </ContainerDiv>
    );
}
  
export default HomePage;
  