package com.branch.manager.address.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.branch.manager.address.book.model.AddressBook;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Integer> {

}
