package com.chatovich.stockmarket.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class Company {

    static final Logger LOGGER = LogManager.getLogger(Company.class);

    private String name;
    private double stockPrice;
    private boolean areStocksBought;
    private ArrayDeque <Broker> queue = new ArrayDeque<>();
    private ReentrantLock lock = new ReentrantLock();


    public Company(String name, double stockPrice) {
        this.name = name;
        this.stockPrice = stockPrice;
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

        queue.add(broker);

        lock.lock();
        try {
        while (queue.peek()!=null) {
                Broker currentBroker = queue.poll();
                double stocksPrice = stockPrice * stocks;
            //check whether the broker has enough money to buy this amount of stocks
                if (currentBroker.getMoney() > stocksPrice) {
                    currentBroker.setMoney(currentBroker.getMoney() - stocksPrice);
                    //check whether the broker has already bought this company' stocks
                    if (currentBroker.getBrokerStocks().containsKey(this)) {
                        int updatedStocks = currentBroker.getBrokerStocks().get(this) + stocks;
                        currentBroker.getBrokerStocks().put(this, updatedStocks);
                    } else {
                        currentBroker.getBrokerStocks().put(this, stocks);
                    }
                    System.out.println(currentBroker.getName() + " bought " + stocks + " stocks of " + name + "("+currentBroker.getName()+"), spent " + String.format("%.2f", stocksPrice));
                    areStocksBought = true;
                    double newPrice = updatePrice(areStocksBought, stocks);
                    this.setStockPrice(newPrice);
                } else {
                    System.out.println(currentBroker.getName() + " doesn't have enough money to purchase " + stocks + " stocks of " + name);
                }
        }
        } finally {
            lock.unlock();
        }

//
    }

    public void sell (int stocks, Broker broker, StockMarket stockMarket, Random random){
            if (lock.tryLock()) {
                try {
                    double stocksPrice = stockPrice * stocks;
                    broker.setMoney(broker.getMoney() + stocksPrice);
                    int stocksLeft = broker.getBrokerStocks().get(this) - stocks;
                    broker.getBrokerStocks().put(this,stocksLeft);
                    System.out.println(broker.getName() + " sold " + stocks + " stocks of " + name + ", got " + String.format("%.2f", stocksPrice));
                    areStocksBought = false;
                    this.setStockPrice(updatePrice(areStocksBought, stocks));
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(broker.getName()+" didn't sell in time, decided to sell tomorrow..............");
            }

    }

    private double updatePrice(boolean isStocksBought, int stocks){
        double delta = (double)stocks/stockPrice;
        if (isStocksBought){
            return stockPrice + delta;
        } else {
            return stockPrice - delta;
        }
    }

    @Override
    public String toString() {
        return this.name+" "+this.stockPrice;
    }
}
