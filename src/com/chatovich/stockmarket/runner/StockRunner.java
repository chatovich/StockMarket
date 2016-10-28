package com.chatovich.stockmarket.runner;

import com.chatovich.stockmarket.action.StockMarketAction;
import com.chatovich.stockmarket.entity.Broker;
import com.chatovich.stockmarket.entity.StockMarket;
import com.chatovich.stockmarket.exception.WrongDataException;
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
    static final String FILE_NAME = System.getProperty("user.dir")+"/file/companies.txt";
    public static final double BROKER_MONEY = 20000.;

    public static void main(String[] args) {

        List <Future<Broker>> futureList = new ArrayList<>();
        StockMarket stockMarket = StockMarket.getInstance();
        //fill the stockmarket with companies
        try {
            StockMarketAction.fillStockMarket(stockMarket, FILE_NAME);
        } catch (WrongDataException e) {
            LOGGER.log(Level.ERROR, e);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 40; i++) {
            futureList.add(exec.submit(new Broker("Broker " + (i + 1), BROKER_MONEY, stockMarket)));
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
