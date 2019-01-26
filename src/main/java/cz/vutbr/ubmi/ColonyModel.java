package cz.vutbr.ubmi;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

public class ColonyModel<T extends RealType>  {
	
	RandomAccessibleInterval<T> img;
	ColonyView view;
	
	public ColonyModel(RandomAccessibleInterval<T> img) {
		this.img=img;
	}
	
	public void setView(ColonyView view) {
		this.view=view;
	}
}
