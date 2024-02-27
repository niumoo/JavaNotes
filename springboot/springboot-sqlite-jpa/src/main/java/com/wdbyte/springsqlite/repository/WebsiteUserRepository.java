package com.wdbyte.springsqlite.repository;

import com.wdbyte.springsqlite.model.WebsiteUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteUserRepository extends CrudRepository<WebsiteUser, Long> {

    WebsiteUser findByUsername(String name);
}