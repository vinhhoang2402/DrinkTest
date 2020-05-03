package com.example.drinkapp.models;

public class Order {
    private long OrderId;
    private int OrderStatus;
    private float OrderPrice;
    private String OrderDetail,OderComment,OrderAddress,OrderDate,UserPhone;

    public Order(){

    }

    public long getOderId() {
        return OrderId;
    }

    public void setOderId(long oderId) {
        OrderId = oderId;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public float getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }

    public String getOderComment() {
        return OderComment;
    }

    public void setOderComment(String oderComment) {
        OderComment = oderComment;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
