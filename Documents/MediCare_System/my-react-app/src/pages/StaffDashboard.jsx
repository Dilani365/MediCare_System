// src/pages/StaffDashboard.jsx
import { Link } from "react-router-dom";

function StaffDashboard() {

    const cards = [
        {
            title: "Doctor Appointments", 
            description: "View and manage upcooming appointments",
            borderColor: "border-blue-600",
            textColor: "text-blue-600"
        },

        {
            title: "Check-in Patients",
            description: "Access patient record securly",
            borderColor: "border-color-600",
            textColor: "text-color=600"
        },

        {
            title: "Staff Management",
            description: "Manage staff schedule and tasks",
            boderColor: "border-color-600",
            textColor: "text-color-600"
        },

        {
            title: "Billing & Payments",
            description: "Managing billing information and payment process",
            boderColor: "border-color-600",
            textColor: "text-color-600"
        },

        {
            title: "Report & Analysis",
            description: "View and analysis hospital performance analysis",
            boderColor: "border-color-600",
            textColor: "text-color-600"
        },

        {
            title: "Profile and Setting",
            description: "Manage account settings and preferences",
            boderColor: "border-color-600",
            textColor: "text-color-600"
        },

        {
            title: "Patint Registration",
            description: "Fill out and submit patient information",
            boderColor: "border-color-600",
            textColor: "text-color-600",
            link: "/PatientReg"
        },

        {
            title: "Medicine/Farmacy Section",
            description: "Manage meditation and prescription",
            boderColor: "border-color-600",
            textColor: "text-color-600"
        },

        {
            title: "Hospital",
            description: "View and analysis hospital performance metrics",
            boderColor: "border-color-600",
            textColor: "text-color-600"
        }
    ]


  return (
    <div className="p-10">
      <h1 className="text-3xl font-bold mb-6">Staff Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {cards.map((card) => (
            <Link 
                key={card.title}
                to={card.link}
                className="`${card.color} text-white p-6 rounded-lg shadow-lg flex item-center hover:scale-105 transition-transform`"
            >
                <h2 className="text-xl font-bold">{card.title}</h2>
            </Link>
        ))
            
        }
      </div>
    </div>
  );
}

export default StaffDashboard;
