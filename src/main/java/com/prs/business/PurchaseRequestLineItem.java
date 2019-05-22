package com.prs.business;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.text.NumberFormat;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PurchaseRequestLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="purchaseRequestid")
	private PurchaseRequest purchaseRequest;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="productid")
	private Product product;
	private int quantity;
	
	public PurchaseRequestLineItem () {
		
	}

	public PurchaseRequestLineItem(PurchaseRequest purchaseRequest, Product productid, int quantity) {
		super();
		this.purchaseRequest = purchaseRequest;
		this.product = productid;
		this.quantity = quantity;
	}

	public PurchaseRequestLineItem(int iD, PurchaseRequest purchaseRequest, Product productid, int quantity) {
		super();
		id = iD;
		this.purchaseRequest = purchaseRequest;
		this.product = productid;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int iD) {
		id = iD;
	}

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequestid) {
		this.purchaseRequest = purchaseRequestid;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product productid) {
		this.product = productid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return "=Product:    " + getProduct().getName() + "=\n" +
		"\tQuantity:  " + getQuantity() + "\n" +
		"\tPrice:     " + formatter.format(getQuantity() * getProduct().getPrice()) + "\n" +
		"\tUser:      " + getPurchaseRequest().getUser().getUserName() + "\n" +
		"\tid:        " + getId() + "\n" +
		"\n";
	}

}
