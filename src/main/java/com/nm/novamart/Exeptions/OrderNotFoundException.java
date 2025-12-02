package com.nm.novamart.Exeptions;

public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException(String OrderId) {

        super("Order Not Found : " + OrderId);
    }
}
