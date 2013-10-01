package org.bigcamp4edu.meaningfinder.util;

public class QuestionListItemType {
	public int listReqNo;
	public String listText;
	public String listImgName;
	public long	timeStamp;
	public QuestionListItemType(int _listReqNo, String _listText, String _listImgName, long _timeStamp){
		listReqNo = _listReqNo;
		listText = _listText;
		listImgName = _listImgName;
		timeStamp = _timeStamp;
	}
}