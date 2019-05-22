package com.prs.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PurchaseRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userid")
	private User user;
	private String description;
	private String justification;
	private LocalDate dateNeeded;
	private String deliveryMode;
	private String status;
	private double total;
	private LocalDate submittedDate;
	private String reasonForRejection;

	public PurchaseRequest() {

	}

	public PurchaseRequest(User userid, String description, String justification, LocalDate dateNeeded,
			String deliveryMode, String status, double total, LocalDate submittedDate, String reasonForRejection) {
		super();
		this.user = userid;
		this.description = description;
		this.justification = justification;
		this.dateNeeded = dateNeeded;
		this.deliveryMode = deliveryMode;
		this.status = status;
		this.total = total;
		this.submittedDate = submittedDate;
		this.reasonForRejection = reasonForRejection;
	}

	public PurchaseRequest(int iD, User userid, String description, String justification, LocalDate dateNeeded,
			String deliveryMode, String status, double total, LocalDate submittedDate, String reasonForRejection) {
		super();
		id = iD;
		this.user = userid;
		this.description = description;
		this.justification = justification;
		this.dateNeeded = dateNeeded;
		this.deliveryMode = deliveryMode;
		this.status = status;
		this.total = total;
		this.submittedDate = submittedDate;
		this.reasonForRejection = reasonForRejection;
	}

	public int getId() {
		return id;
	}

	public void setId(int iD) {
		id = iD;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User userid) {
		this.user = userid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public LocalDate getDateNeeded() {
		return dateNeeded;
	}

	public void setDateNeeded(LocalDate dateNeeded) {
		this.dateNeeded = dateNeeded;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate submittedDate) {
		this.submittedDate = submittedDate;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	@Override
	public String toString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
		return "=User: " + getUser().getUserName() + "=\n" +
		"\tid:            " + getId() + "\n" +
		"\tDescription:   " + getDescription() + "\n" +
		"\tJustification: " + getJustification() + "\n" +
		"\tDate Needed:   " + dtf.format(getDateNeeded()) + "\n" +
		"\tDelivery Mode: " + getDeliveryMode() + "\n" +
		"\tStatus:        " + getStatus() + "\n";
	}

}
