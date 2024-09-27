import { useEffect, useState } from "react";
import "./App.css";
import axios from "axios";

import Food from "./components/Food";
import colors from "./colorTheme";
import calculatePev from "./utilities";
import AddFood from "./components/AddFood";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSquarePlus } from "@fortawesome/free-solid-svg-icons";

export default function App() {
    const [data, setData] = useState([]);
    const [isDarkMode, setIsDarkMode] = useState(true);
    const [showAddView, setShowAddView] = useState(false);
    const [editing, setEditing] = useState(null);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        const url = import.meta.env.VITE_API_URL || "";
        const hr = await axios.get(url + "/api/foods");

        hr.data.sort((a, b) => calculatePev(b.calories, b.proteins) - calculatePev(a.calories, a.proteins));
        setData(hr.data);
    };

    return (
        <div
            className="app"
            style={{
                backgroundColor: isDarkMode ? colors.dark.primary.dark : colors.light.primary.main,
            }}
        >
            <div className="header">
                <h1
                    style={{
                        color: isDarkMode ? "white" : "black",
                        margin: 0,
                    }}
                >
                    PEC
                    <br />
                    <small>Protein Efficiency Calculator</small>
                </h1>
                <button
                    onClick={() => setShowAddView(true)}
                    style={{
                        color: isDarkMode ? colors.dark.secondary.main : "black",
                    }}
                >
                    <FontAwesomeIcon icon={faSquarePlus} className="add-icon" />
                </button>
            </div>
            {editing ? (
                <AddFood
                    info={editing}
                    isDarkMode={isDarkMode}
                    setShowAddView={setShowAddView}
                    data={data}
                    setData={setData}
                    setEditing={setEditing}
                />
            ) : null}
            {showAddView ? (
                <AddFood
                    info={editing}
                    isDarkMode={isDarkMode}
                    setShowAddView={setShowAddView}
                    data={data}
                    setData={setData}
                    setEditing={setEditing}
                />
            ) : null}
            <div className="food-list">
                {data.map((food, index) => (
                    <Food
                        key={index}
                        id={food.id}
                        name={food.name}
                        calories={food.calories}
                        carbs={food.carbohydrates}
                        fats={food.fats}
                        proteins={food.proteins}
                        barcode={food.barcode}
                        isDarkMode={isDarkMode}
                        data={data}
                        setData={setData}
                        setEditing={setEditing}
                        setShowAddView={setShowAddView}
                    />
                ))}
            </div>
            <button onClick={() => setIsDarkMode(!isDarkMode)}>Toggle Theme</button>
        </div>
    );
}
