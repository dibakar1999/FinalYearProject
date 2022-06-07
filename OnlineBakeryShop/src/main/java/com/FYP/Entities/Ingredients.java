package com.FYP.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Data
@Table(name="Ingredients_Table")
public class Ingredients {
	@Id
	private int iId;
	@NotEmpty(message="Error name cannot be empty!!!")
	private String name;
	private String image;
	private String items;
	
	
	

}
