import styled from 'styled-components'
import { useCallback, useContext, useEffect, useState } from 'react';
import ErrorBox from '../../components/ErrorBox/component'
import Reservation from './Reservation/component';
import useGetRequest from '../../hooks/useGetRequest'
import UserContext from '../../components/UserContext/component'

const ContainerDiv = styled.div`
    padding: 1rem;
`;

const EntryList = styled.div`
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    justify-content: center;
`;

const MenuEntryDiv = styled.div`
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
    const authedUser = useContext(UserContext)
    const {data, loading, error} = useGetRequest(`/reservation?userId=${authedUser.id}`)
    const [reservations, setReservations] = useState(null)

    useEffect(() => {
        if (data && !loading && !error) {
            setReservations(data)
        }
    }, [data, loading, error, setReservations])

    const onCancelReservation = useCallback((reservation) => {
        setReservations(v => {
            return v.filter((o) => o.id !== reservation.id)
        })
    }, [setReservations])

    return (
        <ContainerDiv>
            <PageTitle>Your Reservations</PageTitle>
            {reservations && data && !loading && !error && (
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
  
export default ReservationListPage;
  