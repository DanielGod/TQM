package tqm.bianfeng.com.tqm.pojo;
import java.util.List;

/**
 * 用户积分
 * */
public class UserPointA {
	
	private String title;
	
	List<UserPointB> points;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<UserPointB> getPoints() {
		return points;
	}

	public void setPoints(List<UserPointB> points) {
		this.points = points;
	}

	
	
}
