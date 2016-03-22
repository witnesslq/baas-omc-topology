package com.ai.baas.omc.topoligy.core.persistence.dao.impl;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.persistence.dao.OmcCreditDao;
import com.ai.baas.omc.topoligy.core.pojo.OmcCredit;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.baas.omc.topoligy.core.util.db.JdbcTemplate;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jackieliu on 16/3/22.
 */
public class OmcCreditDaoImpl implements OmcCreditDao {
    private Logger logger = LoggerFactory.getLogger(OmcCreditDaoImpl.class);
    String tableName ="omc_creditfee";

    @Override
    public List<OmcCredit> selectCredit(
            Connection connection,String tenantId, String ownerType, String ownerId, String resourceCode)
            throws OmcException {
        List<OmcCredit> omcCredits = new ArrayList<OmcCredit>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select credit_seq, credit_type, credit_line,eff_time,exp_time from ");
            sql.append(tableName).append(" where ");
            sql.append(" tenant_id =").append("'").append(tenantId).append("'");
            sql.append(" and  owner_type =").append("'").append(ownerType).append("'");
            sql.append(" and  owner_id =").append("'").append(ownerId).append("'");
            sql.append(" and  resource_code =").append("'").append(resourceCode).append("'");

            List<Map<String,Object>> rows =  JdbcTemplate.query(sql.toString(), connection, new MapListHandler());
            if ((rows == null)||(rows.isEmpty())){
                logger.info("信用度没取到数据【omc_creditfee】");
                return Collections.emptyList();
            }

            for (Map<String, Object> map : rows) {
                OmcCredit omcCredit = new OmcCredit();
                omcCredit.setOwnerid(ownerId);
                omcCredit.setOwnertype(ownerType);
                omcCredit.setTenantid(tenantId);
                String creditline = (map.get("credit_line") == null) ? "0.0":map.get("credit_line").toString();
                omcCredit.setCreditline(Double.parseDouble(creditline));
                omcCredit.setCredittype(map.get("credit_type").toString());
                String efftime = map.get("eff_time").toString();
                String exptime = map.get("exp_time").toString();
                omcCredit.setEffDate(DateUtils.getTimestamp(efftime, "yyyy-MM-dd HH:mm:ss"));
                omcCredit.setExpDate(DateUtils.getTimestamp(exptime, "yyyy-MM-dd HH:mm:ss"));
                omcCredits.add(omcCredit);
            }
        } catch (Exception e) {
            throw new OmcException("ConfigObtain",e);
        }
        return omcCredits;
    }
}
