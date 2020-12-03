import firebase, { firebaseVapidKey } from '../firebase/component';
import {useContext, useEffect, useState} from 'react';
import UserContext from '../UserContext/component';
import {BACKEND_ADDR} from '../../constants';

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
                    const userData = {
                        mail: authedUser.email,
                        device: currentToken,
                    }
                    const requestOptions = {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(userData),
                    };
                    fetch('/user', requestOptions).catch(err => console.err(err));
                } else {
                  // Show some sort of error and try again?
                }
              }).catch((err) => {
                // Show some sort of error and try again?
              });
        }
    }, [messagingInstance, authedUser]);
    
    return <></>;
  }
  
  export default NotificationReceiver;