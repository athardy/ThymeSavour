package com.techelevator.controller;


import com.techelevator.dao.GroceryListDao;
import com.techelevator.dao.UserDao;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grocery-list")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class GroceryListController {

    private final GroceryListDao groceryListDao;
    private final UserDao userDao;


    public GroceryListController(GroceryListDao groceryListDao, UserDao userDao) {
        this.groceryListDao = groceryListDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getGroceryList(Principal principal){
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Map<String, Object>> groceryList = groceryListDao.getGroceryList(userId);
        return ResponseEntity.ok(groceryList);
    }






}
