package com.branch.manager.address.book.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id
	@SequenceGenerator(name = "myusersequence", initialValue = 4)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "myusersequence")
	private int id;

	@Size(min = 2)
	private String name;

	@OneToMany(mappedBy = "user")
	private List<AddressBook> userContacts;

	public User() {
	}

	public User(Integer id, String name) {
		this.id = id;
		this.name = name;
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

	public List<AddressBook> getUserContacts() {
		return userContacts;
	}

	public void setUserContacts(List<AddressBook> userContacts) {
		this.userContacts = userContacts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", userContacts=" + userContacts + "]";
	}

}
