package cz.vutbr.ubmi;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.scijava.Context;

import com.indago.util.ImglibUtil;

import bdv.ui.panel.BigDataViewerUI;
import bdv.util.AxisOrder;
import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandlePanel;
import bdv.util.BdvOptions;
import bdv.util.BdvOverlay;
import bdv.util.BdvSource;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;


public class ColonyView< T extends RealType< T >>  extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */

	public final BdvHandlePanel bdv;
	public final BdvSource source;
	public BigDataViewerUI bdvUI;
	public MaskPanel maskPanel;
	public ColonyController controller;

	public ColonyView(ColonyModel model, OpService opService) {
		
		model.setView(this);
		
		bdv= new BdvHandlePanel( null, Bdv.options().is2D() );
		source = BdvFunctions.show(model.img , "img", Bdv.options().addTo( bdv ).axisOrder(AxisOrder.XYC));
		
		BigDataViewerUI bdvUI = createBDV(opService.context());
		bdvUI.switch2D(true);
		bdvUI.addImage(Views.hyperSlice(model.img, 2, 0), "R", Color.RED);
		bdvUI.addImage(Views.hyperSlice(model.img, 2, 1), "G", Color.GREEN);
		bdvUI.addImage(Views.hyperSlice(model.img, 2, 2), "B", Color.BLUE);

		
		maskPanel=new MaskPanel();
		bdvUI.addCard(new JLabel("Create Mask"),false,maskPanel);
		
		
//		final overlayMask overlay = new OverlayMask(model.maskCircles);
		
		
//		bdvUI.addOverlay(overlay, "test overlay");
		


	}
	public void setController(ColonyController controller) {
		this.controller=controller;
	}
	
	
	
	private class MaskPanel extends JPanel{
		
		JButton loadDataForMaskBtn;
		JToggleButton drawMaskTBtn;
		JButton saveMaskBtn;
		JButton loadMaskBtn;
		
		public MaskPanel()
		{
			
	
			setLayout(new GridLayout(2,2,2,2));
			Dimension d=this.getSize();
			
			loadDataForMaskBtn=new JButton("Load data for mask");
			add(loadDataForMaskBtn);
			
			drawMaskTBtn=new JToggleButton("Draw mask");
			add(drawMaskTBtn);
			
			saveMaskBtn=new JButton("Save mask");
			add(saveMaskBtn);
			
			loadMaskBtn=new JButton("Load mask");
			add(loadMaskBtn);
			
			
			
			
		}
	}
	
	
	private BigDataViewerUI createBDV(Context ctx) {
		final JFrame frame = new JFrame("Blob Detection");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final BigDataViewerUI bdvUI = new BigDataViewerUI<>(frame, ctx,BdvOptions.options().preferredSize(800, 800));
		frame.add(bdvUI.getPanel());
		frame.pack();
		frame.setVisible(true);
		return bdvUI;
	}
	
	
	
	public BigDataViewerUI getBigDataViewerUI() {
		return this.bdvUI;
	}

}