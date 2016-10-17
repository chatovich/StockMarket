package com.chatovich.stockmarket.runner;

import com.chatovich.stockmarket.action.Const;
import com.chatovich.stockmarket.action.StockMarketAction;
import com.chatovich.stockmarket.creator.StockMarketCreator;
import com.chatovich.stockmarket.entity.Broker;
import com.chatovich.stockmarket.entity.StockMarket;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
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
        StockMarket stockMarket = new StockMarket(StockMarketCreator.creatStockMarket());
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            futureList.add(exec.submit(new Broker("Broker " + (i + 1), Const.BROKER_MONEY, stockMarket)));
        }
        exec.shutdown();
        StockMarketAction action = new StockMarketAction();
        List <Broker> brokersList = action.compareBrokers(futureList);

        LOGGER.log(Level.INFO, "Brokers after trading in assets' ascending order: ");
        brokersList.forEach(a-> LOGGER.log(Level.INFO,a));
    }
}
