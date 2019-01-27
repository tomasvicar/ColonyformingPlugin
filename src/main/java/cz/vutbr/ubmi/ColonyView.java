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
import net.miginfocom.swing.MigLayout;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	public ColonyView(ColonyModel model, OpService opService) {
		
		this.model=model;
		model.setView(this);
		
		eventHandler=new EventHandler();
		
		
		
		
		
//		bdvUI = createBDV(opService.context());
        JFrame frame = new JFrame("BDV-UI");
        frame.setBounds(50, 50, 1200, 900);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setLayout(new MigLayout("fillx, filly, ins 0", "[grow]", "[grow]"));

        bdv = new BdvHandlePanel( frame, Bdv.options() );
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
//		bdvUI.removeAll();
//        bdvUI.addImage(Views.hyperSlice(model.img, 2, 0), "R", Color.RED);
//        bdvUI.addImage(Views.hyperSlice(model.img, 2, 1), "G", Color.GREEN);
//        bdvUI.addImage(Views.hyperSlice(model.img, 2, 2), "B", Color.BLUE);
//        
//        

        BdvFunctions.show(model.img,"fdsfd",Bdv.options().addTo(bdv).axisOrder(AxisOrder.XYCT));
        
        
        
        
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