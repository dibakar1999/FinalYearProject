package com.FYP.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="Category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String name;

}
