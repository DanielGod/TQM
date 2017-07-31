package tqm.bianfeng.com.tqm.pojo;

import java.io.Serializable;

/**
 * 贷款申请基类
 * */
public class YwDksq  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2789663699328401933L;

	private String id;
	
	private String sqr;//申请人
	
	private String sqrId; //申请人Id
	
	private String dkje; //贷款金额
	
	private Integer dkqx; //贷款期限

	private String dkyt; //贷款用途
	
	private String szcs; // 所在城市
	
	private String zysf; //职业身份:01-上班族;02-企业主;03-个体户;04-自由职业
	
	private String lxdh;//联系电话
	
	private String income; //收入(元)
	
	private String gsmc; //公司名称
	
	private String sfjj; //是否交金 否：00 是01
	
	private String sfjnsb; //是否缴纳社保
	
	private String sfyf; //是否有房
	
	private String fcgz; // 房产估值
	
	private String sfyc; //是否有车
	
	private String ccgz; //车辆估值
	
	private String sqzt;//申请状态

	private String  extractMoney;//领取费用

	private String shbz;// 审核备注

	private String isReceive ;//1-领取，0-未领取

	@Override
	public String toString() {
		return "YwDksq{" +
				"id='" + id + '\'' +
				", sqr='" + sqr + '\'' +
				", sqrId='" + sqrId + '\'' +
				", dkje='" + dkje + '\'' +
				", dkqx=" + dkqx +
				", dkyt='" + dkyt + '\'' +
				", szcs='" + szcs + '\'' +
				", zysf='" + zysf + '\'' +
				", lxdh='" + lxdh + '\'' +
				", income='" + income + '\'' +
				", gsmc='" + gsmc + '\'' +
				", sfjj='" + sfjj + '\'' +
				", sfjnsb='" + sfjnsb + '\'' +
				", sfyf='" + sfyf + '\'' +
				", fcgz='" + fcgz + '\'' +
				", sfyc='" + sfyc + '\'' +
				", ccgz='" + ccgz + '\'' +
				", sqzt='" + sqzt + '\'' +
				", extractMoney='" + extractMoney + '\'' +
				", shbz='" + shbz + '\'' +
				", isReceive='" + isReceive + '\'' +
				'}';
	}

	public String getDkyt() {
		return dkyt;
	}

	public void setDkyt(String dkyt) {
		this.dkyt = dkyt;
	}

	public String getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(String isReceive) {
		this.isReceive = isReceive;
	}

	public String getShbz() {
		return shbz;
	}

	public void setShbz(String shbz) {
		this.shbz = shbz;
	}

	public String getExtractMoney() {
		return extractMoney;
	}

	public void setExtractMoney(String extractMoney) {
		this.extractMoney = extractMoney;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getSqrId() {
		return sqrId;
	}

	public void setSqrId(String sqrId) {
		this.sqrId = sqrId;
	}

	public String getDkje() {
		return dkje;
	}

	public void setDkje(String dkje) {
		this.dkje = dkje;
	}

	public Integer getDkqx() {
		return dkqx;
	}

	public void setDkqx(Integer dkqx) {
		this.dkqx = dkqx;
	}

	public String getSzcs() {
		return szcs;
	}

	public void setSzcs(String szcs) {
		this.szcs = szcs;
	}

	public String getZysf() {
		return zysf;
	}

	public void setZysf(String zysf) {
		this.zysf = zysf;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getSfjj() {
		return sfjj;
	}

	public void setSfjj(String sfjj) {
		this.sfjj = sfjj;
	}

	public String getSfjnsb() {
		return sfjnsb;
	}

	public void setSfjnsb(String sfjnsb) {
		this.sfjnsb = sfjnsb;
	}

	public String getSfyf() {
		return sfyf;
	}

	public void setSfyf(String sfyf) {
		this.sfyf = sfyf;
	}

	public String getFcgz() {
		return fcgz;
	}

	public void setFcgz(String fcgz) {
		this.fcgz = fcgz;
	}

	public String getSfyc() {
		return sfyc;
	}

	public void setSfyc(String sfyc) {
		this.sfyc = sfyc;
	}

	public String getCcgz() {
		return ccgz;
	}

	public void setCcgz(String ccgz) {
		this.ccgz = ccgz;
	}

	public String getSqzt() {
		return sqzt;
	}

	public void setSqzt(String sqzt) {
		this.sqzt = sqzt;
	}

	
	
}
