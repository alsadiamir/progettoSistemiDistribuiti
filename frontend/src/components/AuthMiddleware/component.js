import { useState } from 'react';
import GoogleLoginButton from '../GoogleLoginButton/component';
import UserContext from '../UserContext/component';
import ErrorBox from '../ErrorBox/component'

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
                <>
                    <span>You are not authenticated in the system.<br/>Please proceed with the user authentication<br/><br/></span>
                    <GoogleLoginButton
                        onSuccess={onSuccess}
                        onFailure={setLastError} 
                    />
                </>
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
