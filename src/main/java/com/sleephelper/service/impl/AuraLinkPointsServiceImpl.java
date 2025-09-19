package com.sleephelper.service.impl;

import com.sleephelper.bean.BurnResult;
import com.sleephelper.bean.ForwarderParams;
import com.sleephelper.bean.MetaBurnRequest;
import com.sleephelper.service.AuraLinkPointsService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
@Service
public class AuraLinkPointsServiceImpl implements AuraLinkPointsService {
    @Override
    public BigInteger getBalance(String userAddress) throws Exception {
        return null;
    }

    @Override
    public String mintPoints(String toAddress, BigInteger amount) throws Exception {
        return "";
    }

    @Override
    public String getName() throws Exception {
        return "";
    }

    @Override
    public String getSymbol() throws Exception {
        return "";
    }

    @Override
    public BigInteger getTotalSupply() throws Exception {
        return null;
    }

    @Override
    public boolean hasMinterRole(String account) throws Exception {
        return false;
    }

    @Override
    public BurnResult relayBurnWithMeta(MetaBurnRequest request) {
        return null;
    }

    @Override
    public String burnFromDirect(String from, BigInteger amount) throws Exception {
        return "";
    }

    @Override
    public ForwarderParams getBurnForwarderParams(String userAddress, BigInteger amount) throws Exception {
        return null;
    }

    @Override
    public BigInteger getPendingTransactionCount() throws Exception {
        return null;
    }

    @Override
    public void printAccountInfo() throws Exception {

    }
}
