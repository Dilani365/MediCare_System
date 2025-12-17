import { useState } from "react";

function MedicineInventory() {
  const [medicine, setMedicine] = useState({
    name: "",
    category: "",
    quantity: "",
    price: "",
    expiryDate: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMedicine({ ...medicine, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    console.log("Medicine Added:", medicine);

    // reset form
    setMedicine({
      name: "",
      category: "",
      quantity: "",
      price: "",
      expiryDate: "",
    });
  };

  return (
    <div className="max-w-md mx-auto mt-8 p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-semibold mb-6">Add Medicine</h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="flex flex-col">
          <label className="mb-1 font-medium">Medicine Name</label>
          <input
            type="text"
            name="name"
            value={medicine.name}
            onChange={handleChange}
            required
            className="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex flex-col">
          <label className="mb-1 font-medium">Category</label>
          <input
            type="text"
            name="category"
            value={medicine.category}
            onChange={handleChange}
            className="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex flex-col">
          <label className="mb-1 font-medium">Quantity</label>
          <input
            type="number"
            name="quantity"
            value={medicine.quantity}
            onChange={handleChange}
            required
            className="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex flex-col">
          <label className="mb-1 font-medium">Price</label>
          <input
            type="number"
            step="0.01"
            name="price"
            value={medicine.price}
            onChange={handleChange}
            required
            className="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex flex-col">
          <label className="mb-1 font-medium">Expiry Date</label>
          <input
            type="date"
            name="expiryDate"
            value={medicine.expiryDate}
            onChange={handleChange}
            required
            className="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <button
          type="submit"
          className="mt-4 w-full bg-blue-500 text-white font-medium py-2 rounded hover:bg-blue-600 transition-colors"
        >
          Add Medicine
        </button>
      </form>
    </div>
  );
}

export default MedicineInventory;
