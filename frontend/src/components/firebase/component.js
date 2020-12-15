import firebase from 'firebase'
import { firebaseConfig, firebaseVapidKey } from '../../firebase-config';

const firebaseApp = firebase.initializeApp(firebaseConfig);
firebaseApp.messaging().onMessage(function(payload) {
    const notificationTitle = payload.notification?.title || "New notification!";
    const notificationOptions = {
        body: payload.notification?.body || "Nothing to show",
    };
    navigator.serviceWorker.getRegistrations().then(list => {
        list.forEach(worker => {
            worker.showNotification(notificationTitle, notificationOptions)
        })
    })
});

export default firebase;
export { firebaseVapidKey };