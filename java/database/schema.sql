BEGIN TRANSACTION;

-- Drop child tables first to avoid foreign key constraints
DROP TABLE IF EXISTS recipe_ingredient;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS meal_plan;
DROP TABLE IF EXISTS recipe_categories;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS ingredient_type;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(200) NOT NULL,
  role VARCHAR(50) NOT NULL
);

-- Create ingredient_type table
CREATE TABLE ingredient_type (
  type_id SERIAL PRIMARY KEY,
  type_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create categories table
CREATE TABLE categories (
  category_id SERIAL PRIMARY KEY,
  category_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create recipes table
CREATE TABLE recipes (
  recipe_id SERIAL PRIMARY KEY,
  recipe_name VARCHAR(255) NOT NULL,
  description TEXT,
  instructions TEXT NOT NULL,
  author INT REFERENCES users (user_id) ON DELETE CASCADE,
  date_added TIMESTAMP DEFAULT current_timestamp,
  is_deleted BOOLEAN DEFAULT FALSE -- Soft delete flag
);

-- Create ingredients table
CREATE TABLE ingredients (
  ingredient_id SERIAL PRIMARY KEY,
  ingredient_name VARCHAR(255) NOT NULL UNIQUE,
  type_id INT REFERENCES ingredient_type (type_id) ON DELETE SET NULL,
  is_deleted BOOLEAN DEFAULT FALSE -- Soft delete flag
);

-- Create recipe_categories table
CREATE TABLE recipe_categories (
  id SERIAL PRIMARY KEY,
  recipe_id INT REFERENCES recipes (recipe_id) ON DELETE CASCADE,
  category_id INT REFERENCES categories (category_id) ON DELETE CASCADE
);

-- Create recipe_ingredient table
CREATE TABLE recipe_ingredient (
  id SERIAL PRIMARY KEY,
  recipe_id INT REFERENCES recipes (recipe_id) ON DELETE CASCADE,
  ingredient_id INT REFERENCES ingredients (ingredient_id) ON DELETE CASCADE,
  quantity NUMERIC(10, 2),
  unit VARCHAR(50)
);

-- Create meal_plan table
CREATE TABLE meal_plan (
  meal_plan_id SERIAL PRIMARY KEY,
  meal_plan_name VARCHAR(200) NOT NULL,
  user_id INT REFERENCES users (user_id) ON DELETE CASCADE,
  creation_date TIMESTAMP DEFAULT current_timestamp,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL
);

-- Create meals table
CREATE TABLE meals (
  meal_id SERIAL PRIMARY KEY,
  meal_plan_id INT REFERENCES meal_plan (meal_plan_id) ON DELETE CASCADE,
  recipe_id INT REFERENCES recipes (recipe_id) ON DELETE CASCADE,
  meal_date DATE NOT NULL,
  meal_type VARCHAR(50),
  is_deleted BOOLEAN DEFAULT FALSE -- Soft delete flag
);

COMMIT TRANSACTION;