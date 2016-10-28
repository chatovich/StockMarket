package com.chatovich.stockmarket.entity;

import com.chatovich.stockmarket.exception.WrongDataException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yultos_ on 16.10.2016
 */
public final class StockMarket {

    private static StockMarket instance;
    private static ReentrantLock lock = new ReentrantLock();
    private List<Company> companies = new ArrayList<>();
    private static AtomicBoolean isNull = new AtomicBoolean(true);

    private StockMarket() {
    }

    public static StockMarket getInstance (){

        if (isNull.get()) {
            lock.lock();
            try{
                if (instance==null){
                    instance = new StockMarket();
                    isNull.set(false);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void addCompany (Company newCompany) throws WrongDataException {
        String newName = newCompany.getName();
        //check whether this company already exists
            for (Company company : companies) {
                if (company.getName().equalsIgnoreCase(newName)){
                    throw new WrongDataException("Company with name "+newName+" already operates on the StockMarket.");
                }
            }
            companies.add(newCompany);
    }


}
