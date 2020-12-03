import firebase from 'firebase'

var config = {
    apiKey: "AIzaSyClXtq-s3OWWUVblP4WzUlU0KU4bQ9NKLs",
    authDomain: "sistemi-distribuiti-m.firebaseapp.com",
    databaseURL: "https://sistemi-distribuiti-m.firebaseio.com",
    projectId: "sistemi-distribuiti-m",
    storageBucket: "sistemi-distribuiti-m.appspot.com",
    messagingSenderId: "853386000336",
    appId: "1:853386000336:web:aa512308684caefc522bd5"
};

firebase.initializeApp(config);

const firebaseVapidKey = "BCRlbGW-Q1qNkbOnk_wBqZtOMWUAQiOOCHO1GJDYEDUwoudLH87GQOj5QcvFTi7IMaFW2P6mUxW5-bqEHg2qCSE";

export default firebase;
export { firebaseVapidKey };