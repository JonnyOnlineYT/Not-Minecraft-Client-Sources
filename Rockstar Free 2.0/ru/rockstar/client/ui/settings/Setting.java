package ru.rockstar.client.ui.settings;

import java.util.function.Supplier;

public class Setting extends Configurable {

    protected String name;
    private double min;
    private double max;
    private String mode;
    private boolean bval;
    private boolean onlyint = false;
	private double dval;
	private String sval;
    protected Supplier<Boolean> visible;

    public boolean isVisible() {
        return visible.get();
    }

    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }
    
    public String getValString(){
		return this.sval;
	}
	
	public void setValString(String in){
		this.sval = in;
	}
	public void setValDouble(double in){
		this.dval = in;
	}
	
    public int getValInt() {
        if (this.onlyint) {
            this.dval = (int) dval;
        }
        return (int) this.dval;
    }
	
    public void setValFloat(float in) {
        this.dval = in;
    }

	 public boolean isMass() {
        return this.mode.equalsIgnoreCase("Mass");
    }
	 
	 public boolean getValBoolean(){
			return this.bval;
		}
		
		public void setValBoolean(boolean in){
			this.bval = in;
		}
		
		public double getValDouble(){
			if(this.onlyint){
				this.dval = (int)dval;
			}
			return this.dval;
		}

	 public double getMin(){
			return this.min;
		}
		
		public double getMax(){
			return this.max;
		}
}
