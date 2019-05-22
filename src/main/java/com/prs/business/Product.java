package com.prs.business;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="vendorID")
    private Vendor vendor;
    private String partNumber;
    private String name;
    private double price;
    private String unit;
    private String photoPath;
    
    public Product () {
    	
    }
    
	public Product(Vendor vendorID, String partNumber, String name, double price, String unit, String photoPath) {
		super();
		this.vendor = vendorID;
		this.partNumber = partNumber;
		this.name = name;
		this.price = price;
		this.unit = unit;
		this.photoPath = photoPath;
	}

	public Product(int iD, Vendor vendorID, String partNumber, String name, double price, String unit, String photoPath) {
		super();
		id = iD;
		this.vendor = vendorID;
		this.partNumber = partNumber;
		this.name = name;
		this.price = price;
		this.unit = unit;
		this.photoPath = photoPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int iD) {
		id = iD;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendorID) {
		this.vendor = vendorID;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	@Override
	public String toString() {
		return "\t=Part Name:   " + getName() + "=\n" +
		"\tid:            " + getId() + "\n" +
		"\tPart Number: " + getPartNumber() + "\n" +
		"\tUnit:        " + getUnit() + "\n" +
		"\tPrice:       " + getPrice() + "\n" +
		"\tVendor:      " + getVendor().getName() + "\n" +
		"\tPhotoPath:   " + getPhotoPath() + "\n" +
		"\n";
	}
  
}
