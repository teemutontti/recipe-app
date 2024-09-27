const calculatePev = (calories, proteins) => {
    const proteinsTimed = proteins * 4;
    const proteinsInCalories = proteinsTimed / calories;
    const proteinEfficiencyPercentage = proteinsInCalories * 100;

    return proteinEfficiencyPercentage.toFixed(1);
};

export default calculatePev;
