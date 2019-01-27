package cz.vutbr.ubmi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import bdv.util.BdvOverlay;
import cz.vutbr.ubmi.ColonyModel.MaskCircle;
import net.imglib2.RealPoint;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.util.LinAlgHelpers;

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
		
		for (MaskCircle cl : cs) {
			MaskCircle cg=l2g(cl,t);
			
			g.setColor(Color.RED);
			
			g.drawOval((int)(cg.x-cg.r), (int)(cg.y-cg.r), (int)(cg.r*2), (int)(cg.r*2));
			
			g.drawOval((int)(cg.x-1), (int)(cg.y-1), (int)(2), (int)(2));
			
			
		}

		
		
	}
	
	
	private MaskCircle l2g(MaskCircle c,AffineTransform2D t) {
		double[] lPos = new double[ 2 ];
		double[] gPos = new double[ 2 ];
		
		RealPoint planarPoint = new RealPoint( c.x, c.y );
		planarPoint.localize( lPos );
		t.apply( lPos, gPos );
		
		double t0= t.get(0, 0);
		double t1= t.get(0, 1);
		
		
		
		double s=Math.sqrt(Math.pow(t0,2)+Math.pow(t1,2));
		
		double r= (c.r*s);
		double x=gPos[0];
		double y=gPos[1];
		
		
		
		MaskCircle cc=new MaskCircle(x, y,r);
		
		
		return cc;
	
	}
	
}
				
				
				
	
	

