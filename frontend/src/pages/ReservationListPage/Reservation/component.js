import { useCallback, useEffect } from 'react';
import styled from 'styled-components'
import { timeBlockSizeInMinutes } from '../../../constants'
import usePostRequest from '../../../hooks/usePostRequest'

const BaseDiv = styled.div`
    width: 100%;
`;

const ContainerDiv = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
`;

const TextInfoDiv = styled.div`
    display: flex;
    flex-direction: column;
`;

const TitleSpan = styled.span`
    font-size: large;
    font-weight: bold;
`;

const TextSpan = styled.span`
    
`;

const CancelButton = styled.button`
    background-color: transparent;
    box-decoration: none;
    border-radius: 1rem;
    outline: none;
    border: none;

    &:hover {
        background-color: #BBBBBB;
    }
`;

function Reservation({reservation, onCancel}) { 
    const {doPost, data, loading, error} = usePostRequest(`/reservation/delete/${reservation.id}`)

    const parseDate = (v) => {
        return new Date(v.year, v.month - 1, v.day);
    }

    const formatBlock = (v) => {
        const mins = timeBlockSizeInMinutes * v
        const date = new Date(Date.now())
        date.setHours(Math.floor(mins / 60), mins % 60, 0)
        return date.toLocaleTimeString()
    }

    useEffect(() => {
        if (loading)
            return;
        if (error) {
            alert("Something went wrong when deleting your reservation! Try again later. :(")
        } else if(data) {
            onCancel(reservation)
        }
    }, [data, error, loading, onCancel, reservation])

    return (
        <BaseDiv>
            <ContainerDiv>
            <TextInfoDiv>
                <TitleSpan>{reservation.seat.room.name} - Seat {reservation.seat.id}</TitleSpan>
                <TextSpan>{parseDate(reservation.reservationDate).toLocaleDateString()}</TextSpan>
                <TextSpan>
                    { formatBlock(reservation.firstBlockReserved) } - { formatBlock(reservation.firstBlockReserved + reservation.blocksReserved) }
                </TextSpan>
            </TextInfoDiv>
            <CancelButton onClick={() => doPost({})}>Cancel</CancelButton>
            </ContainerDiv>
        </BaseDiv>
    );
}
  
export default Reservation;
  