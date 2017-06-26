package tqm.bianfeng.com.tqm.pojo;

import java.io.Serializable;

/**
 * 入驻申请
 * */
public class YwRzsq implements Serializable{

	@Override
	public String toString() {
		return "YwRzsq{" +
				"id='" + id + '\'' +
				", gsmc='" + gsmc + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", county='" + county + '\'' +
				", lxr='" + lxr + '\'' +
				", lxdh='" + lxdh + '\'' +
				", sqlx='" + sqlx + '\'' +
				", lxbq='" + lxbq + '\'' +
				", gslogo='" + gslogo + '\'' +
				", gsimage='" + gsimage + '\'' +
				", grmp='" + grmp + '\'' +
				", shzt='" + shzt + '\'' +
				", shbz='" + shbz + '\'' +
				", srq=" + sqr +
				'}';
	}

	/**
	 * 
	 */

	private String id;
	
	private String gsmc;//公司名称
	
	private String province;//省
	
	private String city; //市
	
	private String county;//县区
	
	private String lxr; //联系人
	
	private String lxdh; //联系方式
	
	private String sqlx; // 申请类型:01-公司;02-个人;03-信贷经理
	
	private String lxbq; //申请类型标签:1001-民间资本;1002中介;2001资方;2002个人中介;信贷经理
	
	private String gslogo; //公司logo
	
	private String gsimage; //公司相关图片
	
	private String grmp;//个人名片(存放各种上传的图片)
	
	private String shzt; // 审核状态:00-待审;01-审核通过;02-审核不通过
	
	private String shbz; //审核备注
	
	private Integer sqr;//申请人ID
	private Integer institutionId;//机构Id
	Long createDate;//申请日期

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getSqlx() {
		return sqlx;
	}

	public void setSqlx(String sqlx) {
		this.sqlx = sqlx;
	}

	public String getLxbq() {
		return lxbq;
	}

	public void setLxbq(String lxbq) {
		this.lxbq = lxbq;
	}

	public String getGslogo() {
		return gslogo;
	}

	public void setGslogo(String gslogo) {
		this.gslogo = gslogo;
	}

	public String getGsimage() {
		return gsimage;
	}

	public void setGsimage(String gsimage) {
		this.gsimage = gsimage;
	}

	public String getGrmp() {
		return grmp;
	}

	public void setGrmp(String grmp) {
		this.grmp = grmp;
	}

	public String getShzt() {
		return shzt;
	}

	public void setShzt(String shzt) {
		this.shzt = shzt;
	}

	public String getShbz() {
		return shbz;
	}

	public void setShbz(String shbz) {
		this.shbz = shbz;
	}

	public Integer getSqr() {
		return sqr;
	}

	public void setSqr(Integer sqr) {
		this.sqr = sqr;
	}
}
