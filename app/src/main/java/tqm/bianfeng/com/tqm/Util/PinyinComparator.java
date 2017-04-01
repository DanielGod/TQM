package tqm.bianfeng.com.tqm.Util;

import java.util.Comparator;

import tqm.bianfeng.com.tqm.pojo.cityInfo;

/**
 *
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<cityInfo> {

	public int compare(cityInfo o1, cityInfo o2) {
		if (o1.getSortKey().equals("@")
				|| o2.getSortKey().equals("#")) {
			return -1;
		} else if (o1.getSortKey().equals("#")
				|| o2.getSortKey().equals("@")) {
			return 1;
		} else {
			return o1.getSortKey().compareTo(o2.getSortKey());
		}
	}

}
