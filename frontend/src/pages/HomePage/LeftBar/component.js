import styled from 'styled-components'

const ContainerDiv = styled.div`
    background-color: #34495e;
    border-radius: 1rem;
    padding: 1rem;
    padding-top: 0;
    border-right: 1px solid #444444;
    display: flex;
    flex-direction: column;
`;

const TitleDiv = styled.div`
    text-align: center;
    color: whitesmoke;
`;

const MenuEntryDiv = styled.button`
    padding: 0.5rem;
    border-radius: 0.5rem;
    margin-bottom: 0.5rem;
    background-color: whitesmoke;
    box-decoration: none;
    outline: none;
    border: none;
    
    &:hover {
        background-color: #95a5a6;
    }
`;

function LeftBar( {userName, menuOptions, onSelection}) {  
    return (
        <ContainerDiv>
            <TitleDiv>
                <h1>Menu</h1>
                <h2>Welcome, {userName}</h2>
            </TitleDiv>
            {menuOptions.map((m => (
                <MenuEntryDiv
                    key={m.code}
                    onClick={() => onSelection(m.code)}
                >
                    <h3>{m.title}</h3>
                </MenuEntryDiv>
            )))}
        </ContainerDiv>
    );
}
  
export default LeftBar;
  