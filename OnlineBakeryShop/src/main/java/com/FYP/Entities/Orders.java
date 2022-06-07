package com.FYP.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="Orders")
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
