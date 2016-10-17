package com.chatovich.stockmarket.entity;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class Broker implements Callable<Broker>{

    private String name;
    private double money;
    private StockMarket stockMarket;
    private Map <Company, Integer> brokerStocks = new HashMap<>();

    public Broker(String name, double money, StockMarket stockMarket) {
        this.name = name;
        this.money = money;
        this.stockMarket = stockMarket;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public StockMarket getStockMarket() {
        return stockMarket;
    }

    public Map<Company, Integer> getBrokerStocks() {
        return brokerStocks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setStockMarket(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
    }

    public void setBrokerStocks(Map<Company, Integer> brokerStocks) {
        this.brokerStocks = brokerStocks;
    }

    @Override
    public Broker call()  {
        Random random = new Random();
        System.out.println(name+" starts...");
        stockMarket.getCompanies().get(random.nextInt(5)).buy(random.nextInt(100),this);
        //TimeUnit.SECONDS.sleep(Const.SLEEP);
        stockMarket.getCompanies().get(random.nextInt(5)).buy(random.nextInt(100),this);
        //stockMarket.buy(stockMarket.getCompanies().get(random.nextInt(5)), random.nextInt(100),this);
        //TimeUnit.SECONDS.sleep(Const.SLEEP);

        //creating list of broker's companies to sell one of them
        List<Company> brokerCompanies = new ArrayList<>();
        for (Company company : brokerStocks.keySet()) {
            brokerCompanies.add(company);
        }
        brokerCompanies.get(0).sell(brokerStocks.get(brokerCompanies.get(0)), this);
        //stockMarket.sell(brokerCompanies.get(0),brokerStocks.get(brokerCompanies.get(0)),this);
        return this;

    }

    @Override
    public String toString (){
        String s = "";
        double assets=money;
        for (Map.Entry<Company, Integer> entry : brokerStocks.entrySet()) {
            s+=entry.getValue()+" stocks of "+entry.getKey().getName();
            assets+=entry.getKey().getStockPrice()*entry.getValue();
        }
        return name+" has "+ (brokerStocks.isEmpty()?"0 stocks":s) +" and "+String.format("%.2f",money)+" money, total assets: "+String.format("%.2f",assets);
    }


}
