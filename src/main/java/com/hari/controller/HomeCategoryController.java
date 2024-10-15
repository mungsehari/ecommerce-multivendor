package com.hari.controller;

import com.hari.model.Home;
import com.hari.model.HomeCategory;
import com.hari.service.HomeCategoryService;
import com.hari.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;


    @PostMapping("/categories")
    public ResponseEntity<Home> createHomeCategory(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories=homeCategoryService.createCategories(homeCategories);

        Home home=homeService.createHomePageDate(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategories() {
        List<HomeCategory> categories=homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @PathVariable Long id,
            @RequestBody HomeCategory homeCategory
    ) throws Exception {
        HomeCategory updatedCategory=homeCategoryService.updateHomeCategory(homeCategory,id);

        return ResponseEntity.ok(updatedCategory);
    }

}
