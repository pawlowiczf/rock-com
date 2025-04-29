import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import TopBar from './pages/TopBar';
import Login from './pages/Login';

function App() {
  return (
    <>
    <Router>
      <div className="App">
        <TopBar />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="*" element={<Login />} /> {/* Fallback */}

        </Routes>
      </div>
    </Router>
    </>
  );
}

export default App;
