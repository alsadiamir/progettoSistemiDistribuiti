import styled from 'styled-components'
import { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker'
import "react-datepicker/dist/react-datepicker.css";
import Select from 'react-select'

const ContainerDiv = styled.div`
    padding: 1rem;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
`;

const PickerContainerDiv = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: flex-center;
`;

const DatePickerDiv = styled.div`
    flex-grow: 3;
`;

const TimePickerDiv = styled.div`
    flex-grow: 3;
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

const timeOptions = [
    { value: 1, label: '15 Mins' },
    { value: 2, label: '30 Mins' },
    { value: 3, label: '1 Hour' }
  ]

const mapTile = (seats, x, y) => {
    const seat = seats.find((r) => r.x === x && r.y === y);
    if (seat) {
        return (
            <button>{seat.id}</button>
        )
    }
    return (
        <span></span>
    )
}

function RoomPage({roomID, onGoBack}) {  
    // TODO: Grab those from backend and show loading state
    const [seats, setSeats] = useState([
        { id: 0, x: 1, y: 1},
        { id: 1, x: 2, y: 1},
        { id: 2, x: 1, y: 2},
        { id: 3, x: 4, y: 4},
        { id: 4, x: 7, y: 10},
    ]);
    const [timeOption, setTimeOption] = useState(timeOptions[0])
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
        </ContainerDiv>
    );
}
  
export default RoomPage;
  