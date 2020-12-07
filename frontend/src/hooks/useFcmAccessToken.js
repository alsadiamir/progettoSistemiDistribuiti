import firebase, { firebaseVapidKey } from '../components/firebase/component';
import {useCallback, useState} from 'react';

export default function useFcmAccessToken() {
    const [error, setError] = useState(null);
    const [token, setToken] = useState(null);

    const refresh = useCallback(() => {
        const messagingInstance = firebase.messaging()
        setError(null)
        setToken(null)
        if (messagingInstance) {
            messagingInstance.getToken({
                vapidKey: firebaseVapidKey
            }).then((currentToken) => {
                if (currentToken) {
                    setToken(currentToken)
                } else {
                  setError("Could not receive FCM device token")
                }
            }).catch((err) => {
                setError(err.toString())    
            });
        }
    }, []);
    
    return {refresh, token, error}
  }
