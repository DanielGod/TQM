package tqm.bianfeng.com.tqm.pojo;

public class cityInfo {
	int id;
	String city;
	String province;
	String sortKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	@Override
	public String toString() {
		return "cityInfo{" +
				"id=" + id +
				", city='" + city + '\'' +
				", province='" + province + '\'' +
				", sortKey='" + sortKey + '\'' +
				'}';
	}
}
