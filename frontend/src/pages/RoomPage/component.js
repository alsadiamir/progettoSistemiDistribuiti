import styled from 'styled-components'
import { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker'
import "react-datepicker/dist/react-datepicker.css";
import Select from 'react-select'
import Seat from './Seat/component'
import { useGetRequest } from '../../hooks/useGetRequest';
import ErrorBox from '../../components/ErrorBox/component';

const blockSizeInMinutes = 15;

const ContainerDiv = styled.div`
    text-align: center;
    self-align: center;
`;

const PickerContainerDiv = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: center;
`;

const DatePickerDiv = styled.div`
    width: 8rem;
    margin-right: 8rem;
    margin-top: 0.5rem;
`;

const TimePickerDiv = styled.div`
    width: 8rem;
`;

const GridContainer = styled.div`
    display: flex;
    justify-content: center;
`;

const Grid = styled.table`
    border: 1px solid black;
    border-spacing: 0;
`;

const Row = styled.tr`
    border: none;
`;

const Cell = styled.td`
    border: 1px solid black;
    width: 3rem;
    height: 3rem;
`;

const PageTitle = styled.h1`
    text-align: center;
`;

const mapTile = (seats, x, y, reservationDate, blockIndex, blocksCount) => {
    const seat = seats.find((r) => r.x === x && r.y === y);
    if (seat) {
        return (
            <Seat seat={seat} reservationDate={reservationDate} blockIndex={blockIndex} blocksCount={blocksCount} />
        )
    }
    return (
        <span></span>
    )
}

function RoomPage({room, onGoBack}) {  
    const {data, loading, error} = useGetRequest("/seats?roomId=" + room.id)
    const [timeOptions, setTimeOptions] = useState(null)
    const [fromBlock, setFromBlock] = useState(null)
    const [toBlock, setToBlock] = useState(null)
    const [startDate, setStartDate] = useState(new Date());
    const [roomWidth, setRoomWidth] = useState(0);
    const [roomHeight, setRoomHeight] = useState(0);

    useEffect(() => {
        var maxW = 0;
        var maxH = 0;

        if (data && !loading && !error) {
            data.forEach((s) => {
                if(s.x + 1 > maxW) {
                    maxW = s.x + 1;
                }
                if(s.y + 1 > maxH) {
                    maxH = s.y + 1;
                }
            })
        }

        setRoomHeight(maxH);
        setRoomWidth(maxW);
    }, [data, loading, error])

    useEffect(() => {
        const opening = room.openingTime.hour * 60 + room.openingTime.minute
        const closing = room.closingTime.hour * 60 + room.closingTime.minute
        const numBlocks = Math.floor((closing - opening) / blockSizeInMinutes)
        var newTimeOptions = []
        var curTimeBlock = 0
        for(let i = 0; i < numBlocks + 1; i++) {
            var now = Date.now()
            var dateNow = new Date(now)
            var blockMinutes = opening + i * blockSizeInMinutes
            dateNow.setHours(Math.floor(blockMinutes / 60), blockMinutes % 60, 0)
            newTimeOptions.push({
                value: i,
                label: dateNow.toLocaleTimeString()
            })
            if(new Date(now) > dateNow) {
                curTimeBlock = i
            }
        }
        setTimeOptions(newTimeOptions)
        setFromBlock(curTimeBlock)
        setToBlock(curTimeBlock + 1)
    }, [room.openingTime, room.closingTime])

    return (
        <ContainerDiv>
            {!loading && !error && data && (
                <>
                    <PageTitle>{room.name}</PageTitle>
                    <h3>Choose Date and time of the reservation</h3>
                    <PickerContainerDiv>
                        <DatePickerDiv>
                            <DatePicker
                                minDate={new Date()}
                                selected={startDate}
                                onChange={setStartDate}
                            />
                        </DatePickerDiv>
                        {timeOptions && (
                            <>
                                <TimePickerDiv>
                                    <Select value={timeOptions[fromBlock]} options={timeOptions} onChange={(v) => {
                                        if(v.value < timeOptions.length - 1) {
                                            setFromBlock(v.value)
                                            if (v.value >= toBlock) {
                                                setToBlock(v.value + 1)
                                            }
                                        }  
                                    }}/>
                                </TimePickerDiv>
                                <TimePickerDiv>
                                    <Select value={timeOptions[toBlock]} options={timeOptions} onChange={(v) => v.value > fromBlock && setToBlock(v.value)}/>
                                </TimePickerDiv>
                            </>
                        )}
                    </PickerContainerDiv>
                    <h3>Choose the seat you want to reserve</h3>
                    <GridContainer>
                        <Grid>
                            {Array(roomHeight).fill().map((_, y) => (
                                <Row>
                                    {Array(roomWidth).fill().map((_, x) => (
                                        <Cell>
                                            {mapTile(data, x, y, startDate, fromBlock, toBlock - fromBlock)}
                                        </Cell>
                                    ))}
                                </Row>
                            ))}
                        </Grid>   
                    </GridContainer> 
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
  
export default RoomPage;
  