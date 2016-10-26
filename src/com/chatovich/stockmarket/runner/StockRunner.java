package com.chatovich.stockmarket.runner;

import com.chatovich.stockmarket.action.Const;
import com.chatovich.stockmarket.action.StockMarketAction;
import com.chatovich.stockmarket.entity.Broker;
import com.chatovich.stockmarket.entity.Company;
import com.chatovich.stockmarket.entity.StockMarket;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class StockRunner {

    static final Logger LOGGER = LogManager.getLogger(StockRunner.class);

    public static void main(String[] args) {

        List <Future<Broker>> futureList = new ArrayList<>();
        StockMarket stockMarket = StockMarket.getInstance();
        stockMarket.addCompany(new Company(Const.NAME_APPLE,Const.PRICE_APPLE));
        stockMarket.addCompany(new Company(Const.NAME_FACEBOOK, Const.PRICE_FACEBOOK));
        stockMarket.addCompany(new Company(Const.NAME_EPAM, Const.PRICE_EPAM));
        stockMarket.addCompany(new Company(Const.NAME_BMW, Const.PRICE_BMW));
        stockMarket.addCompany(new Company(Const.NAME_MCDONALDS, Const.PRICE_MCDONALDS));

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 40; i++) {
            futureList.add(exec.submit(new Broker("Broker " + (i + 1), Const.BROKER_MONEY, stockMarket)));
        }
        exec.shutdown();
        //extract brokers from <Broker> future
        StockMarketAction action = new StockMarketAction();
        List <Broker> brokerList = new ArrayList<>();
        try {
            for (Future<Broker> future : futureList) {
                brokerList.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.ERROR, e, e.getCause());
        }
        //sort brokers by their profits
        brokerList = action.compareBrokers(brokerList);

        LOGGER.log(Level.INFO, "Brokers after trading in assets' ascending order: ");
        brokerList.forEach(a-> LOGGER.log(Level.INFO,a));

    }
}
