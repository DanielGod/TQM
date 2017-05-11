package tqm.bianfeng.com.tqm.pojo.bank;

import java.io.Serializable;
import java.util.List;

/**
 * 银行网点列表
 * */
public class BankDotItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5467430415788436186L;
	
	private String uid;
	
	private String name; //网点名称
	
	private String address; //网点地址
	
	private Float lat;//网点纬度
	
	private Float lng;//网点经度
	
	private String business;

	private int distance;
	
	private List<String> businessIcons;

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public List<String> getBusinessIcons() {
		return businessIcons;
	}

	public void setBusinessIcons(List<String> businessIcons) {
		this.businessIcons = businessIcons;
	}

	@Override
	public String toString() {
		return "BankDotItem{" +
				"uid='" + uid + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", lat=" + lat +
				", lng=" + lng +
				", business='" + business + '\'' +
				", businessIcons=" + businessIcons +
				'}';
	}
}
