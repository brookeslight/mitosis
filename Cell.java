package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cell {
	
	private float x;
	private float y;
	private float size;
	private float velX;
	private float velY;
	private int spinX;
	private int spinY;
	private double k;
	private Color color;
	
	private CopyOnWriteArrayList<Cell> cells;
	
	public Cell(float x, float y, float size, CopyOnWriteArrayList<Cell> cells) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.cells = cells;
		this.velX = 0;
		this.velY = 0;
		this.spinX = (Math.random() > 0.5) ? -1 : 1;
		this.spinY = (Math.random() > 0.5) ? -1 : 1;
		this.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
		this.k = Math.random();
	}
	
	public void tick() {
		this.size += 0.025f;
		if(Math.random() > 0.9975) { //1% change
			this.cells.remove(this);
			if(this.cells.size() <= 50) {
				this.cells.add(new Cell(this.x + (this.size/2), this.y + (this.size/2), this.size/2, this.cells));
				this.cells.add(new Cell(this.x - (this.size/2), this.y - (this.size/2), this.size/2, this.cells));
			}
		}
		//
		this.velX = (float) (this.spinX * Math.cos(this.k*this.size));
		this.velY = (float) (this.spinY * Math.sin(this.k*this.size));
		this.x += this.velX;
		this.y += this.velY;
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(this.color);
		g2d.setStroke(new java.awt.BasicStroke(this.size/5));
		g2d.drawOval((int)(this.x - this.size), (int)(this.y - this.size), (int)(2*this.size), (int)(2*this.size));
	}
	
}