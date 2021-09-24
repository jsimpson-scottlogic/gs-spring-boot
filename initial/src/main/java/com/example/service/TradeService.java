package com.example.service;

import com.example.repository.TradeRepository;
import com.example.repository.UserRepository;
import com.example.springboot.Trade;
import com.example.springboot.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    public TradeService(TradeRepository tradeRepository){
        this.tradeRepository=tradeRepository;
    }

    public void add(Trade trade){
        tradeRepository.save(trade);
    }

}
