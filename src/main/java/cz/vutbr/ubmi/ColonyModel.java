package cz.vutbr.ubmi;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

public class ColonyModel {
	
	RandomAccessibleInterval<?> img;
	ColonyView view;
	
	public <T extends RealType> ColonyModel(RandomAccessibleInterval<T> img,ColonyView view) {
		this.img=img;
		this.view=view;
		
	
	}

}
