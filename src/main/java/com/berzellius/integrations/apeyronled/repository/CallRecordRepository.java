package com.berzellius.integrations.apeyronled.repository;

import com.berzellius.integrations.apeyronled.dmodel.CallRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by berz on 28.03.2017.
 */
public interface CallRecordRepository extends CrudRepository<CallRecord, Long> {
}
