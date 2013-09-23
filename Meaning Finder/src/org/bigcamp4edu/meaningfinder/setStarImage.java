package org.bigcamp4edu.meaningfinder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class setStarImage {
	
	private Context sContext;
	private String starImageStr;
	private Drawable starDrawable;
	
	public setStarImage(Context context){
		sContext	= context;
	}
	
	public void setStarImageName(String starImg){
		starImageStr	= (starImg);
		Log.d("VOM setStarImage", "StarImage Name: " + starImageStr);
	}
	
	public Drawable getStarImage(){
		
		if(starImageStr.equals("con_aries_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_aries_small);
    	}else if(starImageStr.equals("con_camelopardalis_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_camelopardalis_small);
    	}else if(starImageStr.equals("con_cancerconstellation_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_cancerconstellation_small);
    	}else if(starImageStr.equals("con_canisminoris_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_canisminoris_small);
    	}else if(starImageStr.equals("con_capricornus_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_capricornus_small);
    	}else if(starImageStr.equals("con_casiopea_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_casiopea_small);
    	}else if(starImageStr.equals("con_comaberenies_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_comaberenies_small);
    	}else if(starImageStr.equals("con_gemin_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_gemin_small);
    	}else if(starImageStr.equals("con_leo_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_leo_small);
    	}else if(starImageStr.equals("con_sagittarius_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_sagittarius_small);
    	}else if(starImageStr.equals("con_scorpius_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_scorpius_small);
    	}else if(starImageStr.equals("con_ursamajor_small.png")){
    		starDrawable	= (Drawable) sContext.getResources().getDrawable(R.drawable.con_ursamajor_small);
    	}
		
		return starDrawable;
	}
	
}
