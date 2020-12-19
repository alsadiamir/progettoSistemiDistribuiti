import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

if ("serviceWorker" in navigator) {
  navigator.serviceWorker
    .register("./firebase-messaging-sw.js")
    .then(function(registration) {
      console.log("Registration successful, scope is:" + registration.scope);
    })
    
}

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

