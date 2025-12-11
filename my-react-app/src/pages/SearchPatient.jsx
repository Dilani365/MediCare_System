import { useState } from "react";

export default function SearchPatient() {

    const [patientID , setPatientID] = useState("");

    const handleSearch = (e) => {
        e.preventDefault();

        //navigate(`/PatientDetails/${patientID}`);

    }   
    return(
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-50 to-blue-200 p-5">
            <div className="bg-white shadow-xl rounded-2xl p-8 w-full max-w-lg">
                <h1 className="text-3xl font-bold text-center text-blue-700 mb-6"> Search Patient Details </h1>

                <form className="space-y-4" onSubmit={handleSearch}>
                    <div>
                        <label className="block font-medium mb-1">Enter Patient ID</label>
                        <input
                        type="text"
                        placeholder="Enter Patient ID"
                        value={patientID}
                        onChange={(e) => setPatientID(e.target.value)}
                        className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus-ring-blue-500 outline-none"
                        required/>
                    </div>

                    <button type="submit" className="w-full border rounded-lg bg-blue-600 p-2 hover:bg-blue-700 text-white"> Search </button>
                </form>
            </div>
        </div>
    )

       
}

