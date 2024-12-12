BEGIN TRANSACTION;

-- Insert users
INSERT INTO users (username, password_hash, role) VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER');
INSERT INTO users (username, password_hash, role) VALUES ('admin', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_ADMIN');
INSERT INTO users (username, password_hash, role) VALUES ('frank', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER');

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
('Garlic', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Salt', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Cayenne Pepper', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Cumin', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Paprika', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Nutmeg', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Garlic Salt', (SELECT type_id FROM ingredient_type WHERE type_name = 'Spice')),
('Canned Tuna', (SELECT type_id FROM ingredient_type WHERE type_name = 'Seafood')),
('Pepperoni', (SELECT type_id FROM ingredient_type WHERE type_name = 'Protein')),
('Pork Chop', (SELECT type_id FROM ingredient_type WHERE type_name = 'Protein')),
('Filet Mignon', (SELECT type_id FROM ingredient_type WHERE type_name = 'Protein')),
('Black Beans', (SELECT type_id FROM ingredient_type WHERE type_name = 'Protein')),
('Tofu', (SELECT type_id FROM ingredient_type WHERE type_name = 'Protein')),
('Celery', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Cauliflower', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Cabbage', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Eggplant', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Asparagus', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Onion', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable')),
('Rye Bread', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Penne', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Macaroni', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Pita Bread', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Flour', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Rice', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Oats', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Baguette', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain')),
('Yogurt', (SELECT type_id FROM ingredient_type WHERE type_name = 'Dairy')),
('Cream Cheese', (SELECT type_id FROM ingredient_type WHERE type_name = 'Dairy')),
('Sour Cream', (SELECT type_id FROM ingredient_type WHERE type_name = 'Dairy')),
('Heavy Cream', (SELECT type_id FROM ingredient_type WHERE type_name = 'Dairy')),
('Cheddar Cheese', (SELECT type_id FROM ingredient_type WHERE type_name = 'Cheese')),
('Swiss Cheese', (SELECT type_id FROM ingredient_type WHERE type_name = 'Cheese')),
('Parmesan Cheese', (SELECT type_id FROM ingredient_type WHERE type_name = 'Cheese')),
('Brie', (SELECT type_id FROM ingredient_type WHERE type_name = 'Cheese')),
('Paneer Cheese', (SELECT type_id FROM ingredient_type WHERE type_name = 'Cheese')),
('Ketchup', (SELECT type_id FROM ingredient_type WHERE type_name = 'Condiment')),
('Mayonnaise', (SELECT type_id FROM ingredient_type WHERE type_name = 'Condiment')),
('Mustard', (SELECT type_id FROM ingredient_type WHERE type_name = 'Condiment')),
('Butter', (SELECT type_id FROM ingredient_type WHERE type_name = 'Oil')),
('Maple Syrup', (SELECT type_id FROM ingredient_type WHERE type_name = 'Condiment')),
('Sweet Relish', (SELECT type_id FROM ingredient_type WHERE type_name = 'Condiment'));

-- Insert recipes
INSERT INTO recipes (recipe_name, description, instructions, author) VALUES 
('Grilled Chicken Salad', 'A healthy grilled chicken salad with ranch dressing.', 'Grill chicken, mix with veggies and ranch.', (SELECT user_id FROM users WHERE username = 'frank')),
('Shrimp Alfredo', 'Creamy shrimp pasta dish.', 'Cook pasta, make Alfredo sauce, and add shrimp.', (SELECT user_id FROM users WHERE username = 'admin')),
('Quinoa Bowl', 'Healthy grain bowl with vegetables.', 'Cook quinoa, stir-fry veggies, and mix together.', (SELECT user_id FROM users WHERE username = 'frank')),
('Banana Smoothie', 'Refreshing banana-based drink.', 'Blend bananas, milk, and honey.', (SELECT user_id FROM users WHERE username = 'frank')),
('Lemon Garlic Shrimp', 'Zesty shrimp dish.', 'Saute shrimp with lemon and garlic.', (SELECT user_id FROM users WHERE username = 'admin')),
('Vegetarian Lasagna', 'Classic lasagna without meat.', 'Layer pasta, veggies, and cheese.', (SELECT user_id FROM users WHERE username = 'admin')),
('Stuffed Peppers', 'Peppers stuffed with rice and meat.', 'Bake stuffed peppers in an oven.', (SELECT user_id FROM users WHERE username = 'frank')),
('Mac & Cheese', 'Thanksgiving Classic', 'Cook pasta, drain pasta, mix cheese milk and pasta in dish, top with breadcrumbs and more cheese, bake for 30 min.', (SELECT user_id FROM users WHERE username = 'frank')),
('Saag Paneer', 'Traditional northern Indian dish.', 'Saute veggies, add spices, add spinach and broth, cook down to desired consistency.', (SELECT user_id FROM users WHERE username = 'frank')),
('Pancakes', 'Fluffy pancakes every time.', 'Mix ingredients, oil in pan, pour contents into desired size, flip when it starts to bubble around edges.', (SELECT user_id FROM users WHERE username = 'frank'));

-- Insert recipe ingredients
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES 
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Pancakes'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Milk'), 1, 'cup'),
((SELECT recipe_id FROM recipes WHERE recipe_name = 'Pancakes'), (SELECT ingredient_id FROM ingredients WHERE ingredient_name = 'Flour'), 2, 'cups');

-- Insert meal plans
INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES 
('Healthy Week', (SELECT user_id FROM users WHERE username = 'frank'), '2024-12-10', '2024-12-16'),
('Seafood Specials', (SELECT user_id FROM users WHERE username = 'admin'), '2024-12-10', '2024-12-16');

-- Insert meals
INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES 
((SELECT meal_plan_id FROM meal_plan WHERE meal_plan_name = 'Healthy Week'), (SELECT recipe_id FROM recipes WHERE recipe_name = 'Grilled Chicken Salad'), '2024-12-10', 'Dinner');

COMMIT TRANSACTION;