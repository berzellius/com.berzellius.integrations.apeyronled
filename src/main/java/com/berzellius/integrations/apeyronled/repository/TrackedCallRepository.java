package com.berzellius.integrations.apeyronled.repository;

import com.berzellius.integrations.apeyronled.dmodel.TrackedCall;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by berz on 27.09.2015.
 */
@Transactional(readOnly = true)
public interface TrackedCallRepository extends CrudRepository<TrackedCall, Long>, JpaSpecificationExecutor {

    //Long countBySiteIdAndDtGreaterThanEqualAndDtLessThanEqual(Integer siteId, Date from, Date to);
}
