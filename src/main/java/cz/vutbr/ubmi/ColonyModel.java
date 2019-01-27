package cz.vutbr.ubmi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

public class ColonyModel<T extends RealType>  {
	
	public RandomAccessibleInterval<T> img;
	public ColonyView view;
	public ArrayList<MaskCircle> maskCircles = new ArrayList<MaskCircle>();
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
	
	
	
	public static class MaskCircle {
		public double x,y,r,t;
		public MaskCircle(double x,double y,double r) {
			this.x=x;
			this.y=y;
			this.r=r;
			this.t=0;
		}
		public MaskCircle(double x,double y,double r,double t) {
			this.x=x;
			this.y=y;
			this.r=r;
			this.t=t;
		}
		
		public MaskCircle copy() {
			MaskCircle cc= new MaskCircle(this.x,this.y,this.r,this.t);
			return cc;
		}
	}



	public MaskCircle getLastMaskCircle() {
		return maskCircles.get(maskCircles.size()-1);
	}

	public MaskCircle getNearestMaskCircle(double x, double y) {
		List< Double > ds = getDistanceSqauredToCircles( x, y, maskCircles );
		int minDsId = ds.indexOf( Collections.min( ds ) );
		MaskCircle c = maskCircles.get( minDsId );
		return c;
	}

	private List<Double> getDistanceSqauredToCircles(double x, double y, ArrayList<MaskCircle> cs) {
		double distanceSqaured;
		ArrayList< Double > ret = new ArrayList<>();
		for ( MaskCircle c : cs ) {
			distanceSqaured = Math.pow( (c.x - x), 2 ) + Math.pow( (c.y - y), 2 ); 
			ret.add( distanceSqaured );
		}
		return ret;
	}

	public double getNearestMaskCircleDist(double x, double y) {
		List< Double > ds = getDistanceSqauredToCircles( x, y, maskCircles );
		int minDsId = ds.indexOf( Collections.min( ds ) );
		MaskCircle c = maskCircles.get( minDsId );
		return Math.sqrt( ds.get( minDsId ) );
	}
	
	

	

}


