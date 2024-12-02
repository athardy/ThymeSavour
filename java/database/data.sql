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

INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Tomato', (SELECT type_id FROM ingredient_type WHERE name = 'Fruit'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Onion', (SELECT type_id FROM ingredient_type WHERE name = 'Vegetable'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Potato', (SELECT type_id FROM ingredient_type WHERE name = 'Vegetable'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Spaghetti', (SELECT type_id FROM ingredient_type WHERE name = 'Grain'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Garlic', (SELECT type_id FROM ingredient_type WHERE name = 'Fruit'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Olive Oil', (SELECT type_id FROM ingredient_type WHERE name = 'Other'));
INSERT INTO ingredients (ingredient_name, type_id) VALUES ('Parmesan Cheese', (SELECT type_id FROM ingredient_type WHERE name = 'Cheese'));

COMMIT TRANSACTION;
