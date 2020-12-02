import { useState } from 'react';
import GoogleLoginButton from '../GoogleLoginButton/component';
import UserContext from '../UserContext/component';
import ErrorBox from '../ErrorBox/component'
import styled from 'styled-components';

const ContainerDiv = styled.div`
    margin: auto;10rem
    margin-top:10rem;
    width: 30rem;
`

const CenterDiv = styled.div`
    display: flex;
    margin: auto;
    flex-direction: column;
    justify-content: center;
`

const WelcomeText = styled.h3`
`

function AuthMiddleware({ children }) {
    const [authedUser, setAuthedUser] = useState(null);
    const [lastError, setLastError] = useState(null);

    const onSuccess = (user) => {
        setAuthedUser(user);
        setLastError(null);
    };

    return (    
        <UserContext.Provider value={authedUser} >
            {!authedUser && (
                <ContainerDiv>
                    <CenterDiv>
                        <WelcomeText>
                            You are not authenticated in the system.<br/>Please proceed with the user authentication<br/><br/>
                        </WelcomeText>
                        <GoogleLoginButton
                            onSuccess={onSuccess}
                            onFailure={setLastError} 
                        />
                    </CenterDiv>
                </ContainerDiv>
            )}
            {!authedUser && lastError && (
                <ErrorBox>
                    {lastError}
                </ErrorBox>
            )}
            {authedUser && (
                <>{children}</>
            )}
        </UserContext.Provider>
    );
}

export default AuthMiddleware;
