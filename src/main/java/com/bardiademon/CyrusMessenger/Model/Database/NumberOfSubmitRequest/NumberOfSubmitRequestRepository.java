package com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberOfSubmitRequestRepository extends JpaRepository<NumberOfSubmitRequest, Long>
{
    NumberOfSubmitRequest findByType (SubmitRequestType type);
}
