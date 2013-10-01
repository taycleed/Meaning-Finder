package org.bigcamp4edu.meaningfinder.util;

public class QuestionListItemType {
	public int listReqNo;
	public String listText;
	public String listStarName;
	public String listImgName;
	public long	timeStamp;
	
	public QuestionListItemType(int _listReqNo, String _listText, String _listStarName, String _listImgName, long _timeStamp){
		listReqNo = _listReqNo;
		listText = _listText;
		listStarName = _listStarName;
		listImgName = _listImgName;
		timeStamp = _timeStamp;
	}
}