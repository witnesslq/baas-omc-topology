package com.ai.baas.omc.topoligy.core.dubbo.service;

import com.ai.baas.amc.api.virtualdeduct.interfaces.IBalanceQuerySV;
import com.ai.baas.amc.api.virtualdeduct.param.BalanceQueryRequest;
import com.ai.baas.amc.api.virtualdeduct.param.VdRealTimeBalance;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;
import com.ai.baas.omc.topoligy.core.util.DateUtils;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 实时余额获取
 * Created by jackieliu on 16/4/1.
 */
@Component
public class RealtimeBalanceService {
    private static Logger LOGGER = LoggerFactory.getLogger(RealtimeBalanceService.class);
    private static final String CFG_RETURN_CODE = "MMP-000000";
    @Reference
    IBalanceQuerySV balanceQuerySV;

    public RealTimeBalance queryBalance(String productType, OmcObj topOmcObj)throws OmcException {
        BalanceQueryRequest queryRequest = new BalanceQueryRequest();
        queryRequest.setOwerId(topOmcObj.getOwerid());
        queryRequest.setOwerType(topOmcObj.getOwertype());
        queryRequest.setTenantId(topOmcObj.getTenantid());
        queryRequest.setBusinessCode(topOmcObj.getBusinesscode());
        queryRequest.setProductType(productType);
        RealTimeBalance realTimeBalance = null;
        try {
            VdRealTimeBalance vdBalance = balanceQuerySV.cancelAccountProcess(queryRequest);
            LOGGER.info(vdBalance.toString());
            if (!CFG_RETURN_CODE.equals(vdBalance.getReturnCode())) {
                throw new OmcException("getBalance", "调用模拟销账异常," + CFG_RETURN_CODE + "[" + vdBalance.getReturnCode() + "]");
            }
            realTimeBalance = new RealTimeBalance();
            realTimeBalance.setOwner(topOmcObj);
            realTimeBalance.setBalance(vdBalance.getBalance());
            realTimeBalance.setCreditline(vdBalance.getCreditLine());
            realTimeBalance.setRealBalance(vdBalance.getRealBalance());
            realTimeBalance.setRealBill(vdBalance.getRealBill());
            realTimeBalance.setUnIntoBill(vdBalance.getUnIntoBill());
            realTimeBalance.setUnSettleBill(vdBalance.getUnSettleBill());
            realTimeBalance.setFstUnSettleMon(vdBalance.getFstUnsettLemon());
            realTimeBalance.setAcctMonth(vdBalance.getAcctMonth());
            realTimeBalance.setUnSettleMons(
                    DateUtils.monthDiffs(vdBalance.getFstUnsettLemon(),vdBalance.getAcctMonth()));
            realTimeBalance.setExtInfo(vdBalance.getExpandInfo());
        } catch (BusinessException e) {
            LOGGER.error("调用模拟销账发生业务处理异常",e);
            throw new OmcException("getBalance", "调用模拟销账发生业务处理异常:错误码"+e.getErrorCode()+",错误信息" + e.getMessage());
        } catch (SystemException e) {
            LOGGER.error("调用模拟销账发生系统处理异常",e);
            throw new OmcException("getBalance", "调用模拟销账发生系统处理异常:错误信息" + e.getMessage());
        } catch (OmcException e){
            throw  e;
        } catch (Exception e){
            LOGGER.error("调用模拟销账发生系统处理异常",e);
            throw new OmcException("调用模拟销账发生未知异常", e.getMessage());
        }
        return realTimeBalance;
    }
}
