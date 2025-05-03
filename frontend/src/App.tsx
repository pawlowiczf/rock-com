import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { LoadScript } from "@react-google-maps/api";
import TopBar from './pages/TopBar';
import Login from './pages/Login';
import Register from './pages/Register';
import AccountCreating from "./pages/AccountCreating";
import UserTypeChoser from "./pages/UserTypeChoser";
import JudgeLicence from "./pages/JudgeLicence";
import RegisterInformation from "./pages/RegisterInformation";
import CreateTournament from "./pages/CreateTournament";
import EditTournament from "./pages/EditTournament";
import EditMatch from "./pages/EditMatch";
import "./styles/App.css";


function App() {
  return (
    <>
    <LoadScript googleMapsApiKey="AIzaSyARWy34KH0FwqnZeF4scwrJeXMoRHm7i9A" libraries={["places"]}>
    <Router>
      <div className="App">
        <TopBar />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/register/account-creating" element={<AccountCreating />} />
          <Route path="/register/chose-user-type" element={<UserTypeChoser />} />
          <Route path="/register/judge" element={<JudgeLicence />} />
          <Route path="/register/information" element={<RegisterInformation />} />
          <Route path="/tournaments/new" element={<CreateTournament />} />
          <Route path="/tournaments/edit" element={<EditTournament />} />
          <Route path="/matches/edit" element={<EditMatch />} />

          <Route path="*" element={<Login />} /> {/* Fallback */}

        </Routes>
      </div>
    </Router>
    </LoadScript>
    </>
  );
}

export default App;
