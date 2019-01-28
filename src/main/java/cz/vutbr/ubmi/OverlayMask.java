package cz.vutbr.ubmi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import bdv.util.BdvOverlay;
import cz.vutbr.ubmi.ColonyModel.MaskCircle;
import net.imglib2.RealPoint;
import net.imglib2.realtransform.AffineTransform2D;


public class OverlayMask extends BdvOverlay {
	
	ColonyModel model;
	ColonyView view;
	
	public OverlayMask(ColonyModel model) {
		this.model=model;
		this.view=model.view;
	}
	

	@Override
	protected void draw( final Graphics2D g ) {
		
		ArrayList<MaskCircle> cs=model.maskCircles;

		final AffineTransform2D t = new AffineTransform2D();
		getCurrentTransform2D( t );
		
		
		for (MaskCircle cg : cs) {
			
//			if (cg.t==info.getTimePointIndex())
//			{
			
				MaskCircle cl=g2l(cg,t);
				
				g.setColor(Color.RED);
				
				g.drawOval((int)(cl.x-cl.r), (int)(cl.y-cl.r), (int)(cl.r*2), (int)(cl.r*2));
				
				g.drawOval((int)(cl.x-1), (int)(cl.y-1), (int)(2), (int)(2));
			
//			}
		}

		
		
	}
	
	
	private MaskCircle g2l(MaskCircle c,AffineTransform2D t) {
		double[] gPos = new double[ 2 ];
		double[] lPos = new double[ 2 ];
		
		RealPoint planarPoint = new RealPoint( c.x, c.y );
		planarPoint.localize( gPos );
		t.apply( gPos, lPos );
		
		double t0= t.get(0, 0);
		double t1= t.get(0, 1);
		
		
		
		double s=Math.sqrt(Math.pow(t0,2)+Math.pow(t1,2));
		
		double r= (c.r*s);
		double x=lPos[0];
		double y=lPos[1];
		
		
		
		MaskCircle cc=new MaskCircle(x, y,r);
		
		
		return cc;
	
	}
	
}
				
				
				
	
	

