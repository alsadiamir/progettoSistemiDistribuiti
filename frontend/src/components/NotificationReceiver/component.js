import firebase, { firebaseVapidKey } from '../firebase/component';
import {useContext, useEffect, useState} from 'react';
import UserContext from '../UserContext/component';
import { usePostRequest } from '../../hooks/usePostRequest';

function NotificationReceiver() {
    const authedUser = useContext(UserContext)
    const [messagingInstance, setMessagingInstance] = useState(null);
    const [firebaseError, setFirebaseError] = useState(null);
    const { doPost: updateUser, error: postError} = usePostRequest("/user")
    const [fcmToken, setFcmToken] = useState(null);

    useEffect(() => {
        setMessagingInstance(firebase.messaging());
        setFirebaseError(null)
    }, [authedUser]);

    useEffect(() => {
        if (messagingInstance || firebaseError) {
            messagingInstance.getToken({
                vapidKey: firebaseVapidKey
            }).then((currentToken) => {
                if (currentToken) {
                    setFcmToken(currentToken)
                } else {
                  setFirebaseError("Could not receive FCM device token")
                }
            }).catch((err) => {
                setFirebaseError(err.toString())    
            });
        }
    }, [messagingInstance, firebaseError]);

    useEffect(() => {
        if(fcmToken && authedUser.mail) {
            updateUser({
                mail: authedUser.mail,
                device: fcmToken,
            })
        }
    }, [fcmToken, authedUser.mail, updateUser, postError])
    
    return <></>;
  }
  
  export default NotificationReceiver;