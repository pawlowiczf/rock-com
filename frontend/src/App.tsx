import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { LoadScript } from "@react-google-maps/api";

import TopBar from "./pages/TopBar";
import Login from "./pages/Login";
import Register from "./pages/register/Register";
import AccountCreating from "./pages/register/AccountCreating";
import UserTypeChooser from "./pages/register/UserTypeChooser.tsx";
import JudgeLicence from "./pages/register/JudgeLicence";
import RegisterInformation from "./pages/register/RegisterInformation";
import CreateTournament from "./pages/organizer/CreateTournament";
import EditTournament from "./pages/organizer/EditTournament";
import EditMatch from "./pages/organizer/EditMatch";
import "./styles/App.css";
import ParticipantProfile from "./pages/user/ParticipantProfile";
import UserTournaments from "./pages/user/UserTournaments";
import OrganizerTournaments from "./pages/organizer/OrganizerTournaments";
import UpcomingMatches from "./pages/user/UpcomingMatches";
import JudgeScore from "./pages/judge/JudgeScore";

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
                          <Route
                              path="/register/account-creating"
                              element={<AccountCreating />}
                          />
                          <Route
                              path="/register/chose-user-type"
                              element={<UserTypeChooser />}
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
                              path="/tournaments/create"
                              element={<CreateTournament />}
                          />
                          <Route 
                              path="/tournaments/edit/:id" 
                              element={<EditTournament />} 
                          />
                          <Route 
                              path="/matches/edit" 
                              element={<EditMatch />} 
                          />
                          <Route
                                path="/profile"
                                element={<ParticipantProfile />}
                          />
                          <Route
                                element={<UserTournaments />}
                                path="/tournaments"
                            />
                          <Route
                                element={<OrganizerTournaments />}
                                path="/organizer/tournaments"
                            />
                          <Route 
                                path="/matches" 
                                element={<UpcomingMatches />} 
                          />
                          <Route 
                                path="/judge/score" 
                                element={<JudgeScore />} 
                          />
                          <Route path="*" element={<Login />} /> {/* Fallback */}
                      </Routes>
                  </div>
              </Router>
            </LoadScript>
        </>
    );
                        
}

export default App;
