BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS recipe_categories;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS ingredient_type;
DROP TABLE IF EXISTS recipe_ingredient;
DROP TABLE IF EXISTS meal_plan;
DROP TABLE IF EXISTS meals;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE recipes (
    recipe_id SERIAL PRIMARY KEY,
    recipe_name varchar(255) NOT NULL,
    description text,
    author int references users (user_id),
    date_added timestamp DEFAULT current_timestamp
);

CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name varchar(50) NOT NULL
);

CREATE TABLE recipe_categories (
    id SERIAL PRIMARY KEY,
    recipe_id int references recipes (recipe_id),
    category_id int references categories (category_id)
);

CREATE TABLE ingredients (
    ingredient_id SERIAL PRIMARY KEY,
    ingredient_name varchar(50) NOT NULL UNIQUE,
    type_id references ingredient_type (type_id)
);

CREATE TABLE ingredient_type (
    type_id SERIAL PRIMARY KEY,
    type_name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE recipe_ingredient (
    id SERIAL PRIMARY KEY,
    recipe_id int references recipes (recipe_id),
    ingredient_id int references ingredients (ingredient_id),
    quantity numeric(10, 2),
    unit varchar(50)
);

CREATE TABLE meal_plan (
    meal_plan_id SERIAL PRIMARY KEY,
    meal_plan_name varchar(200) NOT NULL,
    user_id int references users (user_id),
    creation_date timestamp DEFAULT current_timestamp,
    start_date date NOT NULL,
    end_date date NOT NULL
);

CREATE TABLE meals (
    meal_id SERIAL PRIMARY KEY,
    meal_plan_id int references meal_plan (meal_plan_id),
    recipe_id int references recipes (recipe_id),
    meal_date Date NOT NULL,
    meal_type varchar(50)
);



COMMIT TRANSACTION;
