package com.example.keyring;

public class Secret {

	private String title;
	private boolean isChecked;
	
	public Secret(String title, boolean isChecked) {
		super();
		this.title = title;
		this.isChecked = isChecked;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
