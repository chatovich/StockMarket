package com.chatovich.stockmarket.action;

import com.chatovich.stockmarket.entity.Broker;
import com.chatovich.stockmarket.entity.Company;
import com.chatovich.stockmarket.entity.StockMarket;
import com.chatovich.stockmarket.exception.WrongDataException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;



/**
 * Created by Yultos_ on 17.10.2016
 */
public class StockMarketAction {

    static final Logger LOGGER = LogManager.getLogger(StockMarketAction.class);
    static final String DELIMETER = " ";

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

    public static void fillStockMarket (StockMarket stockMarket, String fileName) throws WrongDataException {
        //read companies' info from file (name and stock price)
        List <String> companiesList = readFromFile(fileName);
        Map <String, Integer> companies = parseList(companiesList);

        for (Map.Entry<String, Integer> entry : companies.entrySet()) {
            stockMarket.addCompany(new Company(entry.getKey(), entry.getValue()));
        }
    }

    private static List<String> readFromFile (String fileName){

        List <String> companiesList = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()){
                companiesList.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            LOGGER.fatal("File not found", e);
            throw new RuntimeException();
        } finally {
            if (scanner!=null){
                scanner.close();
            }
        }
        return companiesList;
    }

    private static Map <String, Integer> parseList (List<String> companiesList) throws WrongDataException {
        Map<String, Integer> companies = new HashMap<>();
        for (String s : companiesList) {
            String [] arr = s.split(DELIMETER);
            if (arr.length==2){
                if (arr[0]!=null&&arr[1]!=null){
                    String key = arr[0].trim();
                    Integer value = Integer.parseInt(arr[1].trim());
                    companies.put(key,value);
                } else {
                    LOGGER.log(Level.ERROR, "Wrong data in the file.");
                }
            }
            else {
                LOGGER.log(Level.ERROR, "Wrong data quantity in the file.");
            }
        }
        return companies;
    }
}
