package com.ai.baas.omc.topoligy.core.business;

import java.util.ArrayList;
import java.util.List;

import com.ai.baas.omc.topoligy.core.business.base.BaseProcess;
import com.ai.baas.omc.topoligy.core.business.command.rule.ResourceCheck;
import com.ai.baas.omc.topoligy.core.constant.AccOwnerType;
import com.ai.baas.omc.topoligy.core.constant.OwnerType;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.manager.service.AccountService;
import com.ai.baas.omc.topoligy.core.manager.service.CustomerService;
import com.ai.baas.omc.topoligy.core.manager.service.SubsUserService;
import com.ai.baas.omc.topoligy.core.manager.service.shm.AccountServiceImplShm;
import com.ai.baas.omc.topoligy.core.manager.service.shm.CustomerServiceImplShm;
import com.ai.baas.omc.topoligy.core.manager.service.shm.SubsUserServiceImplShm;
import com.ai.baas.omc.topoligy.core.pojo.Account;
import com.ai.baas.omc.topoligy.core.pojo.Customer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * 三户信息
 */
public final class InformationProcessor extends BaseProcess {
	
	private static final Logger logger = LoggerFactory.getLogger(InformationProcessor.class);
	
	private SubsUserService subsUserService;
	private AccountService accountService;
	private CustomerService customerService;
	
	private Customer customer = null;
	private List<User> users = new ArrayList<User>() ;
	private List<Account> accounts = new ArrayList<Account>();	

	public InformationProcessor(ConfigContainer cfg, OmcObj obj, JsonObject data) throws OmcException {
		super(cfg, obj, data);
		subsUserService = new SubsUserServiceImplShm();
		accountService = new AccountServiceImplShm();
		customerService = new CustomerServiceImplShm();
	}


	@Override
	public void process() throws OmcException {
		String ownertype = getOmcobj().getOwertype();
		String ownerid = getOmcobj().getOwerid();
		String tenantid = getOmcobj().getTenantid();
		String policyId = this.getInput().get(OmcCalKey.OMC_POLICY_ID).getAsString();

		ConfigContainer cfg = this.getConfig();
		String infofrom = cfg.getCfgPara(OmcCalKey.OMC_CFG_INFORMATION_FROM, tenantid, policyId, null);
		//初始bean，确定三户信息的来源
		this.prepare(infofrom);
		
		check(tenantid,ownertype,policyId);
		//账户
		if (OwnerType.ACCT.equals(ownertype)){
			infoBuilerByAcct(tenantid,ownertype,ownerid);
		}
		//客户
		if (OwnerType.CUST.equals(ownertype)){
			infoBuilerByCust(tenantid,ownertype,ownerid);
		}
		//用户
		if (OwnerType.SERV.equals(ownertype)){
			infoBuilerByUser(tenantid,ownertype,ownerid);
		}


	}

	private void check(String tenantid,String ownertype,String policyId) throws OmcException{
		if (StringUtils.isBlank(ownertype)){
			throw new OmcException("Information","信控对象类型不能为空");
		}
		
		if (!(OwnerType.ACCT.equals(ownertype))
				&& !(OwnerType.CUST.equals(ownertype))
				&& !(OwnerType.SERV.equals(ownertype))){
			throw new OmcException("Information","不支持的信控对象类型:" + ownertype);
		}
		//根据余额模式检测 owner_type的正确性
		if  (!ResourceCheck.checkOwnerType(tenantid,ownertype,policyId,this.getConfig())){
			throw new OmcException("Information","信控对象类型与信控参数配置不符：OWERTYPE[" + ownertype +"]");
		}

	}

	/**
	 * 通过客户查询三户信息
	 * @param tenantid
	 * @param ownertype
	 * @param ownerid
     * @return
     */
	private boolean infoBuilerByCust(String tenantid,String ownertype,String ownerid)throws OmcException{
		logger.error("待扩展");
		return false;
	}
	/**
	 * 通过账户查找三户资料
	* @Title: infoBuilerByAcct 
	* @Description:
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws OmcException
	 */
	private boolean infoBuilerByAcct(String tenantid, String ownertype, String ownerid) throws OmcException {
		//获取账户信息
		Account account = accountService.selectById(tenantid, ownerid);
		if (null == account) {
			throw new OmcException("Information", "没有取到账户信息infoOwner:"
					+ paramsString(tenantid, ownertype, ownerid));
		}

		//默认设想为客户id
		String custId = account.getOwnerId();
		//判断用户类型,如果为用户类型
		if (AccOwnerType.USER_TYPE.equals(account.getOwnerType())) {
			User user = subsUserService.selectById(tenantid, account.getOwnerId());
			if (user != null)
				custId = user.getCustomerid();
			else
				throw new OmcException("Information", "账户属主为用户,但无法查询到用户信息,账户信息:"
						+ account.toString());
		//不是用户类型,也不是客户类型,则抛出异常
		}else if (!AccOwnerType.CUST_TYPE.equals(account.getOwnerType())){
			throw new OmcException("Information", "账户属主类型无法识别,账户信息:"
					+ account.toString());
		}

		//获取客户信息
		Customer cust = customerService.getCustomer(tenantid, custId);
		if (null == cust) {
			throw new OmcException("Information", "没有取到客户信息infoOwner"
					+ paramsString(tenantid, ownertype, ownerid));
		}

		//获取用户信息
		List<User> usrs = subsUserService.selectByAcctId(tenantid, ownerid);
		if ((null == usrs) || (usrs.isEmpty())) {
			throw new OmcException("Information", "没有取到客户信息infoOwner"
					+ paramsString(tenantid, ownertype, ownerid));
		}
		accounts.add(account);
		customer = cust;
		users = usrs;

		return true;
	}
	
	/**
	 * 	通过用户查询三户信息
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws OmcException
	 */
	private boolean infoBuilerByUser(String tenantid,String ownertype,String ownerid) throws OmcException{
		//获取用户信息
		User user = subsUserService.selectById(tenantid,ownerid); 
		if (null == user){
			throw new OmcException("Information","没有取到用户信息infoOwner:"
					+ paramsString(tenantid,ownertype,ownerid));
		}
		
		//获取账户信息
		Account account = accountService.selectById(tenantid,user.getAccountid());
		if (null == account ){
			throw new OmcException("Information","没有取到账户信息infoOwner:"
					+ paramsString(tenantid,ownertype,ownerid));
		}

		//获取客户信息
		Customer cust = customerService.getCustomer(tenantid,user.getCustomerid());
		if (null == cust){
			throw new OmcException("Information","没有取到客户信息infoOwner:"
					+ paramsString(tenantid,ownertype,ownerid));
		}
		
		users.add(user);
		customer = cust;
		accounts.add(account);
		
		return true;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public String getRemindNbr(String remintype,String tenantid,String ownertype,String ownerid) throws OmcException{
		User user = subsUserService.selectById(tenantid, ownerid);
		if (user == null){
			throw new OmcException("Information","获取提醒号码异常:userid：" + ownerid );
		}
		if (StringUtils.isBlank(user.getFactorcode())){
			throw new OmcException("Information","资料异常没有配置提醒号码:userid：" + ownerid );
		}
		return user.getFactorcode();
	}

	@Override
	public void prepare(JsonObject data) throws OmcException {
		// TODO Auto-generated method stub
	}

	@Override
	public void prepare(String cfg) throws OmcException {
		// TODO Auto-generated method stub
	}

	private String paramsString(String tenantid,String ownertype,String ownerid){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(OmcCalKey.OMC_OWNER_ID, ownerid);
		jsonObject.addProperty(OmcCalKey.OMC_OWNER_TYPE, ownertype);
		jsonObject.addProperty(OmcCalKey.OMC_TENANT_ID, tenantid);
		return jsonObject.toString();
	}
}
