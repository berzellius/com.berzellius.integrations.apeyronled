package com.berzellius.integrations.apeyronled.repository;

import com.berzellius.integrations.apeyronled.dmodel.ContactAdded;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by berz on 25.06.2017.
 */
@Transactional(readOnly = true)
public interface ContactAddedRepository extends CrudRepository<ContactAdded, Long> {
}
