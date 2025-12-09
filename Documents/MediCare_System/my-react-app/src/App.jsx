

import {Routes, Route } from "react-router-dom"
import PatientReg from "./pages/PatientReg.jsx"
import StaffDashboard from "./pages/StaffDashboard.jsx"
import './App.css'

function App(){
  return (
    <Routes>
      <Route path="/" element={<StaffDashboard/>}/>
      <Route path="/PatientReg" element={<PatientReg/>}/>

    </Routes>
  )

}

export default App;