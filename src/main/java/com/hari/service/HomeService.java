package com.hari.service;

import com.hari.model.Home;
import com.hari.model.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageDate(List<HomeCategory> allCategories);
}
