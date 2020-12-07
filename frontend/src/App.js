import './App.css';
import AuthMiddleware from './components/AuthMiddleware/component';
import HomePage from './pages/HomePage/component';

function App() {
    return (
        <div className="App">
            <AuthMiddleware>
                <HomePage/>
            </AuthMiddleware>
        </div>
    );
}

export default App;
