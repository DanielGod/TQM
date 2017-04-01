package tqm.bianfeng.com.tqm.lawhelp.tools;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tqm.bianfeng.com.tqm.Util.CharacterParser;
import tqm.bianfeng.com.tqm.Util.PinyinComparator;
import tqm.bianfeng.com.tqm.pojo.cityInfo;

public class InternetImpl {
	private static InternetImpl internetImpl;
	private Context mContext;
	CharacterParser mCharacterParser;
	String url = "";

	PinyinComparator pinyinComparator;
	private InternetImpl() {

	}

	public static InternetImpl createInternetImpl() {
		if (internetImpl == null) {
			internetImpl = new InternetImpl();

		}

		return internetImpl;

	}
	//模糊查询
	public List<cityInfo> searchCity(List<cityInfo> datas,
										  String search) {
		List<cityInfo> searchCity = new ArrayList<>();
		mCharacterParser = CharacterParser.getInstance();
		for (int i = 0; i < datas.size(); i++) {
			if (search.length() <= mCharacterParser.getSelling(
					datas.get(i).getCity()).length()) {
				if (mCharacterParser.getSelling(datas.get(i).getCity())
						.substring(0, search.length()).contains(search)) {

					searchCity.add(datas.get(i));

				} else if (datas.get(i).getCity().contains(search)) {
					searchCity.add(datas.get(i));
				}
			}
		}
		return searchCity;
	}

	//数据排序
	public List<cityInfo> sortingData( List<cityInfo> data){
		pinyinComparator=new PinyinComparator();
		Collections.sort(data, pinyinComparator);
		return data;
	}
}
