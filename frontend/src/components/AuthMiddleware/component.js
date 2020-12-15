import { useState, useEffect } from 'react';
import GoogleLoginButton from '../GoogleLoginButton/component';
import UserContext from '../UserContext/component';
import ErrorBox from '../ErrorBox/component'
import styled from 'styled-components';
import usePostRequest from '../../hooks/usePostRequest';
import useFcmAccessToken from '../../hooks/useFcmAccessToken'

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
    const [googleUser, setGoogleUser] = useState(null)
    const [curUser, setCurUser] = useState(null)
    const {refresh: refreshDevice, token: device, error: deviceError} = useFcmAccessToken()
    const {doPost, data, error} = usePostRequest("/user")
    
    const onSuccess = (user) => {
        setGoogleUser(user)
        //console.log(user)
        refreshDevice()
    };

    useEffect(() => {
        if(deviceError) {
            setLastError(deviceError)
        } else if (device && googleUser) {
            setCurUser({
                mail: googleUser.email,
                displayName: googleUser.displayName,
                accessToken: googleUser.accessToken
            })
            doPost({
                mail: googleUser.email,
                device: device,
            }, googleUser.accessToken)
        }
    }, [doPost, googleUser, device, deviceError])

    useEffect(() => {
        if(data && !error) {
            setCurUser(c => {
                return {
                    ...c,
                    device: data.device,
                    id: data.id,
                }
            })
        }
    }, [data, error, setCurUser])

    useEffect(() => {
        if(error !== null) {
            setLastError(error)
        }
    }, [error])

    useEffect(() => {
        if(curUser !== null) {
            console.log(curUser)
        }
    }, [curUser])

    return (    
        <UserContext.Provider value={curUser} >
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
            {curUser && (
                <>
                    {children}
                </>
            )}
        </UserContext.Provider>
    );
}

export default AuthMiddleware;
