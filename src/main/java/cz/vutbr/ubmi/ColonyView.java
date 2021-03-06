package cz.vutbr.ubmi;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.scijava.Context;

import com.indago.util.ImglibUtil;


import bdv.util.AxisOrder;
import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandlePanel;
import bdv.util.BdvOptions;
import bdv.util.BdvOverlay;
import bdv.util.BdvSource;
import bdv.util.BdvStackSource;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.miginfocom.swing.MigLayout;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

//	public BigDataViewerUI bdvUI;
	public MaskPanel maskPanel;
	public ColonyController controller;
	public OverlayMask overlayMask ;
	public ColonyModel model;
	private EventHandler eventHandler;
	public AlignPanel alignPanel;
	public BdvHandlePanel bdv;
	public OpService opService;
	public BdvStackSource<ARGBType> source ;

	public ColonyView(ColonyModel model, OpService opService) {
		
		this.model=model;
		this.opService=opService;
		model.setView(this);
		
		eventHandler=new EventHandler();
		
		
		
		
		
//		bdvUI = createBDV(opService.context());
        JFrame frame = new JFrame("BDV-UI");
        frame.setBounds(50, 50, 1200, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setLayout(new MigLayout("fillx, filly, ins 0", "[grow]", "[grow]"));

        bdv = new BdvHandlePanel( frame, Bdv.options().axisOrder(AxisOrder.XYC).is2D() );
        frame.pack();
        
        
		
		
		
//		bdvUI.switch2D(true);
		updateImage();

		
		maskPanel=new MaskPanel();
		bdv.addNewCard("Create Mask",false,maskPanel);
		
		alignPanel=new AlignPanel();
		bdv.addNewCard("Align Images",false,alignPanel);
		
		
		overlayMask = new OverlayMask(model);
		
		
		


	}
	
	public void updateImage() {     

		
		
		
		
		
        ArrayImgFactory<ARGBType> fac = new ArrayImgFactory<>(new ARGBType());
        
        Img<ARGBType> rgbImg = fac.create(new FinalDimensions(model.img.dimension(0), model.img.dimension(1),model.img.dimension(3)));
        
        RandomAccess<T> ra = model.img.randomAccess();
        Cursor<ARGBType> rgbC = Views.flatIterable(rgbImg).cursor();
        
        int[] pos = new int[4];
        while (rgbC.hasNext()) {
        	rgbC.fwd();
        	pos[0] = rgbC.getIntPosition(0);
        	pos[1] = rgbC.getIntPosition(1);
        	pos[3] = rgbC.getIntPosition(2);
        	pos[2] = 0;
        	ra.setPosition(pos);
        	float r = ra.get().getRealFloat();
        	pos[2] = 1;
        	ra.setPosition(pos);
        	float g = ra.get().getRealFloat();
        	pos[2] = 2;
        	ra.setPosition(pos);
        	float b = ra.get().getRealFloat();
        	
        	rgbC.get().set(new ARGBType(ARGBType.rgba(r/255f, g/255f, b/255f, 1)));
        }
        
        if (source!= null)
        	source.removeFromBdv();
        
        source = BdvFunctions.show(rgbImg,"fdsfd",Bdv.options().addTo(bdv).axisOrder(AxisOrder.XYT));
        
        
	}
	
	
	
	
	
	public void setController(ColonyController controller) {
		this.controller=controller;
	}
	
	public void updateOverlayMask(){
		BdvFunctions.showOverlay(overlayMask, "test overlay", Bdv.options().addTo(bdv));
	}
	
	
	
	
	
	class MaskPanel extends JPanel{
		
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
			loadDataForMaskBtn.addActionListener(eventHandler);
			
			drawMaskTBtn=new JToggleButton("Draw mask");
			add(drawMaskTBtn);
			
			saveMaskBtn=new JButton("Save mask");
			add(saveMaskBtn);
			saveMaskBtn.addActionListener(eventHandler);
			
			loadMaskBtn=new JButton("Load mask");
			add(loadMaskBtn);
			loadMaskBtn.addActionListener(eventHandler);
			
		}
	}
	
	
	
	class AlignPanel extends JPanel{
		
		JButton selectAddDataBtn;
		JButton alignBtn;
		JButton divideBtn;
		
		public AlignPanel()
		{

			setLayout(new GridLayout(3,1,2,2));
			Dimension d=this.getSize();
			
			selectAddDataBtn=new JButton("Select additional data");
			add(selectAddDataBtn);
			selectAddDataBtn.addActionListener(eventHandler);
			
			alignBtn=new JButton("Align");
			add(alignBtn);
			alignBtn.addActionListener(eventHandler);
			
			divideBtn=new JButton("Divide");
			add(divideBtn);
			divideBtn.addActionListener(eventHandler);
			
		}
	}
	
	
	 
//	private BigDataViewerUI createBDV(Context ctx) {
//		final JFrame frame = new JFrame("Blob Detection");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		final BigDataViewerUI bdvUI = new BigDataViewerUI<>(frame, ctx,BdvOptions.options().preferredSize(800, 800));
//		frame.add(bdvUI.getPanel());
//		frame.pack();
//		frame.setVisible(true);
//		return bdvUI;
//		
//	}
	
	

	
//	public BigDataViewerUI getBigDataViewerUI() {
//		return this.bdvUI;
//	}
	
	
	public class EventHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()==maskPanel.loadDataForMaskBtn){
				controller.loadDataForMaskBtnAction();
			}
			if(e.getSource()==maskPanel.saveMaskBtn){
				controller.saveMaskBtnAction();
			}
			if(e.getSource()==maskPanel.loadMaskBtn){
				controller.loadMaskBtnAction();
			}
			
			
			
			if(e.getSource()==alignPanel.selectAddDataBtn){
				controller.selectAddDataBtnAction();
			}
			
			
			
		}
		

		
	}

}