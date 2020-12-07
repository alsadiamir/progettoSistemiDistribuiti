import styled from 'styled-components'
import { useEffect, useState } from 'react';
import RoomPage from '../RoomPage/component';
import { useGetRequest } from '../../hooks/useGetRequest';
import ErrorBox from '../../components/ErrorBox/component';

const ContainerDiv = styled.div`
    padding: 1rem;
`;

const EntryList = styled.div`
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
`;

const MenuEntryDiv = styled.button(() => [
    `margin: 1rem;
    padding: 2rem;
    justify-content: center;
    display: flex;
    flex-direction: column;
    
    border-radius: 1rem;
    color: whitesmoke;
    box-decoration: none;
    outline: none;
    border: none;
    background-color: #2980b9;
    &:hover { background-color: #2c3e50};`
]);

const MenuEntryClosedDiv = styled.button(() => [
    `margin: 1rem;
    padding: 2rem;
    justify-content: center;
    display: flex;
    flex-direction: column;
    
    border-radius: 1rem;
    color: whitesmoke;
    box-decoration: none;
    outline: none;
    border: none;
    background-color: #95a5a6;
    &:hover { background-color: #7f8c8d}; };`
]);

const MenuEntryTitleSpan = styled.span`
    display: block;
    font-size: large;
    font-weight: bold;
`;

const MenuEntrySubtitleSpan = styled.span`
    display: block; 
    font-size: medium;
`;

const PageTitle = styled.h1`
    text-align: center;
`;

function RoomListPage() {  
    const {data, loading, error} = useGetRequest("/rooms")
    const [selectedRoomID, setSelectedRoomID] = useState(null);

    const localTimeToDate = (lt) => {
        const date = new Date(Date.now())
        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), lt.hour, lt.minute, lt.second);
    }

    const isRoomOpen = (room) => {
        const now = new Date(Date.now())
        const openingTime = localTimeToDate(room.openingTime)
        const closingTime = localTimeToDate(room.closingTime)
        return now >= openingTime && now <= closingTime
    }

    return (
        <ContainerDiv>
            {!loading && !error && data && (
                <>
                    {!selectedRoomID && (
                        <>
                            <PageTitle>Available Rooms</PageTitle>
                            <EntryList>
                                {data.map((m => (
                                    <>
                                        {isRoomOpen(m) && (
                                            <MenuEntryDiv
                                                key={m.id}
                                                onClick={() => setSelectedRoomID(m.id)}
                                            >
                                                <MenuEntryTitleSpan>{m.name}</MenuEntryTitleSpan>
                                                <MenuEntrySubtitleSpan>{m.address}</MenuEntrySubtitleSpan>
                                                <MenuEntrySubtitleSpan>
                                                    {localTimeToDate(m.openingTime).toLocaleTimeString()} - {localTimeToDate(m.closingTime).toLocaleTimeString()}
                                                </MenuEntrySubtitleSpan>
                                            </MenuEntryDiv>
                                        )}
                                        {!isRoomOpen(m) && (
                                            <MenuEntryClosedDiv
                                                key={m.id}
                                            >
                                                <MenuEntryTitleSpan>{m.name}</MenuEntryTitleSpan>
                                                <MenuEntrySubtitleSpan>{m.address}</MenuEntrySubtitleSpan>
                                                <MenuEntrySubtitleSpan>
                                                    {localTimeToDate(m.openingTime).toLocaleTimeString()} - {localTimeToDate(m.closingTime).toLocaleTimeString()}
                                                </MenuEntrySubtitleSpan>
                                                {!isRoomOpen(m) && (
                                                    <MenuEntrySubtitleSpan>CLOSED</MenuEntrySubtitleSpan>
                                                )}
                                            </MenuEntryClosedDiv>
                                        )}
                                    </>
                                )))}
                            </EntryList> 
                        </>
                    )}
                    {selectedRoomID && (
                        <RoomPage
                            room={data.find(r => r.id === selectedRoomID)}
                            onGoBack={() => setSelectedRoomID(null)}
                        />
                    )}
                </>
            )}
            {loading && !error && (
                <p>Loading...</p>
            )} 
            {!loading && error && (
                <ErrorBox>error</ErrorBox>
            )} 
        </ContainerDiv>
    );
}
  
export default RoomListPage;
  