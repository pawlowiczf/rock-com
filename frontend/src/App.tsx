import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import TopBar from "./pages/TopBar";
import Login from "./pages/Login";
import Register from "./pages/Register";
import AccountCreating from "./pages/AccountCreating";
import UserTypeChoser from "./pages/UserTypeChoser";
import JudgeLicence from "./pages/JudgeLicence";
import RegisterInformation from "./pages/RegisterInformation";
import ParticipantProfile from "./pages/ParticipantProfile";
import TennisTournaments from "./pages/TennisTournaments";
import UpcomingMatches from "./pages/UpcomingMatches";
import JudgeScore from "./pages/JudgeScore";

function App() {
    return (
        <>
            <Router>
                <div className="App">
                    <TopBar />
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route
                            path="/register/account-creating"
                            element={<AccountCreating />}
                        />
                        <Route
                            path="/register/chose-user-type"
                            element={<UserTypeChoser />}
                        />
                        <Route
                            path="/register/judge"
                            element={<JudgeLicence />}
                        />
                        <Route
                            path="/register/information"
                            element={<RegisterInformation />}
                        />
                        <Route
                            path="/profile"
                            element={<ParticipantProfile />}
                        />
                        <Route
                            path="/tournaments"
                            element={<TennisTournaments />}
                        />
                        <Route path="/matches" element={<UpcomingMatches />} />
                        <Route path="/judge/score" element={<JudgeScore />} />
                        <Route path="*" element={<Login />} /> {/* Fallback */}
                    </Routes>
                </div>
            </Router>
        </>
    );
}

export default App;
