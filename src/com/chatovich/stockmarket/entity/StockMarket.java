package com.chatovich.stockmarket.entity;

import com.chatovich.stockmarket.exception.WrongDataException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yultos_ on 16.10.2016
 */
public final class StockMarket {

    static final Logger LOGGER = LogManager.getLogger(StockMarket.class);
    private static StockMarket instance = null;
    private static ReentrantLock lock = new ReentrantLock();
    private List<Company> companies = new ArrayList<>();

    private StockMarket() {
    }

    public static StockMarket getInstance (){
        lock.lock();
        try{
            if (instance==null){
                instance = new StockMarket();
            }
        } finally {
            lock.unlock();
        }
        return instance;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void addCompany (Company newCompany) {
        String newName = newCompany.getName();
        //check whether this company already exists
        try {
            for (Company company : companies) {
                if (company.getName().equalsIgnoreCase(newName)){
                    throw new WrongDataException("Company with name "+newName+" already operates on the StockMarket.");
                }
            }
            companies.add(newCompany);
        } catch (WrongDataException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }


}
