import { useContext } from 'react';
import UserContext from '../../components/UserContext/component';
import './style.css';

function HomePage() {
    const authedUser = useContext(UserContext)
    return (
        <span>Hello {authedUser.displayName}</span>
    );
  }
  
  export default HomePage;
  