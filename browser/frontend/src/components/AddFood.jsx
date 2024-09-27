import { useState } from "react";
import PropTypes from "prop-types";
import "./AddFood.css";
import colors from "../colorTheme";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import calculatePev from "../utilities";

export default function AddFood({ info, isDarkMode, setShowAddView, data, setData, setEditing }) {
    const [name, setName] = useState("");
    const [calories, setCalories] = useState(0);
    const [carbs, setCarbs] = useState(0);
    const [fats, setFats] = useState(0);
    const [proteins, setProteins] = useState(0);
    const [barcode, setBarcode] = useState("");

    const updateName = (e) => {
        setName(e.target.value);
    };

    const updateCalories = (e) => {
        setCalories(parseInt(e.target.value));
    };

    const updateCarbs = (e) => {
        setCarbs(parseFloat(e.target.value));
    };

    const updateFats = (e) => {
        setFats(parseFloat(e.target.value));
    };

    const updateProteins = (e) => {
        setProteins(parseFloat(e.target.value));
    };

    const updateBarcode = (e) => {
        setBarcode(e.target.value);
    };

    const handleSubmit = async () => {
        const newData = {
            name: name,
            calories: calories,
            carbohydrates: carbs,
            fats: fats,
            proteins: proteins,
            barcode: barcode,
        };
        const url = import.meta.env.VITE_API_URL || "";
        let response;
        if (info) {
            response = await axios.patch(url + "/api/foods/" + info.id, newData);
        } else {
            response = await axios.post(url + "/api/foods", newData);
            const newFullData = [...data, newData];
            newFullData.sort((a, b) => calculatePev(b.calories, b.proteins) - calculatePev(a.calories, a.proteins));
            setData(newFullData);
        }
        console.log(response.data);
    };

    const handleClose = () => {
        setShowAddView(false);
        setEditing(null);
    };

    return (
        <div
            className="add-food"
            style={{
                color: isDarkMode ? "white" : "black",
            }}
        >
            <div
                className="container"
                style={{
                    backgroundColor: isDarkMode ? colors.dark.primary.dark : colors.light.primary.dark,
                }}
            >
                <h2>{info ? "Edit food" : "Add New Food"}</h2>
                <button
                    className="close-button"
                    onClick={handleClose}
                    style={{
                        color: isDarkMode ? colors.dark.secondary.main : colors.light.secondary.main,
                    }}
                >
                    <FontAwesomeIcon icon={faXmark} className="icon" />
                </button>
                <form className="add-food-form" onSubmit={handleSubmit}>
                    <span>
                        <label htmlFor="">Name *</label>
                        <input
                            type="text"
                            value={info ? info.name : name}
                            onChange={updateName}
                            required
                            autoComplete="off"
                            style={{
                                color: isDarkMode ? "white" : "black",
                                borderColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.light,
                            }}
                        ></input>
                    </span>
                    <span>
                        <label htmlFor="">Calories *</label>
                        <input
                            type="number"
                            value={info ? info.calories : calories != 0 ? calories : ""}
                            required
                            onChange={updateCalories}
                            style={{
                                color: isDarkMode ? "white" : "black",
                                borderColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.light,
                            }}
                        ></input>
                    </span>
                    <span>
                        <label htmlFor="">Proteins *</label>
                        <input
                            type="number"
                            step="any"
                            required
                            value={info ? info.proteins : proteins != 0 ? proteins : ""}
                            onChange={updateProteins}
                            style={{
                                color: isDarkMode ? "white" : "black",
                                borderColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.light,
                            }}
                        ></input>
                    </span>
                    <span>
                        <label htmlFor="">Carbs</label>
                        <input
                            type="number"
                            step="any"
                            value={info ? info.carbs : carbs != 0 ? carbs : ""}
                            onChange={updateCarbs}
                            style={{
                                color: isDarkMode ? "white" : "black",
                                borderColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.light,
                            }}
                        ></input>
                    </span>
                    <span>
                        <label htmlFor="">Fats</label>
                        <input
                            type="number"
                            step="any"
                            value={info ? info.fats : fats != 0 ? fats : ""}
                            onChange={updateFats}
                            style={{
                                color: isDarkMode ? "white" : "black",
                                borderColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.light,
                            }}
                        ></input>
                    </span>
                    <span>
                        <label htmlFor="">Barcode</label>
                        <input
                            type="text"
                            value={info ? info.barcode : barcode}
                            onChange={updateBarcode}
                            style={{
                                color: isDarkMode ? "white" : "black",
                                borderColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.light,
                            }}
                        />
                    </span>
                    <input
                        type="submit"
                        style={{
                            color: isDarkMode ? "white" : "black",
                            backgroundColor: isDarkMode ? colors.dark.secondary.main : colors.light.secondary.main,
                            borderRadius: "5px",
                            fontSize: "20px",
                            fontWeight: "bold",
                            marginTop: "15px",
                            paddingTop: "10px",
                            paddingBottom: "10px",
                        }}
                    ></input>
                </form>
            </div>
        </div>
    );
}

AddFood.propTypes = {
    info: PropTypes.array,
    isDarkMode: PropTypes.bool,
    setShowAddView: PropTypes.func,
    data: PropTypes.array,
    setData: PropTypes.func,
};
