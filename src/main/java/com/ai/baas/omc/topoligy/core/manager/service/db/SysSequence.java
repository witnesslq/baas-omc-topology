package com.ai.baas.omc.topoligy.core.manager.service.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.service.ISysSequenceCredit;
import com.ai.baas.omc.topoligy.core.persistence.dao.SysSequenceCreditDao;
import com.ai.baas.omc.topoligy.core.persistence.dao.impl.SysSequenceCreditDaoImpl;
import com.ai.baas.omc.topoligy.core.pojo.SysSequenceCredit;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lvsj on 2015/9/27.
 */
public final class SysSequence implements ISysSequenceCredit {
	
	private  static final Logger logger = LoggerFactory.getLogger(SysSequence.class);
	
	private static final JdbcProxy dbproxy = JdbcProxy.getInstance();
	
    private static SysSequence ourInstance = new SysSequence();
    
    public static SysSequence getInstance() {
        if (ourInstance==null){
            ourInstance = new SysSequence();
        }
        return ourInstance;
    }
    private SysSequence() {

    }

    public   List<Long>   getSequence(String name, int nCount) throws OmcException {
        List<Long> longList = new ArrayList<Long>();
        long currvalue;
        long nextvalue;

        try{
	        SysSequenceCreditDao sysSequenceCreditDao = new SysSequenceCreditDaoImpl();
	        Connection conn = dbproxy.getConnection();
	        SysSequenceCredit sequence = sysSequenceCreditDao.selectByKey(conn, name);
	        
	        if (sequence==null){
	            throw  new OmcException("ERROR","序列名[" + name + "]没有定义,请对表sys_sequence_credit进行维护");
	        }
	        
	        //获取当前值
	        currvalue = sequence.getCurrentValue();
	        nextvalue = currvalue + nCount;
	        sequence.setCurrentValue(nextvalue);
	        
	        Connection conn1 = dbproxy.getConnection();
	        Boolean autocommitflag = conn1.getAutoCommit();
	        
	        if (!autocommitflag){
	        	conn1.setAutoCommit(true);
	        }
	        
	        int nRet =  sysSequenceCreditDao.update(conn1, sequence);
	
	        if (nRet <= 0){
	        	conn1.setAutoCommit(autocommitflag);
	            throw  new OmcException("ERROR","序列["+name+"更新异常");
	        }
	
	        if (!autocommitflag){
	        	conn1.setAutoCommit(autocommitflag);
	        }
	        
	        for(long l=currvalue;l<nextvalue;l++){
	            longList.add(l);
	        }
        }catch(SQLException e){
        	logger.error("获取序列异常" + name,e);
        	throw new OmcException("获取序列异常" + name,e);
        }
        return longList;
    }

    public Long getSequence(String name) throws OmcException{
        List<Long> longList = getSequence(name, 1);
        if ((longList == null)||(longList.isEmpty())){
            throw new OmcException("ERROR","序列名[" + name + "]获取序列异常");
        }
        return longList.get(0);
    }
}
