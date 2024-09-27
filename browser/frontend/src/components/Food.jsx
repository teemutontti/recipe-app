import PropTypes from "prop-types";
import colors from "../colorTheme";
import "./Food.css";
import axios from "axios";
import { useEffect, useState } from "react";
import calculatePev from "../utilities";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronDown, faChevronUp, faCircle, faEllipsisVertical } from "@fortawesome/free-solid-svg-icons";

export default function Food({ id, name, calories, carbs, fats, proteins, barcode, isDarkMode, data, setData, setEditing }) {
    const [proteinEfficiencyPercentage, setProteinEfficiencyPercentage] = useState(0);
    const [showData, setShowData] = useState(false);

    useEffect(() => {
        const newProteinEfficiencyPercentage = calculatePev(calories, proteins);
        setProteinEfficiencyPercentage(newProteinEfficiencyPercentage);
    }, [calories, proteins]);

    const handleDelete = async () => {
        const url = import.meta.env.VITE_API_URL || "";
        const hr = await axios.delete(url + "/api/foods/" + id);

        console.log(hr.data);

        const newData = data.filter((x) => x.id != id);
        setData(newData);
    };

    const handleEdit = () => {
        setEditing({
            name,
            calories,
            carbs,
            fats,
            proteins,
            barcode,
        });
    };

    return (
        <div
            className="food-element"
            style={{
                color: isDarkMode ? "white" : "black",
                backgroundColor: isDarkMode ? colors.dark.primary.main : colors.light.primary.main,
            }}
        >
            <FontAwesomeIcon
                icon={faCircle}
                className="circle-icon"
                style={{
                    color: proteinEfficiencyPercentage > 50 ? "green" : proteinEfficiencyPercentage < 25 ? "red" : "orange",
                }}
            />
            <div className="food-header">
                <h2>{name}</h2>
                <button
                    className="show-actions-button"
                    style={{
                        color: isDarkMode ? "white" : "black",
                    }}
                >
                    <FontAwesomeIcon icon={faEllipsisVertical} className="icon" />
                </button>
                <ul
                    className="food-actions"
                    style={{
                        color: isDarkMode ? "white" : "black",
                        backgroundColor: isDarkMode ? colors.dark.primary.dark : colors.light.primary.light,
                        boxShadow: "-1px 1px 2px rgba(255,255,255,0.2)",
                    }}
                >
                    <li>
                        <button
                            onClick={handleEdit}
                            style={{
                                color: isDarkMode ? "white" : "black",
                            }}
                        >
                            Edit
                        </button>
                    </li>
                    <li>
                        <button
                            className="delete-button"
                            onClick={handleDelete}
                            style={{
                                color: colors.dark.secondary.main,
                                borderColor: colors.dark.secondary.main,
                                borderWidth: "1px",
                                borderStyle: "solid",
                                borderRadius: "5px",
                            }}
                        >
                            Delete
                        </button>
                    </li>
                </ul>
            </div>
            <div>
                <section>
                    <p className="pev" style={{ marginBottom: 0 }}>
                        Protein Efficiency Value
                    </p>
                    <span
                        style={{
                            fontSize: "40px",
                            fontWeight: 600,
                            color: isDarkMode ? "white" : "black",
                        }}
                    >
                        {proteinEfficiencyPercentage}
                    </span>
                </section>
                <button
                    onClick={() => setShowData(!showData)}
                    className={showData ? "show-data no-shadow" : "show-data"}
                    style={{
                        color: isDarkMode ? "white" : "black",
                        backgroundColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.dark,
                    }}
                >
                    <FontAwesomeIcon icon={showData ? faChevronUp : faChevronDown} className="icon" />
                </button>
                {showData ? (
                    <ul
                        className="food-table"
                        style={{
                            backgroundColor: isDarkMode ? colors.dark.primary.light : colors.light.primary.main,
                        }}
                    >
                        <li>
                            <span>Calories</span>
                            <span>{calories}</span>
                        </li>
                        <li>
                            <span>Carbs</span>
                            <span>{carbs}</span>
                        </li>
                        <li>
                            <span>Fats</span>
                            <span>{fats}</span>
                        </li>
                        <li>
                            <span>Proteins</span>
                            <span>{proteins}</span>
                        </li>
                    </ul>
                ) : null}
            </div>
        </div>
    );
}

Food.propTypes = {
    id: PropTypes.number,
    name: PropTypes.string,
    calories: PropTypes.number,
    carbs: PropTypes.number,
    fats: PropTypes.number,
    proteins: PropTypes.number,
    isDarkMode: PropTypes.bool,
    data: PropTypes.array,
    setData: PropTypes.func,
};
