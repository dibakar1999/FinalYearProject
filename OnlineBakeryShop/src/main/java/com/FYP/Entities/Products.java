package com.FYP.Entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name="product_table")
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long pId;
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cId", referencedColumnName = "cId")
	@JsonIgnore
	private Category category;
	private double price;
	private double weight;
	private String description;
	private String productImage;

}
