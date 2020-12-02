import styled from 'styled-components'
import { useState } from 'react';
import RoomPage from '../RoomPage/component';

const ContainerDiv = styled.div`
    padding: 1rem;
`;

const EntryList = styled.div`
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
`;

const MenuEntryDiv = styled.button`
    margin: 1rem;
    padding: 2rem;
    justify-content: center;
    display: flex;
    flex-direction: column;
    background-color: #2980b9;
    border-radius: 1rem;
    color: whitesmoke;
    box-decoration: none;
    outline: none;
    border: none; 
    
    &:hover {
        background-color: #2c3e50;
    }
`;

const MenuEntryTitleSpan = styled.span`
    font-size: large;
    font-weight: bold;
`;

const PageTitle = styled.h1`
    text-align: center;
`;

function RoomListPage() {  
    // TODO: Grab those from backend and show loading state
    const [rooms, setRooms] = useState([
        { id: 0, name: "Mensa di Ingernaria", address: "Via Risorgimento 2, Bologna"},
        { id: 1, name: "Bar all'Angolo", address: "Via dei Pioppi 5, Vicenza"},
        { id: 2, name: "Hell's Kitchen", address: "The Strip, Las Vegas"},
        { id: 3, name: "Mensa di Ingernaria", address: "Via Risorgimento 2, Bologna"},
        { id: 4, name: "Bar all'Angolo", address: "Via dei Pioppi 5, Vicenza"},
        { id: 5, name: "Hell's Kitchen", address: "The Strip, Las Vegas"},
        { id: 6, name: "Mensa di Ingernaria", address: "Via Risorgimento 2, Bologna"},
        { id: 7, name: "Bar all'Angolo", address: "Via dei Pioppi 5, Vicenza"},
        { id: 8, name: "Hell's Kitchen", address: "The Strip, Las Vegas"},
    ]);
    const [selectedRoomID, setSelectedRoomID] = useState(null);

    return (
        <ContainerDiv>
            {!selectedRoomID && (
                <>
                    <PageTitle>Available Rooms</PageTitle>
                    <EntryList>
                        {rooms.map((m => (
                            <MenuEntryDiv
                                key={m.id}
                                onClick={() => setSelectedRoomID(m.id)}
                            >
                                <MenuEntryTitleSpan>{m.name}</MenuEntryTitleSpan>
                                <span>{m.address}</span>
                            </MenuEntryDiv>
                        )))}
                    </EntryList> 
                </>
            )}
            {selectedRoomID && (
                <RoomPage
                    room={rooms.find(r => r.id === selectedRoomID)}
                    onGoBack={() => setSelectedRoomID(null)}
                />
            )}
        </ContainerDiv>
    );
}
  
export default RoomListPage;
  