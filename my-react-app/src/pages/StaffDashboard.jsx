// src/pages/StaffDashboard.jsx
import { Link } from "react-router-dom";

function StaffDashboard() {
  const cards = [
    {
      title: "Doctor Appointments",
      description: "View and manage upcoming appointments",
      borderColor: "border-blue-600",
      textColor: "text-blue-600",
      link: "/Appointment",
    },
    {
      title: "Check-in Patients",
      description: "Access patient records securely",
      borderColor: "border-green-600",
      textColor: "text-green-600",
      link: "/SearchPatient",
    },
    {
      title: "Staff Management",
      description: "Manage staff schedules and tasks",
      borderColor: "border-purple-600",
      textColor: "text-purple-600",
      link: "/PatientDetails",
    },
    {
      title: "Billing & Payments",
      description: "Manage billing and payment processing",
      borderColor: "border-orange-600",
      textColor: "text-orange-600",
    },
    {
      title: "Reports & Analysis",
      description: "Analyze performance and hospital reports",
      borderColor: "border-red-600",
      textColor: "text-red-600",
    },
    {
      title: "Profile & Settings",
      description: "Manage account settings and preferences",
      borderColor: "border-yellow-600",
      textColor: "text-yellow-600",
    },
    {
      title: "Patient Registration",
      description: "Fill out and submit patient information",
      borderColor: "border-teal-600",
      textColor: "text-teal-600",
      link: "/PatientReg",
    },
    {
      title: "Medicine / Pharmacy",
      description: "Manage medication and prescriptions",
      borderColor: "border-indigo-600",
      textColor: "text-indigo-600",
      link: "/Medicine",
    },
    {
      title: "Hospital Overview",
      description: "View overall hospital performance metrics",
      borderColor: "border-pink-600",
      textColor: "text-pink-600",
    },
  ];

  return (
    <div className="p-10 min-h-screen bg-gray-100">
      <h1 className="text-4xl font-bold mb-10 text-gray-800">
        Staff Dashboard
      </h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {cards.map((card) => (
          <Link
            key={card.title}
            to={card.link || "#"}
            className={`border ${card.borderColor} bg-white rounded-xl p-6 shadow-md hover:shadow-xl hover:-translate-y-1 transition-all`}
          >
            <h2 className={`text-2xl font-semibold mb-2 ${card.textColor}`}>
              {card.title}
            </h2>
            <p className="text-gray-600">{card.description}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default StaffDashboard;
