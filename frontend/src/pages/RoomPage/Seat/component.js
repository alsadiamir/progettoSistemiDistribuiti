import styled from 'styled-components'

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
`;

function Seat({seat}) { 
    // TODO: Distinguish seats that already have a reservation (maybe show them in yellow)?
    return (
        <ReserveButton />
    );
}
  
export default Seat;
  