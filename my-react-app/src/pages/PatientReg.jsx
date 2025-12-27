import { useState } from "react";

function PatientReg() {
  const [name, setName] = useState("");
  const [NIC, setNIC] = useState("");
  const [date, setDate] = useState("");
  const [CurrentMeditation, setCurrentMeditation] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRegistration({ ...registration, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/registration", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          ...registration,
          quantity: Number(registration.quantity),
          price: Number(registration.price),
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to add medicine");
      }

      const savedMedicine = await response.json();
      console.log("Saved:", savedMedicine);

      // reset form
      setRegistration({
        name: "",
        NIC: "",
        date: "",
        CurrentMeditation: "",       
      });
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-100 to-blue-200">

      <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <h2 className="text-2xl font-bold text-center text-red-800 mb-6">
          Registration Form
        </h2>

        <form onSubmit={handleSubmit} className="space-y-3 text-left">

          <div>
            <label className="block mb-1 font-medium">Full Name:</label>
            <input
              type="text"
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus:ring-blue-400 outline-none"
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Identity Number:</label>
            <input
              type="text"
              placeholder="Enter your NIC"
              value={NIC}
              onChange={(e) => setNIC(e.target.value)}
              className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus:ring-blue-400 outline-none"
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Date of Birth:</label>
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus:ring-blue-400 outline-none"
            />
          </div>

          <div>
            <label className="block mb-1 font-medium"> Current Meditation </label>
            <input
              type="text"
              placeholder="enter your current meditation and your allergies"
              value={CurrentMeditation}
              onChange={(e) => setCurrentMeditation(e.target.value)}
              className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus:ring-blue-400 outline-none"/>
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
          >
            Submit
          </button>

        </form>
      </div>
    </div>
  );
}

export default PatientReg;
