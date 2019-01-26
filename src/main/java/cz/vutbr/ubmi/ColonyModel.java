package cz.vutbr.ubmi;

import java.util.ArrayList;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

public class ColonyModel<T extends RealType>  {
	
	public RandomAccessibleInterval<T> img;
	public ColonyView view;
	public ArrayList<MaskCircle> maskCircles;
	public ColonyController controller;
	
	public ColonyModel(RandomAccessibleInterval<T> img) {
		this.img=img;
	}
	
	public void setView(ColonyView view) {
		this.view=view;
	}
	
	public void setController(ColonyController controller) {
		this.controller=controller;
	}
	
	
	
	public class MaskCircle {
		public double x,y,r;
		public MaskCircle(double x,double y,double r) {
			this.x=x;
			this.y=y;
			this.r=r;
		}
	}
	

}
