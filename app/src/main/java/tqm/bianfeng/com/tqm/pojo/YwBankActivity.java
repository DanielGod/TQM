package tqm.bianfeng.com.tqm.pojo;

/**
 * 银行活动 银行活动
 */
public class YwBankActivity  {


	private Integer activityId; // 活动Id

	private String activityTitle; // 活动标题

	private String institution; // 所属机构Id

	private Integer activityViews;
	
	private String imageUrl;
	
	private String activityContent="";
	
	private Integer createUser;
	
	private String statusCode;
	
	private String remark;
	
	private String homeShow;

	
	private Integer isDelete;
	

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}


	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Integer getActivityViews() {
		return activityViews;
	}

	public void setActivityViews(Integer activityViews) {
		this.activityViews = activityViews;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getActivityContent() {
		return activityContent;
	}

	public void setActivityContent(String activityContent) {
		this.activityContent = activityContent;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getHomeShow() {
		return homeShow;
	}

	public void setHomeShow(String homeShow) {
		this.homeShow = homeShow;
	}


	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "YwBankActivity{" +
				"activityId=" + activityId +
				", activityTitle='" + activityTitle + '\'' +
				", institution='" + institution + '\'' +
				", activityViews=" + activityViews +
				", imageUrl='" + imageUrl + '\'' +
				", activityContent='" + activityContent + '\'' +
				", createUser=" + createUser +
				", statusCode='" + statusCode + '\'' +
				", remark='" + remark + '\'' +
				", homeShow='" + homeShow + '\'' +
				", isDelete=" + isDelete +
				'}';
	}
}