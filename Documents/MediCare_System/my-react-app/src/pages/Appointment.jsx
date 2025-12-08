import { useState } from "react";

function Appointment(){

    const [formData, setFormData] = useState ({
        patientID: "",
        hospital: "",
        department: "",
        appointmentDate: "",
        appointmentTime: "",
        doctor: "",
        
    });

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        alert(`
            PatientID: ${formData.patientID} 
            Hospital: ${formData.hospital}
            Department: ${formData.department}
            AppointmentDate: ${formData.appointmentDate}
            AppointmentTime: ${formData.appointmentTime}
            Doctor: ${formData.doctor}`
            );
    };

    return(
        <div className="min-h-screen flex item-center justify-center from-blue-50 to-blue-200 p-5">
            <div className="bg-white shadow-2xl p-8 rounded-2xl w-full max-w-lg">
                <h1 className="text-3xl font-bold text-center text-blue-700 mb-6">
                    Book Appoitment 
                </h1>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block font-medium mb-1">PatientID</label>
                        <input
                        type="text"
                        name="patientID"
                        placeholder="Your NIC"
                        value={formData.patientID}
                        onChange={handleChange}
                        className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus:ring-blue-400 outline-none"
                        required
                        />
                    </div>

                    <div>
                        <label className="block font-medium mb-1">Hospital</label>
                        <select
                        type="text"
                        name="hospital"
                        placeholder="select hospital"
                        value={formData.hospital}
                        onChange={handleChange}
                        className="w-full border border-gray-300 p-2 rounded-lg focus:ring-2 focus:ring-blue-400 outline-none"
                        required
                        >
                            <option value="">-- Choose Doctor --</option>
                            <option value="Dr.nimal"></option>
                            <option value="Dr.saman"></option>
                        </select>
                    </div>

                    <div>
                        
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Appointment;
        