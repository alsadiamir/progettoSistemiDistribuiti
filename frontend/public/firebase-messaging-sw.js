import { firebaseConfig } from './firebase-config.js';
importScripts("https://www.gstatic.com/firebasejs/8.1.2/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/8.1.2/firebase-messaging.js");
importScripts("https://www.gstatic.com/firebasejs/8.1.2/firebase-analytics.js");

const firebaseApp = firebase.initializeApp(firebaseConfig);
const messaging = firebaseApp.messaging()

messaging.onBackgroundMessage(function(payload) {
     console.log('[firebase-messaging-sw.js] Received background message ', payload);
     const notificationTitle = payload.notification.title;
     const notificationOptions = {
          body: payload.notification.body,
     };
   
     self.registration.showNotification(notificationTitle, notificationOptions);
});
