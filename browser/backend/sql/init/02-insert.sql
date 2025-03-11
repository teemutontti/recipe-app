USE app;

-- Insert users
INSERT INTO users (id, email, password, role) VALUES
    -- DISCLAIMER: Replace these values with the encrypted and hashed versions of the credentials
    (1, 'ENCRYPTED_EMAIL_HERE', 'PASSWORD_HASH_HERE', 'ROLE_ADMIN');

-- Insert foods
INSERT INTO foods (name, calories, carbs, fat, protein, barcode, serving_size, created_by, edited_by) VALUES
    ('Apple', 52.00, 13.81, 0.17, 0.26, '1234567890123', 100, 1, 1),
    ('Banana', 89.00, 22.84, 0.33, 1.09, '2345678901234', 100, 1, 1),
    ('Chicken Breast', 165.00, 0.00, 3.60, 31.00, NULL, 100, 1, 1),
    ('Salmon', 208.00, 0.00, 13.42, 22.00, '3456789012345', 100, 1, 1),
    ('Broccoli', 55.00, 11.12, 0.60, 3.71, '4567890123456', 100, 1, 1),
    ('Carrot', 41.00, 9.58, 0.24, 0.93, '5678901234567', 100, 1, 1),
    ('Egg', 155.00, 1.12, 10.61, 13.00, '6789012345678', 100, 1, 1),
    ('Rice', 130.00, 28.17, 0.28, 2.69, '7890123456789', 100, 1, 1),
    ('Avocado', 160.00, 8.53, 14.66, 2.00, '8901234567890', 100, 1, 1),
    ('Spinach', 23.00, 3.63, 0.39, 2.86, '9012345678901', 100, 1, 1),
    ('Lentils', 116.00, 20.13, 0.38, 9.02, '0123456789012', 100, 1, 1),
    ('Peanut Butter', 588.00, 20.00, 50.00, 25.00, '1234567890124', 20, 1, 1),
    ('Tomato', 18.00, 3.89, 0.20, 0.91, '2345678901235', 100, 1, 1),
    ('Potato', 77.00, 17.49, 0.10, 2.02, '3456789012346', 100, 1, 1),
    ('Cucumber', 16.00, 3.63, 0.10, 0.65, '4567890123457', 100, 1, 1),
    ('Olive Oil', 884.00, 0.00, 100.00, 0.00, '5678901234568', 10, 1, 1),
    ('Cottage Cheese', 98.00, 3.38, 4.30, 11.10, '6789012345679', 100, 1, 1),
    ('Tofu', 144.00, 1.90, 8.00, 15.00, '7890123456790', 100, 1, 1);

-- Insert logs
INSERT INTO logs (date, meal, amount, time, user_id, food_id) VALUES
    (CURRENT_DATE, 'Breakfast', 150.00, '08:00:00', 1, 1),
    (CURRENT_DATE, 'Lunch', 200.00, '12:30:00', 1, 2),
    (CURRENT_DATE, 'Dinner', 250.00, '18:00:00', 1, 3),
    (CURRENT_DATE, 'Breakfast', 100.00, '08:00:00', 1, 4),
    (CURRENT_DATE, 'Lunch', 180.00, '12:30:00', 1, 5),
    (CURRENT_DATE, 'Dinner', 230.00, '18:00:00', 1, 6),
    (CURRENT_DATE, 'Breakfast', 160.00, '08:00:00', 1, 7),
    (CURRENT_DATE, 'Lunch', 220.00, '12:30:00', 1, 8),
    (CURRENT_DATE, 'Dinner', 260.00, '18:00:00', 1, 9),
    (CURRENT_DATE, 'Breakfast', 120.00, '08:00:00', 1, 10),
    (CURRENT_DATE, 'Lunch', 210.00, '12:30:00', 1, 11),
    (CURRENT_DATE, 'Dinner', 240.00, '18:00:00', 1, 12),
    (CURRENT_DATE, 'Breakfast', 140.00, '08:00:00', 1, 13),
    (CURRENT_DATE, 'Lunch', 190.00, '12:30:00', 1, 14),
    (CURRENT_DATE, 'Dinner', 250.00, '18:00:00', 1, 15),
    (CURRENT_DATE, 'Breakfast', 155.00, '08:00:00', 1, 16),
    (CURRENT_DATE, 'Lunch', 215.00, '12:30:00', 1, 17),
    (CURRENT_DATE, 'Dinner', 225.00, '18:00:00', 1, 18);