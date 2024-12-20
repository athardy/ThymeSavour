package com.techelevator.dao;

import com.techelevator.model.Meal;
import com.techelevator.model.MealPlan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

@Component
public class JdbcMealPlanDao implements MealPlanDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMealPlanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createMealPlan(MealPlan mealPlan) {
        String sql = "INSERT INTO meal_plan (meal_plan_name, user_id, start_date, end_date) VALUES (?, ?, ?, ?)";

        try {
            System.out.println("Debug: Creating meal plan with:");
            System.out.println("Name: " + mealPlan.getMeal_plan_name());
            System.out.println("Start Date: " + mealPlan.getStart_date());
            System.out.println("End Date: " + mealPlan.getEnd_date());
            System.out.println("User ID: " + mealPlan.getUser_id());

            jdbcTemplate.update(sql,
                    mealPlan.getMeal_plan_name(),
                    mealPlan.getUser_id(),
                    mealPlan.getStart_date(),
                    mealPlan.getEnd_date() // Use correct getter
            );

            System.out.println("Debug: Meal plan created successfully!");
        } catch (Exception e) {
            System.err.println("Error creating meal plan: " + e.getMessage());
            throw e; // Re-throw exception for higher-level handling
        }
    }

    @Override
public List<MealPlan> getMealPlansByUserId(int userId) {
    String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan WHERE user_id = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> new MealPlan(
            rs.getInt("meal_plan_id"),
            rs.getString("meal_plan_name"),
            rs.getInt("user_id"),
            rs.getDate("creation_date"),
            rs.getDate("start_date"),
            rs.getDate("end_date")
    ), userId);
}

    @Override
    public List<Meal> getRecipesForMealPlan(int mealPlanId) {
        String sql = "SELECT meal_id, meal_plan_id, recipe_id, meal_date, meal_type, is_deleted FROM meals WHERE meal_plan_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Meal(
                rs.getInt("meal_id"),
                rs.getInt("meal_plan_id"),
                rs.getInt("recipe_id"),
                rs.getDate("meal_date").toLocalDate(),
                rs.getString("meal_type"),
                rs.getBoolean("is_deleted")
        ), mealPlanId);
    }

    @Override
    public void toggleMealDelete(int mealId) {
        String sql = "UPDATE meals SET is_deleted = NOT is_deleted WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealId);
    }

    @Override
    public Meal getMealById(int mealId) {
        String sql = "SELECT meal_id, meal_plan_id, recipe_id, meal_date, meal_type, is_deleted FROM meals WHERE meal_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Meal(
                rs.getInt("meal_id"),
                rs.getInt("meal_plan_id"),
                rs.getInt("recipe_id"),
                rs.getDate("meal_date").toLocalDate(),
                rs.getString("meal_type"),
                rs.getBoolean("is_deleted")
        ), mealId);
    }

    @Override
    public void addRandomRecipeToMealPlan(int mealPlanId, int recipeId) {
        String sql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, CURRENT_DATE, 'Random')";
        jdbcTemplate.update(sql, mealPlanId, recipeId);
    }

    public void updateMealPlan(MealPlan mealPlan) {
        String sql = "UPDATE meal_plan SET meal_plan_name = ?, start_date = ?, end_date = ? WHERE meal_plan_id = ?";
    
        jdbcTemplate.update(sql, 
            mealPlan.getMeal_plan_name(),
            mealPlan.getStart_date(),
            mealPlan.getEnd_date(),
            mealPlan.getMeal_plan_id()
        );
    }

    @Override
public MealPlan getMealPlanById(int mealPlanId) {
    String sql = "SELECT meal_plan_id, meal_plan_name, user_id, creation_date, start_date, end_date FROM meal_plan WHERE meal_plan_id = ?";
    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new MealPlan(
        rs.getInt("meal_plan_id"),
        rs.getString("meal_plan_name"),
        rs.getInt("user_id"),
        rs.getDate("creation_date"),
        rs.getDate("start_date"),
        rs.getDate("end_date")
    ), mealPlanId);
}
    @Override
    public void addMealToPlan(Meal meal) {
        String sql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, meal.getMeal_plan_id(), meal.getRecipe_id(), meal.getMeal_date(), meal.getMeal_type());
    }
    public List<Map<String, Object>> getMealsWithRecipes(int mealPlanId) {
        String sql = "SELECT m.meal_id, m.meal_date, m.meal_type, r.recipe_id, r.recipe_name, r.description " +
                "FROM meals m " +
                "JOIN recipes r ON m.recipe_id = r.recipe_id " +
                "WHERE m.meal_plan_id = ? AND m.is_deleted = FALSE";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> mealWithRecipe = new HashMap<>();
            mealWithRecipe.put("meal_id", rs.getInt("meal_id"));
            mealWithRecipe.put("meal_date", rs.getDate("meal_date").toLocalDate());
            mealWithRecipe.put("meal_type", rs.getString("meal_type"));
            mealWithRecipe.put("recipe_id", rs.getInt("recipe_id"));
            mealWithRecipe.put("recipe_name", rs.getString("recipe_name"));
            mealWithRecipe.put("description", rs.getString("description"));
            return mealWithRecipe;
        }, mealPlanId);
    }
    public void generateMealsForPlan(int mealPlanId) {
        String getRecipesSql = "SELECT recipe_id FROM recipes WHERE is_deleted = FALSE";
        List<Integer> recipeIds = jdbcTemplate.queryForList(getRecipesSql, Integer.class);

        if (recipeIds.isEmpty()) {
            throw new RuntimeException("No recipes available to generate meals.");
        }

        String getMealPlanSql = "SELECT start_date, end_date FROM meal_plan WHERE meal_plan_id = ?";
        Map<String, Object> mealPlanDates = jdbcTemplate.queryForMap(getMealPlanSql, mealPlanId);

        LocalDate startDate = ((Date) mealPlanDates.get("start_date")).toLocalDate();
        LocalDate endDate = ((Date) mealPlanDates.get("end_date")).toLocalDate();

        String insertMealSql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, ?, ?)";

        Random random = new Random();
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snack"};

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (String mealType : mealTypes) {
                int randomRecipeId = recipeIds.get(random.nextInt(recipeIds.size()));
                jdbcTemplate.update(insertMealSql, mealPlanId, randomRecipeId, date, mealType);
            }
        }
    }

    @Override
    public void deleteMealPlan(int mealPlanId) {
        String sql = "DELETE FROM meal_plan WHERE meal_plan_id = ?";
        jdbcTemplate.update(sql, mealPlanId);
    }

    public void fillEmptyMeals(int mealPlanId) {
        String fetchRecipesSql = "SELECT recipe_id FROM recipes WHERE is_deleted = FALSE";
        List<Integer> recipeIds = jdbcTemplate.queryForList(fetchRecipesSql, Integer.class);

        if (recipeIds.isEmpty()) {
            throw new RuntimeException("No recipes available to fill meals.");
        }

        String fetchMealPlanDatesSql = "SELECT start_date, end_date FROM meal_plan WHERE meal_plan_id = ?";
        Map<String, Object> mealPlanDates = jdbcTemplate.queryForMap(fetchMealPlanDatesSql, mealPlanId);

        LocalDate startDate = ((Date) mealPlanDates.get("start_date")).toLocalDate();
        LocalDate endDate = ((Date) mealPlanDates.get("end_date")).toLocalDate();

        String fetchExistingMealsSql = "SELECT meal_date, meal_type FROM meals WHERE meal_plan_id = ?";
        List<Map<String, Object>> existingMeals = jdbcTemplate.queryForList(fetchExistingMealsSql, mealPlanId);

        Set<String> occupiedSlots = new HashSet<>();
        for (Map<String, Object> meal : existingMeals) {
            occupiedSlots.add(meal.get("meal_date") + ":" + meal.get("meal_type"));
        }

        String insertMealSql = "INSERT INTO meals (meal_plan_id, recipe_id, meal_date, meal_type) VALUES (?, ?, ?, ?)";
        Random random = new Random();
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snack 1", "Snack 2"};

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (String mealType : mealTypes) {
                String slotKey = date + ":" + mealType;
                if (!occupiedSlots.contains(slotKey)) {
                    int randomRecipeId = recipeIds.get(random.nextInt(recipeIds.size()));
                    jdbcTemplate.update(insertMealSql, mealPlanId, randomRecipeId, Date.valueOf(date), mealType);
                }
            }
        }
    }
    @Override
    public List<Map<String, Object>> getGroceryListByMealPlan(int mealPlanId) {
        String sql =
                "SELECT " +
                        "    i.ingredient_id, " +
                        "    i.ingredient_name, " +
                        "    ri.unit, " +
                        "    SUM(ri.quantity) AS total_quantity " +
                        "FROM " +
                        "    meal_plan mp " +
                        "JOIN " +
                        "    meals m " +
                        "    ON mp.meal_plan_id = m.meal_plan_id " +
                        "JOIN " +
                        "    recipe_ingredient ri " +
                        "    ON ri.recipe_id = m.recipe_id " +
                        "JOIN " +
                        "    ingredients i " +
                        "    ON ri.ingredient_id = i.ingredient_id " +
                        "WHERE " +
                        "    mp.meal_plan_id = ? " +
                        "GROUP BY " +
                        "    i.ingredient_id, " +
                        "    i.ingredient_name, " +
                        "    ri.unit";

        return jdbcTemplate.queryForList(sql, mealPlanId);
    }

}