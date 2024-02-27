package com.wdbyte.springsqlite.repository;

import com.wdbyte.springsqlite.model.WebsiteUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteUserRepository extends CrudRepository<WebsiteUser, Long> {

    /**
     * 根据 username 查询数据
     * @param name
     * @return
     */
    WebsiteUser findByUsername(String name);
}