package Simulation;

import javax.swing.JPanel;
import java.util.ArrayList;
import Simulation.Creatura;
import java.awt.Color;


public class Simulacao extends JPanel {
	//ATRIBUTOS
	private int tamPopulacao = 9;
	private int width;
	private int height;
	private ArrayList<Creatura> populacao;
	private Creatura mestre;

	//CONSTRUTORES
	public Simulacao(int width, int height, Tela tela) {
		this.width = width;
		this.height = height;
		
		this.mestre = new Creatura(tela, 10, true);
		this.mestre.setMestre(true);
		this.mestre.setPosicao(this.width / 2, this.height / 2);
		this.mestre.corpo.setBackground(new Color(255, 140, 0));


		this.populacao = new ArrayList<Creatura>();
		for(int i = 0; i < this.tamPopulacao; i++)
			this.populacao.add(new Creatura(tela, i+1, false));

	}

	//SETs & GETs
	public ArrayList<Creatura> getPopulacao() {return this.populacao;} 

	public Creatura getMestre() {return this.mestre;} 
}
