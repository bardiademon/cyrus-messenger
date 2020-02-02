package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SubmitRequestRepository extends JpaRepository<SubmitRequest, Long>
{
    int countByTypeAfterAndMainAccountIdAndActiveTrue (SubmitRequestType type , long id);

    int countByTypeAndIpAndActiveTrue (SubmitRequestType type , String ip);

    @Transactional
    @Modifying
    @Query ("update SubmitRequest submitRequest set " +
            "submitRequest.active = false " +
            "where submitRequest.mainAccount.id = :ID_USER " +
            "and submitRequest.active = true " +
            "and submitRequest.type = :SUBMIT_REQUEST_TYPE")
    void deactiveAllRequest (@Param ("ID_USER") long idUser , @Param ("SUBMIT_REQUEST_TYPE") SubmitRequestType submitRequestType);

    @Transactional
    @Modifying
    @Query ("update SubmitRequest submitRequest set " +
            "submitRequest.active = false " +
            "where submitRequest.ip = :IP " +
            "and submitRequest.active = true " +
            "and submitRequest.type = :SUBMIT_REQUEST_TYPE")
    void deactiveAllRequest (@Param ("IP") String ip , @Param ("SUBMIT_REQUEST_TYPE") SubmitRequestType submitRequestType);

}
