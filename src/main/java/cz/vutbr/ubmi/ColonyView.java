package cz.vutbr.ubmi;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.scijava.Context;

import com.indago.util.ImglibUtil;

import bdv.util.AxisOrder;
import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandlePanel;
import bdv.util.BdvSource;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;


public class ColonyView extends JPanel {


	/**
	 * Create the panel.
	 */
	
	public final BdvHandlePanel bdv;
	public final BdvSource source;
	
	public < T extends RealType< T >> ColonyView(RandomAccessibleInterval<T> img, Context context, OpService opService) {
				
		bdv= new BdvHandlePanel( null, Bdv.options().is2D() );
		source = BdvFunctions.show( img , "img", Bdv.options().addTo( bdv ).axisOrder(AxisOrder.XYC));
		context.inject( this );
		
		
		
		source.setDisplayRangeBounds( 0, 10000);
		source.setDisplayRange( 0,10000);
		
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		
		JButton btnNewButton = new JButton("New button");
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);;
		
		panel_1.setLayout(new BorderLayout());
		panel_1.add(bdv.getViewerPanel(),BorderLayout.CENTER);
		

		
		
	}
}