package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7604959185792845431L;
	private boolean running;
	private Thread thread;
	private CopyOnWriteArrayList<Cell> cells;

	public static void main(String[] args) {
		new Main().start();
	}
	
	public synchronized void start() {
		if(this.running == true) {
			return;
		}
		this.thread = new Thread(this);
		this.thread.start();
		this.running = true;
	}
	
	public synchronized void stop() {
		this.running = false;
		//clean up
	}
	
	private void init() {
		JFrame frame = new JFrame("2D Game");
		frame.setSize(1920, 1080);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		this.requestFocus();
		//
		this.cells = new CopyOnWriteArrayList<Cell>();
		for(int i = 0; i < 1; i++) {
			//creates new cell in random location with random size
			this.cells.add(new Cell(this.getWidth()/2, this.getHeight()/2, 10, this.cells));
		}
	}
	
	@Override
	public void run() {
		this.init();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(this.running == true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				this.tick();
				updates++;
				delta--;
			}
			this.render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		System.exit(0);
	}
	
	private void tick() {
		for(Cell c: this.cells) {
			c.tick();
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform af = g2d.getTransform();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//start draw
			//bg
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
			//cells
		for(Cell c: this.cells) {
			c.render(g2d);
		}
		//end draw
		g2d.setTransform(af);
		g.dispose();
		bs.show();
	}

}