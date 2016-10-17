package com.chatovich.stockmarket.creator;

import com.chatovich.stockmarket.action.Const;
import com.chatovich.stockmarket.entity.Company;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yultos_ on 16.10.2016
 */
public class StockMarketCreator {

    public static List<Company> creatStockMarket (){

        List <Company> stockMarket = new ArrayList<>();

        Company apple = new Company(Const.NAME_APPLE,Const.PRICE_APPLE);
        Company facebook = new Company(Const.NAME_FACEBOOK, Const.PRICE_FACEBOOK);
        Company epam = new Company(Const.NAME_EPAM, Const.PRICE_EPAM);
        Company bmw = new Company(Const.NAME_BMW, Const.PRICE_BMW);
        Company mcdonalds = new Company(Const.NAME_MCDONALDS, Const.PRICE_MCDONALDS);

        stockMarket.add(apple);
        stockMarket.add(facebook);
        stockMarket.add(epam);
        stockMarket.add(bmw);
        stockMarket.add(mcdonalds);

        return stockMarket;

    }
}
