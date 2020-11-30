import './style.css';
import firebase from '../firebase/component';
import {useState} from 'react';

function GoogleLoginButton({
    onSuccess,
    onFailure
}) {
    const [provider] = useState(new firebase.auth.GoogleAuthProvider()); 
    const handleClick = () => {
        firebase.auth().signInWithPopup(provider).then(function(result) {
            onSuccess({
                email: result.user.email,
                displayName: result.user.displayName,
                accessToken: result.credential.accessToken,
            })
          }).catch(function(error) {
            onFailure(error.message)
          });
    }
    return (
        <button
            className="google-button"
            onClick={handleClick}
        >
            <span>Login with Google</span>
        </button>
    );
  }
  
  export default GoogleLoginButton;
  