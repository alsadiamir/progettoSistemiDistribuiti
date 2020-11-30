importScripts("https://www.gstatic.com/firebasejs/5.9.4/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/5.9.4/firebase-messaging.js");
var firebaseConfig = {
    apiKey: "AIzaSyClXtq-s3OWWUVblP4WzUlU0KU4bQ9NKLs",
    authDomain: "sistemi-distribuiti-m.firebaseapp.com",
    databaseURL: "https://sistemi-distribuiti-m.firebaseio.com",
    projectId: "sistemi-distribuiti-m",
    storageBucket: "sistemi-distribuiti-m.appspot.com",
    messagingSenderId: "853386000336",
    appId: "1:853386000336:web:aa512308684caefc522bd5"
  };
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();
messaging.setBackgroundMessageHandler(function(payload) {
     const promiseChain = clients
          .matchAll({
               type: "window",
               includeUncontrolled: true,
          })
          .then((windowClients) => {
               for (let i = 0; i < windowClients.length; i++) {
                    const windowClient = windowClients[i];
                    windowClient.postMessage(payload);
               }
          })
          .then(() => {
               return registration.showNotification("my notification title");
          });
     return promiseChain;
});
self.addEventListener("notificationclick", function(event) {
     console.log(event);
});