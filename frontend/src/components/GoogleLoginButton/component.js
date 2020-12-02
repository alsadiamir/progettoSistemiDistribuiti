import styled from 'styled-components'
import firebase from '../firebase/component';
import {useState} from 'react';

const GoogleButton = styled.button`
    display: flex;
    flex-direction: row;
    justify-content: center;
    padding: 1rem;
    border-radius: 5rem;
    margin-bottom: 0.5rem;
    background-color: whitesmoke;
    box-decoration: none;
    outline: none;
    border: none;
    
    &:hover {
        background-color: #95a5a6;
    }
`;

const GoogleLogo = styled.img`
    width: 2rem;
    height: 2rem;
    align-self: center;
    margin-right: 0.5rem;
`

const TextSpan = styled.span`
    margin-left: 0.5rem;
    font-size: large;
    align-self: center;
`

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
        <GoogleButton
            onClick={handleClick}
        >
            <GoogleLogo
                src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/1024px-Google_%22G%22_Logo.svg.png"
                alt =""
            />
            <TextSpan>Login with Google</TextSpan>
        </GoogleButton>
    );
  }
  
  export default GoogleLoginButton;
  