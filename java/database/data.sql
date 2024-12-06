BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');

INSERT INTO categories (category_name) VALUES ('Breakfast');
INSERT INTO categories (category_name) VALUES ('Lunch');
INSERT INTO categories (category_name) VALUES ('Dinner');
INSERT INTO categories (category_name) VALUES ('Snacks');
INSERT INTO categories (category_name) VALUES ('Desert');
INSERT INTO categories (category_name) VALUES ('Vegetarian');
INSERT INTO categories (category_name) VALUES ('Vegan');
INSERT INTO categories (category_name) VALUES ('Low-Carb');
INSERT INTO categories (category_name) VALUES ('Paleo');
INSERT INTO categories (category_name) VALUES ('Keto');

INSERT INTO ingredient_type (type_name) VALUES ('Herb');
INSERT INTO ingredient_type (type_name) VALUES ('Meat');
INSERT INTO ingredient_type (type_name) VALUES ('Vegetable');
INSERT INTO ingredient_type (type_name) VALUES ('Fruit');
INSERT INTO ingredient_type (type_name) VALUES ('Dairy');
INSERT INTO ingredient_type (type_name) VALUES ('Cheese');
INSERT INTO ingredient_type (type_name) VALUES ('Grain');
INSERT INTO ingredient_type (type_name) VALUES ('Other');

INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Tomato', (SELECT type_id FROM ingredient_type WHERE type_name = 'Fruit'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Onion', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Potato', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Spaghetti', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Garlic', (SELECT type_id FROM ingredient_type WHERE type_name = 'Vegetable'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Olive Oil', (SELECT type_id FROM ingredient_type WHERE type_name = 'Other'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Parmesan Cheese', (SELECT type_id FROM ingredient_type WHERE type_name = 'Cheese'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Lasagna', (SELECT type_id FROM ingredient_type WHERE type_name = 'Grain'));

INSERT INTO recipes (recipe_name, description, instructions, author) VALUES ('Spaghetti with Red Sauce', 'Classic Italian dish', 'Cook spaghetti and add red sauce', '1' );

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('1', '1', '1', 'Can');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('1', '4', '1', 'lb');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('1', '5', '5', 'Cloves');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('1', '6', '2', 'TBSP');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('1', '7', '1', 'Cup');

INSERT INTO recipes (recipe_name, description, instructions, author) VALUES ('Lasagna', 'Just like how mom used to make... mmmm hmmm.', 'Go to Olive Garden', '1' );

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('2', '1', '2', 'Can');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('2', '8', '1', 'lb');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('2', '5', '5', 'Cloves');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('2', '6', '3', 'TBSP');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES ('2', '7', '2', 'Cup');

INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES ('Italian-fest Week', '1', '2024-12-06', '2024-12-08');

INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES ('1', '1', '2024-12-06', 'Dinner');
INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES ('1', '2', '2024-12-07', 'Dinner');







COMMIT TRANSACTION;
