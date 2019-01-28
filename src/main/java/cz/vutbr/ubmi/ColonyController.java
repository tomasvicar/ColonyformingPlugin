package cz.vutbr.ubmi;


import java.io.File;
import java.util.ArrayList;


import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.DragBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;



import cz.vutbr.ubmi.ColonyModel.MaskCircle;
import ij.io.Opener;

import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.ValuePair;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class ColonyController< T extends RealType< T >> {
	
	public ColonyModel< T > model;
	public ColonyView< T > view;
	public OpService opService;
	
	public ColonyController(ColonyModel< T > model,ColonyView< T > view, OpService opService) {
		this.model=model;
		this.view=view;
		this.opService=opService;
		model.setController(this);
		view.setController(this);
		initBehavior();
	}
	
	
	public void initBehavior(){
		Behaviours behaviours = new Behaviours( new InputTriggerConfig() );
		
	
		
		behaviours.install( view.bdv.getTriggerbindings(), "my-new-behaviours" );
		
		
		behaviours.behaviour( new DragCircle(), "left drag", "button1" );
		behaviours.behaviour( new DragCircleRight(), "rigth drag", "button3" );
		behaviours.behaviour( new DeleteCircle(), "delete", "DELETE" );
		behaviours.behaviour( new EnlargeCircle(), "plus", "ADD" );
		behaviours.behaviour( new ReduceCircle(), "minus", "SUBTRACT" );
		behaviours.behaviour( new ReduceCircle(), "minus", "SUBTRACT" );
		behaviours.behaviour( new CopyCircle(), "c", "C" );
		
	 
	}
	
	
	private class CopyCircle implements ClickBehaviour{
		private RealPoint pos = new RealPoint( 3 );
		private double d;
		private MaskCircle nearestC;
		@Override
		public void click(int x, int y) {
			view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
			double xx=pos.getDoublePosition(0);
			double yy=pos.getDoublePosition(1);
			
			nearestC=model.getNearestMaskCircle(xx, yy);
			d=model.getNearestMaskCircleDist(xx, yy);
			if (d<100){
				model.maskCircles.add(nearestC.copy());
				MaskCircle c = model.getLastMaskCircle();
				c.x=c.x+10;
				c.y=c.y+10;
				view.updateOverlayMask();
			}
			
		}
		
	}
	
	
	
	
	private class DeleteCircle implements ClickBehaviour{
		private RealPoint pos = new RealPoint( 3 );
		private double d;
		private MaskCircle nearestC;
		@Override
		public void click(int x, int y) {
			view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
			double xx=pos.getDoublePosition(0);
			double yy=pos.getDoublePosition(1);
			
			nearestC=model.getNearestMaskCircle(xx, yy);
			d=model.getNearestMaskCircleDist(xx, yy);
			if (d<100)
				model.maskCircles.remove(nearestC);
			view.updateOverlayMask();
			
		}
		
	}
	
	private class EnlargeCircle implements ClickBehaviour{
		private RealPoint pos = new RealPoint( 3 );
		private double d;
		private MaskCircle nearestC;
		@Override
		public void click(int x, int y) {
			view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
			double xx=pos.getDoublePosition(0);
			double yy=pos.getDoublePosition(1);
			
			nearestC=model.getNearestMaskCircle(xx, yy);
			d=model.getNearestMaskCircleDist(xx, yy);
			if (d<100)
				nearestC.r=nearestC.r+1;
			view.updateOverlayMask();
			
		}
		
	}
	
	private class ReduceCircle implements ClickBehaviour{
		private RealPoint pos = new RealPoint( 3 );
		private double d;
		private MaskCircle nearestC;
		@Override
		public void click(int x, int y) {
			view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
			double xx=pos.getDoublePosition(0);
			double yy=pos.getDoublePosition(1);
			
			nearestC=model.getNearestMaskCircle(xx, yy);
			d=model.getNearestMaskCircleDist(xx, yy);
			if (d<100)
				nearestC.r=nearestC.r-1;
			view.updateOverlayMask();
			
		}
		
	}
	
	
	private class DragCircle implements DragBehaviour{
		public ValuePair<Double, Double> startPosition;
		public RealPoint pos = new RealPoint( 3 );
		
		@Override
		public void init(int x, int y) {
			if (view.maskPanel.drawMaskTBtn.isSelected()) {

				view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
				double xx=pos.getDoublePosition(0);
				double yy=pos.getDoublePosition(1);
				
				startPosition=new ValuePair<Double, Double>(xx,yy);
	
				model.maskCircles.add(new MaskCircle((int) (xx+startPosition.getA())/2,(int) (yy+startPosition.getB())/2,1) );
				view.updateOverlayMask();
			}
		}

		@Override
		public void drag(int x, int y) {
			if (view.maskPanel.drawMaskTBtn.isSelected()) {
				view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
				double xx=pos.getDoublePosition(0);
				double yy=pos.getDoublePosition(1);
				
				
				MaskCircle c =  model.getLastMaskCircle();
				c.x=(xx+startPosition.getA())/2;
				c.y=(yy+startPosition.getB())/2;
				c.r=dist(xx,yy,startPosition.getA(),startPosition.getB())/2;
				view.updateOverlayMask();
			}
					
		}

		@Override
		public void end(int x, int y) {
			if (view.maskPanel.drawMaskTBtn.isSelected()) {
				view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
				double xx=pos.getDoublePosition(0);
				double yy=pos.getDoublePosition(1);
				
				
				MaskCircle c =  model.getLastMaskCircle();
				c.x=(xx+startPosition.getA())/2;
				c.y=(yy+startPosition.getB())/2;
				c.r=dist(xx,yy,startPosition.getA(),startPosition.getB())/2;
				view.updateOverlayMask();
			}
		}
			
		
	}
	
	private double dist(double x1,double y1,double x2,double y2) {
		
		return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		
	}
	
	
	private class DragCircleRight implements DragBehaviour{
		private ValuePair<Double, Double> startPosition;
		private RealPoint pos = new RealPoint( 3 );
		private double d;
		private MaskCircle nearestC;
		
		private double cxOrig;
		private double cyOrig;
		
		@Override
		public void init(int x, int y) {
			if (view.maskPanel.drawMaskTBtn.isSelected()) {
				view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
				double xx=pos.getDoublePosition(0);
				double yy=pos.getDoublePosition(1);
				
				nearestC=model.getNearestMaskCircle(xx, yy);
				d=model.getNearestMaskCircleDist(xx, yy);
				if (d>=100)
					nearestC=new MaskCircle(1,1, 1);
						
				
				startPosition=new ValuePair<Double, Double>(xx,yy);
				
				cxOrig=nearestC.x;
				cyOrig=nearestC.y;
			}

		}

		@Override
		public void drag(int x, int y) {
			if (view.maskPanel.drawMaskTBtn.isSelected()) {
				view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
				double xx=pos.getDoublePosition(0);
				double yy=pos.getDoublePosition(1);
				
				nearestC.x=(cxOrig+xx-startPosition.getA());
				nearestC.y=(cyOrig+yy-startPosition.getB());
				view.updateOverlayMask();
			}
					
		}

		@Override
		public void end(int x, int y) {
			if (view.maskPanel.drawMaskTBtn.isSelected()) {
			
				view.bdv.getViewerPanel().displayToGlobalCoordinates( x, y, pos );
				double xx=pos.getDoublePosition(0);
				double yy=pos.getDoublePosition(1);
				
				
				nearestC.x=(cxOrig+xx-startPosition.getA());
				nearestC.y=(cyOrig+yy-startPosition.getB());
				view.updateOverlayMask();
			}
		}
			
	}

	public void loadDataForMaskBtnAction() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle( "Load Data for Mask: " );
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "*.tif", "tif" );
		jfc.setFileFilter( filter );
		jfc.addChoosableFileFilter(filter);
		int returnValue = jfc.showOpenDialog( null );
		if ( returnValue == JFileChooser.APPROVE_OPTION ) {
			File file = jfc.getSelectedFile();

			String path = file.getAbsolutePath();


			RandomAccessibleInterval< T > img = (RandomAccessibleInterval<T>) ImagePlusAdapter.wrap(new Opener().openImage(path));
			model.img=img;
			view.updateImage();


		}


	}


	public void saveMaskBtnAction() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle( "Save mask csv: " );
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "*.csv", "csv" );
		jfc.setFileFilter( filter );
		jfc.addChoosableFileFilter(filter);
		int returnValue = jfc.showSaveDialog( null );
		if ( returnValue == JFileChooser.APPROVE_OPTION ) {
			File file = jfc.getSelectedFile();

			String fileName = file.getAbsolutePath();

			if (!(fileName.substring(fileName.length()-4).equals(".csv")))
				fileName=fileName.concat(".csv");
			
			CSVMaskReaderWriter.writeCsv(fileName, model.maskCircles);
		}
		
	}


	public void loadMaskBtnAction() {
		
		
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle( "Load mask csv: " );
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "*.csv", "csv" );
		jfc.setFileFilter( filter );
		jfc.addChoosableFileFilter(filter);
		int returnValue = jfc.showOpenDialog( null );
		if ( returnValue == JFileChooser.APPROVE_OPTION ) {
			File file = jfc.getSelectedFile();

			String fileName = file.getAbsolutePath();
			
			model.maskCircles=CSVMaskReaderWriter.loadCsv(fileName);
			view.updateOverlayMask();
		}
		
	}


	public void selectAddDataBtnAction() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle( "Load aditional data (without mask template) to process: " );
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "*.tif", "tif" );
		jfc.setFileFilter( filter );
		jfc.addChoosableFileFilter(filter);
		jfc.setMultiSelectionEnabled(true);
		int returnValue = jfc.showOpenDialog( null );
		if ( returnValue == JFileChooser.APPROVE_OPTION ) {
			
			ArrayList<RandomAccessibleInterval< T >> imgList=new ArrayList<RandomAccessibleInterval< T >>();
			
			imgList.add(Views.hyperSlice(model.img, 3, 0));
			
			File[] files = jfc.getSelectedFiles();
			for (File file : files) {
				String path = file.getAbsolutePath();
				RandomAccessibleInterval< T > img = (RandomAccessibleInterval<T>) ImagePlusAdapter.wrap(new Opener().openImage(path));
				imgList.add(img);
			}
			

			
			RandomAccessibleInterval< T > imgStack=Views.stack(imgList);


			model.img=imgStack;
			

	        ArrayImgFactory<FloatType> fac = new ArrayImgFactory<>(new FloatType());
	        Img<FloatType> grayImg = fac.create(new FinalDimensions(model.img.dimension(0), model.img.dimension(1),model.img.dimension(3)));
			
			
			Cursor<T> cr = Views.hyperSlice(imgStack, 2, 0).cursor();
			Cursor<T> cg = Views.hyperSlice(imgStack, 2, 1).cursor();
			Cursor<T> cb = Views.hyperSlice(imgStack, 2, 2).cursor();
			Cursor<FloatType> cgray = Views.iterable(grayImg).cursor();
			while ( cr.hasNext()) {
				cr.fwd();
				cg.fwd();
				cb.fwd();
				cgray.fwd();
				
				cgray.get().set(new FloatType(0.299f*cr.get().getRealFloat() + 0.587f*cg.get().getRealFloat() + 0.114f*cb.get().getRealFloat()));
	        }
			model.grayimg=(RandomAccessibleInterval<T>) grayImg;

			view.updateImage();


		}
		
	}


	public void alignBtnAction() {
		
//		
//		RandomAccessibleInterval< T > slice0 =Views.hyperSlice(model.grayimg, 3, 0);
//		for (int i=1; i<model.grayimg.dimension(3); i++) {
//			RandomAccessibleInterval< T > slice =Views.hyperSlice(model.grayimg, 3, i);
//			
//					
//			
//			
//		}
			
		
	}
	
	
	
	
}
