import { useParams } from "react-router-dom";

export default function PatientDetails() {

    //const { patientId } = useParams();

    return (
        <div style="min-h-screen ">
            <h2>Patient Details</h2>

            

            <h3>Current Appointment</h3>
            <form>
                <label>Date:</label><br/>
                <input type="date" name="date" /><br/><br/>

                <label>Time:</label><br/>
                <input type="time" name="time" /><br/><br/>

                <label>Doctor:</label><br/>
                <input type="text" name="doctor" placeholder="Doctor name" /><br/><br/>

                <button type="submit">Book Appointment</button>
            </form>
        </div>
    );
}
