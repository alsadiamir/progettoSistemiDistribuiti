import styled from 'styled-components'
import { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker'
import "react-datepicker/dist/react-datepicker.css";
import Select from 'react-select'
import Seat from './Seat/component'

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


const timeOptions = [
    { value: 1, label: '15 Mins' },
    { value: 2, label: '30 Mins' },
    { value: 3, label: '1 Hour' }
]

const mapTile = (seats, x, y) => {
    const seat = seats.find((r) => r.x === x && r.y === y);
    if (seat) {
        return (
            <Seat seat={seat} />
        )
    }
    return (
        <span></span>
    )
}

function RoomPage({room, onGoBack}) {  
    // TODO: Grab those from backend and show loading state
    const [seats, setSeats] = useState([
        { id: 0, x: 1, y: 1},
        { id: 1, x: 2, y: 1},
        { id: 2, x: 1, y: 2},
        { id: 3, x: 4, y: 4},
        { id: 4, x: 7, y: 10},
    ]);
    const [timeOption, setTimeOption] = useState(timeOptions[1])
    const [startDate, setStartDate] = useState(new Date());
    const [roomWidth, setRoomWidth] = useState(0);
    const [roomHeight, setRoomHeight] = useState(0);

    useEffect(() => {
        var maxW = 0;
        var maxH = 0;

        if (seats) {
            seats.forEach((s) => {
                if(s.x > maxW) {
                    maxW = s.x + 1;
                }
                if(s.y > maxH) {
                    maxH = s.y + 1;
                }
            })
        }

        setRoomHeight(maxH);
        setRoomWidth(maxW);
    }, [seats])

    return (
        <ContainerDiv>
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
                <TimePickerDiv>
                    <Select value={timeOption} options={timeOptions} onChange={(v) => setTimeOption(v)}/>
                </TimePickerDiv>
            </PickerContainerDiv>
            <h3>Choose the seat you want to reserve</h3>
            <GridContainer>
                <Grid>
                    {Array(roomHeight).fill().map((_, y) => (
                        <Row>
                            {Array(roomWidth).fill().map((_, x) => (
                                <Cell>
                                    {mapTile(seats, x, y)}
                                </Cell>
                            ))}
                        </Row>
                    ))}
                </Grid>   
            </GridContainer>  
        </ContainerDiv>
    );
}
  
export default RoomPage;
  