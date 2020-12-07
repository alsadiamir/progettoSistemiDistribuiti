import styled from 'styled-components'
import { useCallback, useEffect } from 'react';
import { useGetRequest } from '../../../hooks/useGetRequest';

const ReserveButton = styled.button`
    background-color: #2ecc71;
    box-decoration: none;
    outline: none;
    border: none;
    width: 100%;
    height: 100%;

    &:hover {
        background-color: #27ae60;
    }
`

const ReserveBusyButton = styled.button`
    background-color: #f1c40f;
    box-decoration: none;
    outline: none;
    border: none;
    width: 100%;
    height: 100%;

    &:hover {
        background-color: #f39c12;
    }
`

function Seat({seat, reservationDate, blockIndex, blocksCount}) { 
    const formatDate = (date) => {
        var d = date,
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;

        return encodeURIComponent([day, month, year].join('/'));
    }

    const {data, loading} = useGetRequest(`/seat/available?seatId=${seat.id}&reservationDate=${formatDate(reservationDate)}&block=${blockIndex}`)


    useEffect(() => {
        console.warn(data)
    }, [data])

    const onClick = useCallback((isBusy) => {
        // Prenota
    }, [seat.id, reservationDate, blockIndex])

    return (
        <>
            {!loading && data && (
                <>
                    {data.busy === true && (
                        <ReserveBusyButton onClick={onClick(true)}/>
                    )}
                    {data.busy !== true && (
                        <ReserveButton onClick={onClick(false)}/>
                    )}
                </>
            )}
            {loading && (
                <span>...</span>
            )}
        </>
    )
}
  
export default Seat;
  