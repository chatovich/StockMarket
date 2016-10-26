package com.chatovich.stockmarket.action;

import com.chatovich.stockmarket.entity.Broker;
import com.chatovich.stockmarket.entity.Company;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Created by Yultos_ on 17.10.2016
 */
public class StockMarketAction {

    static final Logger LOGGER = LogManager.getLogger(StockMarketAction.class);

    public List <Broker> compareBrokers (List <Broker> brokerList) {

        Collections.sort(brokerList,((o1, o2) -> Double.compare(calcAssets(o1),calcAssets(o2))));

        return brokerList;
    }

    private double calcAssets (Broker broker){
        double assets = broker.getMoney();
        for (Map.Entry<Company, Integer> entry : broker.getBrokerStocks().entrySet()) {
            assets+=entry.getKey().getStockPrice()*entry.getValue();
        }
        return assets;
    }
}
