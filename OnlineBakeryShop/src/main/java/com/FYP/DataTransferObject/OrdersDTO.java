package com.FYP.DataTransferObject;

import lombok.Data;

@Data
public class OrdersDTO {
	private int orderId;
	private String name;
	private String address;
	private String cNumber;
	private String email;
	private String orderDate;
	private String deliveryDate;
	private int productId;
	private String image;
	private String pName;
	private long quantity;
	private long totalAmount;
	private String status;

}
