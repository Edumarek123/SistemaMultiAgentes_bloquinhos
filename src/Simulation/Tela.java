package Simulation;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Tela extends JFrame implements ActionListener {
	//ATRIBUTOS
	private  Simulacao simulacao;
	private int width;
	private int height;

	//CONSTRUTORES
	public Tela(int width, int height) {
		this.width = width;
		this.height = height;

		this.simulacao = new Simulacao(this.width, this.height, this);

		this.simulacao.setPreferredSize(new Dimension(this.width, this.height));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(this.simulacao);	
		this.setResizable(false);
		this.simulacao.setBackground(new Color(0, 0, 0));
		this.pack();

		this.setExtendedState(200);
		this.setVisible(true);
	}

	//OVERRIDES
	@Override
	public void actionPerformed(ActionEvent evento) {}

	//SETs & GETs
	public int getWidth() {return this.width;}
	public int getHeigth() {return this.height;}
	public Simulacao getSimulacao() {return this.simulacao;}

}
