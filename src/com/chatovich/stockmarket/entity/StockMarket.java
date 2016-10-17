package com.chatovich.stockmarket.entity;

import java.util.List;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class StockMarket {

    private List<Company> companies;

    public StockMarket(List<Company> companies) {
        this.companies = companies;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }


}
