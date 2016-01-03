package model;

import javafx.scene.control.CheckBox;

public class CuotesInsureTable {
	IbCuotesInsure ic = new IbCuotesInsure();
	CheckBox ck;

	public IbCuotesInsure getIc() {
		return ic;
	}

	public void setIc(IbCuotesInsure ic) {
		this.ic = ic;
	}

	public CheckBox getCk() {
		return ck;
	}

	public void setCk(CheckBox ck) {
		this.ck = ck;
	}

}
