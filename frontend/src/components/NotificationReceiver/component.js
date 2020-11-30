import firebase, { firebaseVapidKey } from '../firebase/component';
import {useContext, useEffect, useState} from 'react';
import UserContext from '../UserContext/component';

function NotificationReceiver() {
    const authedUser = useContext(UserContext)
    const [messagingInstance, setMessagingInstance] = useState(null);

    useEffect(() => {
        setMessagingInstance(firebase.messaging());
    }, [authedUser]);

    useEffect(() => {
        if (messagingInstance) {
            messagingInstance.getToken({
                vapidKey: firebaseVapidKey
            }).then((currentToken) => {
                if (currentToken) {
                  // Send the token to the server
                  console.info(currentToken)
                } else {
                  // Show some sort of error and try again?
                }
              }).catch((err) => {
                // Show some sort of error and try again?
              });
            console.log(messagingInstance)
        }
    }, [messagingInstance]);
    
    return <></>;
  }
  
  export default NotificationReceiver;