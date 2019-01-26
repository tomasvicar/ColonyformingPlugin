package cz.vutbr.ubmi;

public class ColonyController {
	
	public ColonyModel model;
	public ColonyView view;
	
	public ColonyController(ColonyModel model,ColonyView view) {
		this.model=model;
		this.view=view;
		model.setController(this);
		view.setController(this);
	}
	
	public void initBehavior(){
		
		
		
	}

	
	
}
