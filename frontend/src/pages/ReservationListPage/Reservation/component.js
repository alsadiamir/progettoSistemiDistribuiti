import styled from 'styled-components'

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
    // TODO: Grab room info from backend
    return (
        <BaseDiv>
            <ContainerDiv>
                <TextInfoDiv>
                    <TitleSpan>{reservation.seatId}</TitleSpan>
                    <TextSpan>{reservation.date}</TextSpan>
                    <TextSpan>{reservation.hourIndex} - {reservation.duration}</TextSpan>
                </TextInfoDiv>
                <CancelButton onClick={() => onCancel(reservation)}>Cancel</CancelButton>
            </ContainerDiv>
        </BaseDiv>
    );
}
  
export default Reservation;
  