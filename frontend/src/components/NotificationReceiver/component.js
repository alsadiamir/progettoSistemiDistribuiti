import firebase, { firebaseVapidKey } from '../firebase/component';
import {useContext, useEffect, useState} from 'react';
import UserContext from '../UserContext/component';

function NotificationReceiver({ onNewToken }) {
    const authedUser = useContext(UserContext)
    const [messagingInstance, setMessagingInstance] = useState(null);
    const [firebaseError, setFirebaseError] = useState(null);
    const [fcmToken, setFcmToken] = useState(null);

    useEffect(() => {
        setMessagingInstance(firebase.messaging());
        setFirebaseError(null)
    }, [authedUser.id]);

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
        if(fcmToken) {
            onNewToken(fcmToken)
        }
    }, [fcmToken, onNewToken])
    
    return <></>;
  }
  
  export default NotificationReceiver;