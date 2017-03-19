package com.berzellius.integrations.apeyronled.repository;


import com.berzellius.integrations.apeyronled.dmodel.LeadFromSite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by berz on 15.06.2016.
 */
@Transactional
public interface LeadFromSiteRepository extends CrudRepository<LeadFromSite, Long> {
}
