package com.wealthrating.repository;

import com.wealthrating.entity.RichPerson;
import org.springframework.data.repository.CrudRepository;

public interface RichPersonRepository extends CrudRepository<RichPerson, Integer> {
}
