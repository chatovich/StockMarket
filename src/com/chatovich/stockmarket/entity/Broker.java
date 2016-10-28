package com.chatovich.stockmarket.entity;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class Broker implements Callable<Broker>{

    private String name;
    private double money;
    private static StockMarket stockMarket;
    private Map <Company, Integer> brokerStocks = new HashMap<>();
    private static Random random = new Random();

    public Broker(String name, double money, StockMarket market) {
        this.name = name;
        this.money = money;
        stockMarket = market;
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

    public void setStockMarket(StockMarket market) {
        stockMarket = market;
    }

    public void setBrokerStocks(Map<Company, Integer> brokerStocks) {
        this.brokerStocks = brokerStocks;
    }

    @Override
    public Broker call()  {

        System.out.println(name+" starts...");
        //how many times to buy
        int purchaseNum = random.nextInt(3)+1;
        //choosing random company and stocks quantity to buy
        for (int i = 0; i < purchaseNum; i++) {
            Company company = stockMarket.getCompanies().get(random.nextInt(stockMarket.getCompanies().size()));
            company.buy(random.nextInt(100)+1,this);
        }
        //how many times to sell
        int saleNum = random.nextInt(brokerStocks.size()+1);
        //choosing random company from broker companies and stocks quantity to sell
        for (int i = 0; i < saleNum; i++) {
            List <Company> brokerCompanies = new ArrayList<>();
            for (Map.Entry<Company, Integer> entry : brokerStocks.entrySet()) {
                brokerCompanies.add(entry.getKey());
            }
            Company company = brokerCompanies.get(random.nextInt(brokerStocks.size()));
            company.sell(random.nextInt(brokerStocks.get(company))+1,this,stockMarket,random);
        }
        return this;
    }

    @Override
    public String toString (){
        String s = "";
        double assets=money;
        for (Map.Entry<Company, Integer> entry : brokerStocks.entrySet()) {
            s+=entry.getValue()+" stocks of "+entry.getKey().getName()+", ("+name+")";
            assets+=entry.getKey().getStockPrice()*entry.getValue();
        }
        return name+" has "+ (brokerStocks.isEmpty()?"0 stocks":s) +String.format("%.2f",money)+" of money, total assets: " +
                String.format("%.2f",assets);
    }


}
