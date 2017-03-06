package com.berzellius.integrations.apeyronled.repository;

import com.berzellius.integrations.apeyronled.dmodel.Call;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * Created by berz on 27.09.2015.
 */
@Transactional(readOnly = true)
public interface CallRepository extends CrudRepository<Call, Long>, JpaSpecificationExecutor {

    Long countByProjectIdAndDtGreaterThanEqualAndDtLessThanEqual(Integer projectId, Date from, Date to);
}
