package com.techelevator.dao;

import java.util.List;
import java.util.Map;

public interface GroceryListDao {
    List<Map<String, Object>> getGroceryList(int userId);
}