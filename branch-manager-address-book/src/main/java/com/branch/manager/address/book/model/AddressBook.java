package com.branch.manager.address.book.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "contacts")
public class AddressBook {

	@Id
	@SequenceGenerator(name = "myaddresssequence", initialValue = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "myaddresssequence")
	private int id;

	@Size(min = 2)
	private String name;

	@Size(min = 10)
	private String phoneNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;

	public AddressBook() {
	}

	public AddressBook(int id, @Size(min = 2) String name, @Size(min = 10) String contact, User user) {
		this.id = id;
		this.name = name;
		this.phoneNumber = contact;
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String contact) {
		this.phoneNumber = contact;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AddressBook [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + ", user=" + user + "]";
	}

}
