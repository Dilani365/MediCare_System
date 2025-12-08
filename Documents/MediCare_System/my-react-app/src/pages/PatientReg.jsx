import { useState } from "react";

function PatientReg() {
  const [name, setName] = useState("");
  const [NIC, setNIC] = useState("");
  const [date, setDate] = useState("");
  const [CurrentMeditation, setCurrentMeditation] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Name: ${name}\nEmail: ${NIC}\nDOB: ${date}\nCurrent Meditation: ${CurrentMeditation}`);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-100 to-blue-200">

      <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <h2 className="text-2xl font-bold text-center text-red-800 mb-6">
          Simple Form
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">

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
              type="email"
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
            <label> Current Meditation </label>
            <input
              type="text"
              placeholder="enter your current meditation and your allergies"
              value={CurrentMeditation}
              onChange={(e) => setCurrentMeditation(e.target.value)}></input>
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
