import styled from 'styled-components'
import { useState } from 'react';
import Reservation from './Reservation/component';

const ContainerDiv = styled.div`
    padding: 1rem;
`;

const EntryList = styled.div`
    display: flex;
    flex-direction: column;
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

const PageTitle = styled.h1`
    text-align: center;
`;

function ReservationListPage() {  
    // TODO: Grab those from backend and show loading state
    const [reservations, setReservations] = useState([
        { id: 1, seatId: 1, date: Date.now(), hourIndex: 4, duration: 2},
        { id: 1, seatId: 2, date: Date.now(), hourIndex: 5, duration: 1},
        { id: 1, seatId: 3, date: Date.now(), hourIndex: 6, duration: 3},
    ]);

    const onCancelReservation = (reservation) => {
        console.log(reservation.id)
    };

    return (
        <ContainerDiv>
            <PageTitle>Your Reservations</PageTitle>
            <EntryList>
                {reservations.map((m => (
                    <MenuEntryDiv key={m.id} >
                        <Reservation
                            reservation={m}
                            onCancel={onCancelReservation}
                        />
                    </MenuEntryDiv>
                )))}
            </EntryList> 
        </ContainerDiv>
    );
}
  
export default ReservationListPage;
  