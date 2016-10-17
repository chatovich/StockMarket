package com.chatovich.stockmarket.entity;

import com.chatovich.stockmarket.action.Const;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class Company {

    static final Logger LOGGER = LogManager.getLogger(Company.class);

    private String name;
    private double stockPrice;
    boolean isStocksBought;
    int stockQuantity;
    ReentrantLock lock = new ReentrantLock();


    public Company(String name, double stockPrice) {
        this.name = name;
        this.stockPrice = stockPrice;
        this.stockQuantity = Const.STOCKS_FROM_START;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void buy (int stocks, Broker broker){
        try {
            if (lock.tryLock(1, TimeUnit.MILLISECONDS)) {
                double stocksPrice = stockPrice * stocks;
                if (broker.getMoney() > stocksPrice) {
                    broker.setMoney(broker.getMoney() - stocksPrice);
                    //check whether the broker has already bought this company' stocks
                    if (broker.getBrokerStocks().containsKey(this)) {
                        int updatedStocks = broker.getBrokerStocks().get(this) + stocks;
                        broker.getBrokerStocks().put(this, updatedStocks);
                    } else {
                        broker.getBrokerStocks().put(this, stocks);
                    }
                    System.out.println(broker.getName() + " bought " + stocks + " stocks of " + name + ", spent " + stocksPrice);
                    isStocksBought = true;
                    double newPrice = updatePrice(isStocksBought, stocks);
                    this.setStockPrice(newPrice);
                } else {
                    System.out.println(broker.getName() + " doesn't have enough money to purchase " + stocks + " stocks of " + name);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        } finally {
            lock.unlock();
        }
    }

    public void sell (int stocks, Broker broker){
        try {
            if (lock.tryLock(1,TimeUnit.MILLISECONDS)) {
                double stocksPrice = stockPrice * stocks;
                broker.setMoney(broker.getMoney() + stocksPrice);
                broker.getBrokerStocks().remove(this);
                System.out.println(broker.getName() + " sold " + stocks + " stocks of " + name + ", got " + stocksPrice);
                isStocksBought = false;
                this.setStockPrice(updatePrice(isStocksBought, stocks));
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        } finally {
            lock.unlock();
        }
    }

    private double updatePrice(boolean isStocksBought, int stocks){
        double delta = (double)stocks/stockQuantity*stockPrice;
        if (isStocksBought){
            return stockPrice + delta;
        } else {
            return stockPrice - delta;
        }
    }
}
