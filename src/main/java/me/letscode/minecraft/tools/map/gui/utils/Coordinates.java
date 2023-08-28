package me.letscode.minecraft.tools.map.gui.utils;

public class Coordinates {

	private int x;
	private int y;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	
	public int getX() {
		return x;
	}
	
	public Coordinates setX(int x) {
		this.x = x;
		return this;
	}
	
	public int getY() {
		return y;
	}
	
	public Coordinates setY(int y) {
		this.y = y;
		return this;
	}
	
	public Coordinates sub(int x, int y){
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Coordinates add(int x, int y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	
	public Coordinates sub(Coordinates coords){
		this.x -= coords.getX();
		this.y -= coords.getY();
		return this;
	}
	
	public Coordinates add(Coordinates coords){
		this.x += coords.getX();
		this.y += coords.getY();
		return this;
	}
	
	public Coordinates mul(double x, double y){
		this.x *= x;
		this.y *= y;
		return this;
	}
	
	
	public Coordinates zero(){
		this.x = 0;
		this.y = 0;
		return this;
	}
	
	public Coordinates clone(){
		return new Coordinates(this.x, this.y);
	}
	
}
