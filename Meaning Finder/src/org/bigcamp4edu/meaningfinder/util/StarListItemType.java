package org.bigcamp4edu.meaningfinder.util;

public class StarListItemType implements Comparable<StarListItemType> {
	public String starName;
	public String starImgName;
	
	public StarListItemType(String _starName, String _starImgName){
		starName = _starName;
		starImgName = _starImgName;
	}

	@Override
	public int compareTo(StarListItemType another) {
		return this.starName.compareTo(another.starName);
	}
	
}
