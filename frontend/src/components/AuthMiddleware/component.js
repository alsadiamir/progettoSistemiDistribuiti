import { useState, useEffect } from 'react';
import GoogleLoginButton from '../GoogleLoginButton/component';
import UserContext from '../UserContext/component';
import ErrorBox from '../ErrorBox/component'
import styled from 'styled-components';
import { usePostRequest } from '../../hooks/usePostRequest';

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
    const [lastError, setLastError] = useState(null);
    const {doPost, data, error} = usePostRequest("/user")
    
    const onSuccess = (user) => {
        const userData = {
            mail: user.email,
        }
        doPost(userData, userData)
    };

    useEffect(() => {
        if(error !== null) {
            setLastError(error)
        }
    }, [error])

    return (    
        <UserContext.Provider value={data} >
            {!data && (
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
            {!data && lastError && (
                <ErrorBox>
                    {lastError}
                </ErrorBox>
            )}
            {data && (
                <>{children}</>
            )}
        </UserContext.Provider>
    );
}

export default AuthMiddleware;
