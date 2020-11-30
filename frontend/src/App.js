import './App.css';
import AuthMiddleware from './components/AuthMiddleware/component';
import NotificationReceiver from './components/NotificationReceiver/component';
import HomePage from './pages/HomePage/component';

function App() {
    return (
        <div className="App">
            <AuthMiddleware>
                <NotificationReceiver/>
                <HomePage/>
            </AuthMiddleware>
        </div>
    );
}

export default App;
