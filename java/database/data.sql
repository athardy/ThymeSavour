BEGIN TRANSACTION;

-- -- Insert users
-- INSERT INTO users (username, password_hash, role) VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER');
-- INSERT INTO users (username, password_hash, role) VALUES ('admin', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_ADMIN');

-- Insert categories
INSERT INTO categories (category_name) VALUES 
('Breakfast'), 
('Lunch'), 
('Dinner'), 
('Snacks'), 
('Dessert'), 
('Vegetarian'), 
('Vegan'), 
('Low-Carb'), 
('Paleo'), 
('Keto'), 
('Appetizers'), 
('Main Course'), 
('Side Dish'), 
('Salad'), 
('Soup'), 
('Seafood'), 
('Pasta'), 
('Casserole'), 
('Smoothie'), 
('BBQ'), 
('Sandwich'), 
('Gluten-Free'), 
('Dairy-Free');

-- Insert ingredient types
INSERT INTO ingredient_type (type_name) VALUES 
('Protein'), 
('Vegetable'), 
('Fruit'), 
('Dairy'), 
('Grain'), 
('Cheese'), 
('Condiment'), 
('Oil'), 
('Spice'), 
('Sweetener'), 
('Nut'), 
('Legume'), 
('Dressing'), 
('Seafood'), 
('Beverage');

-- Insert ingredients
INSERT INTO ingredients (ingredient_name, type_id) VALUES 
('Chicken Breast', (SELECT type_id FROM ingredient_type WHERE type_name = 'Protein')),
('Tomato', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Spinach', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Quinoa', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Milk', (SELECT type_id FROM ingredient_type WHERE type_name = 'Dairy')),
('Ranch Dressing', (SELECT type_id FROM ingredient_type WHERE type_name = 'Dressing')),
('Shrimp', (SELECT type_id FROM ingredient_type WHERE type_name = 'Seafood')),
('Black Pepper', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Honey', (SELECT type_id FROM ingredient_type WHERE type_name = 'Sweetener')),
('Vegetable Oil', (SELECT type_id FROM ingredient_type WHERE type_name = 'Oil')),
('Almonds', (SELECT type_id FROM ingredient_type WHERE type_name = 'Nut')),
('Peanuts', (SELECT type_id FROM ingredient_type WHERE type_name = 'Legume')),
('Bananas', (SELECT type_id FROM ingredient_type WHERE type_name = 'Fruit')),
('Orange Juice', (SELECT type_id FROM ingredient_type WHERE type_name = 'Beverage')),
('Garlic', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable'));

-- Insert recipes
INSERT INTO recipes (recipe_name, description, instructions, author) VALUES 
('Grilled Chicken Salad', 'A healthy grilled chicken salad with ranch dressing.', 'Grill chicken, mix with veggies and ranch.', (SELECT user_id FROM users WHERE username = 'user')),
('Shrimp Alfredo', 'Creamy shrimp pasta dish.', 'Cook pasta, make Alfredo sauce, and add shrimp.', (SELECT user_id FROM users WHERE username = 'admin')),
('Quinoa Bowl', 'Healthy grain bowl with vegetables.', 'Cook quinoa, stir-fry veggies, and mix together.', (SELECT user_id FROM users WHERE username = 'user')),
('Banana Smoothie', 'Refreshing banana-based drink.', 'Blend bananas, milk, and honey.', (SELECT user_id FROM users WHERE username = 'user')),
('Lemon Garlic Shrimp', 'Zesty shrimp dish.', 'Saute shrimp with lemon and garlic.', (SELECT user_id FROM users WHERE username = 'admin')),
('Vegetarian Lasagna', 'Classic lasagna without meat.', 'Layer pasta, veggies, and cheese.', (SELECT user_id FROM users WHERE username = 'admin')),
('Stuffed Peppers', 'Peppers stuffed with rice and meat.', 'Bake stuffed peppers in an oven.', (SELECT user_id FROM users WHERE username = 'user'));

-- Insert recipe_ingredients
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES 
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Grilled Chicken Salad'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Chicken Breast'), 200, 'g'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Grilled Chicken Salad'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Tomato'), 100, 'g'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Shrimp Alfredo'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Shrimp'), 300, 'g'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Shrimp Alfredo'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Garlic'), 5, 'cloves'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Quinoa Bowl'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Quinoa'), 150, 'g'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Quinoa Bowl'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Spinach'), 100, 'g'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Banana Smoothie'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Bananas'), 2, 'pcs'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Banana Smoothie'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Milk'), 200, 'ml'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Lemon Garlic Shrimp'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Shrimp'), 250, 'g'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Lemon Garlic Shrimp'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Garlic'), 3, 'cloves'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Stuffed Peppers'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Tomato'), 150, 'g');

-- Insert meal plans
INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES 
('Healthy Week', (SELECT user_id FROM users WHERE username = 'user'), '2024-12-10', '2024-12-16'),
('Seafood Specials', (SELECT user_id FROM users WHERE username = 'admin'), '2024-12-10', '2024-12-16');

-- Insert meals
INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES 
((SELECT meal_plan_id FROM meal_plan WHERE meal_plan_name = 'Healthy Week'), (SELECT recipe_id FROM recipes WHERE recipe_name = 'Grilled Chicken Salad'), '2024-12-10', 'Dinner'),
((SELECT meal_plan_id FROM meal_plan WHERE meal_plan_name = 'Healthy Week'), (SELECT recipe_id FROM recipes WHERE recipe_name = 'Banana Smoothie'), '2024-12-11', 'Breakfast'),
((SELECT meal_plan_id FROM meal_plan WHERE meal_plan_name = 'Seafood Specials'), (SELECT recipe_id FROM recipes WHERE recipe_name = 'Shrimp Alfredo'), '2024-12-12', 'Dinner'),
((SELECT meal_plan_id FROM meal_plan WHERE meal_plan_name = 'Seafood Specials'), (SELECT recipe_id FROM recipes WHERE recipe_name = 'Lemon Garlic Shrimp'), '2024-12-13', 'Dinner');

COMMIT TRANSACTION;