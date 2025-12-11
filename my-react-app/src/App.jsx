

import {Routes, Route } from "react-router-dom"
import PatientReg from "./pages/PatientReg.jsx"
import StaffDashboard from "./pages/StaffDashboard.jsx"
import './App.css'
import Appointment from "./pages/Appointment.jsx"
import SearchPatient from "./pages/SearchPatient.jsx"
import PatientDetails from "./pages/PatientDetails.jsx"


function App(){
  return (
    <Routes>
      <Route path="/" element={<StaffDashboard/>}/>
      <Route path="/PatientReg" element={<PatientReg/>}/>
      <Route path="/Appointment" element={<Appointment/>}/>
      <Route path="/SearchPatient" element={<SearchPatient/>}/>
      <Route path="/PatientDetails" element={<PatientDetails/>}/>

    </Routes>
  )

}

export default App;