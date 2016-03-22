package com.ai.baas.omc.topoligy.core.persistence.dao;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;

import java.sql.Connection;
import java.util.List;

/**
 * 信用度操作
 * Created by jackieliu on 16/3/22.
 */
public interface OmcCreditDao {

    public List<OmcCredit> selectCredit(
            Connection connection,String tenantId, String ownerType, String ownerId, String resourceCode)
            throws OmcException;
}
