import styled from 'styled-components'
import { useCallback, useContext, useEffect, useState } from 'react';
import useGetRequest from '../../../hooks/useGetRequest';
import usePostRequest from '../../../hooks/usePostRequest';
import UserContext from '../../../components/UserContext/component';

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

        return [day, month, year].join('/');
    }

    const authedUser = useContext(UserContext)
    const {data, loading, refetch} = useGetRequest(`/seat/available?seatId=${seat.id}&reservationDate=${encodeURIComponent(formatDate(reservationDate))}&block=${blockIndex}`)
    const {doPost, data: postData, error: postError} = usePostRequest(`/reservation`)
    const [posted, setPosted] = useState(false)

    useEffect(() => {
        console.warn(postData)
    }, [postData, postError])

    const onClick = useCallback((_) => {
       doPost({
           user: {
               id: authedUser.id,
           },
           seat: {
               id: seat.id,
           },
           reservationDate: formatDate(reservationDate),
           firstBlockReserved: blockIndex,
           blocksReserved: blocksCount,
       })
       setPosted(true)
    }, [doPost, seat.id, authedUser, reservationDate, blockIndex, blocksCount, setPosted])

    useEffect(() => {
        if (posted && postError)  {
            alert("Something went wrong with your reservation! :(")
            setPosted(false)
        } if(posted && postData) {
            alert("You reserved this seat!")
            refetch()
            setPosted(false)
        }
    }, [postData, postError, refetch, posted, setPosted])

    return (
        <>
            {!loading && data && (
                <>
                    {data.busy === true && (
                        <ReserveBusyButton onClick={() => onClick(true)}/>
                    )}
                    {data.busy !== true && (
                        <ReserveButton onClick={() => onClick(false)}/>
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
  