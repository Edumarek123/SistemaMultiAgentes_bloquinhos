package Simulation;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import javax.swing.JButton;
import java.util.ArrayList;
import javax.swing.SwingWorker;

public class Creatura extends SwingWorker<Object, Object> {
	//CONSTANTES	
	static final int FPS = 100; 

	//TELA
	public JButton corpo;
	private Tela tela;
	private boolean simulando;

	//CARACTERISTICAS
	private int tamanho;
	private boolean mestre;
	private double VELOCIDADE_MAXIMA = 1;

	//FISICA
	private double posicao[];
	private double velocidade[];
	private double aceleracao[];
	private	double teta = 0;


	//BORDAS TELA
	private int top_x;
	private int bot_x;
	private int top_y;
	private int bot_y;


	//CONSTRUTORES
	public Creatura(Tela tela, int id, boolean mestre) {
		this.tamanho = (int)Math.floor(Math.random() * 20 + 10);

		//Corpo
		this.corpo = new JButton("");
		this.corpo.setBorder(null);
		this.corpo.setFocusable(false);
		this.corpo.setBounds(0, 0, this.tamanho, this.tamanho);

		//Cor
		this.corpo.setBackground(new Color(0, 0, 255));
		
		this.tela = tela;
		tela.add(corpo);	

		this.top_x = 0;
		this.bot_x = this.tela.getWidth() - 10;
		this.top_y = 0;
		this.bot_y = this.tela.getHeigth() - 10;

		//Fisica inicial
		this.posicao = new double[2];
		this.velocidade = new double[2];
		this.aceleracao = new double[2];
		this.teta = 0;

		this.posicao[0] = ThreadLocalRandom.current().nextDouble(this.top_x, this.bot_x);
		this.posicao[1] = ThreadLocalRandom.current().nextDouble(this.top_y, this.bot_y);

		for(int i = 0; i < 2; i ++) {
			this.velocidade[i] = 0;
			this.aceleracao[i] = 0;
		}

		//Ganha vida
		this.mestre = mestre;
		this.simulando = true;

		//Mestre
		if(mestre) {
			this.posicao[0] = (this.top_x + this.bot_x) / 2;
			this.posicao[1] = (this.top_y + this.bot_y) / 2;
		}


		this.execute();
	}

	//OVERRIDES
	@Override
	public String doInBackground() {
		while (this.simulando) {
			try {
				if(this.mestre) {
					this.vagar();
				} else {
					double posMestre[] = this.tela.getSimulacao().getMestre().getPosicao();
					int mestreTamanho = this.tela.getSimulacao().getMestre().getTamanho();
					double distanciaMestre = Math.sqrt(Math.pow(posMestre[0] - this.posicao[0], 2) + Math.pow(posMestre[1] - this.posicao[1], 2));

					//Ordem de prioridade de seguimento
					if(distanciaMestre >= 200 + this.tamanho + mestreTamanho) {
						this.entrada(posMestre[0] - this.posicao[0], posMestre[1] - this.posicao[1]);
					} else if(distanciaMestre >= 50 + this.tamanho + mestreTamanho) {
						double velMestre[] = this.tela.getSimulacao().getMestre().getVelocidade();
						this.setVelocidade(velMestre[0] , velMestre[1]);					
					} else if(!this.verificaProximidade()) {
						this.entrada(this.tela.getSimulacao().getMestre().getVelocidade()[0], this.tela.getSimulacao().getMestre().getVelocidade()[1]);
					}
				}
					
				if (this.colisaoBorda() && this.mestre) {
					this.setPosicaoAleatoria(); 

					ArrayList<Creatura> populacao = this.tela.getSimulacao().getPopulacao();
					for(Creatura individuo: populacao)
						individuo.setPosicaoAleatoria(); 
				}

				this.atualizar();
				this.desenhar();
			
				synchronized (this) {
					this.wait(1000 / FPS);		
				}
			} catch (Exception e) {}
		}
		return "";
	}

	@Override
	protected void done() {
		try {

		} catch (Exception ignore) {

		}
	}

	//METODOS
	private void atualizar() {
		//Atualiza velocidade
		this.velocidade[0] += this.aceleracao[0];
		this.velocidade[1] += this.aceleracao[1];

		//Limitador velocidade
		if(this.velocidade[0] > this.VELOCIDADE_MAXIMA && this.velocidade[0] > 0)
			this.velocidade[0] = this.VELOCIDADE_MAXIMA;

		if(velocidade[0] < -this.VELOCIDADE_MAXIMA && this.velocidade[0] < 0)
			this.velocidade[0] = -this.VELOCIDADE_MAXIMA;

		if(velocidade[1] > this.VELOCIDADE_MAXIMA && this.velocidade[1] > 0)
			this.velocidade[1] = this.VELOCIDADE_MAXIMA;

		if(velocidade[1] < -this.VELOCIDADE_MAXIMA && this.velocidade[1] < 0)
			this.velocidade[1] = -this.VELOCIDADE_MAXIMA;

		//Atualiza posicao
		this.posicao[0] += this.velocidade[0];
		this.posicao[1] += this.velocidade[1];

		//Zera aceleracao
		this.aceleracao[0] = 0;
		this.aceleracao[1] = 0;
	}

	public void entrada(double x, double y) {
		this.aceleracao[0] = x;
		this.aceleracao[1] = y;
	}

	public void vagar(){
		double ponto[] = this.velocidade;
		int pontoRaio = this.tamanho;
		double variacao = 0.15;
		this.teta += ThreadLocalRandom.current().nextDouble(-variacao, variacao);

		ponto[0] *= 3;
		ponto[1] *= 3;

		ponto[0] += this.posicao[0] + (pontoRaio * Math.cos(this.teta));
		ponto[1] += this.posicao[1] + (pontoRaio * Math.sin(this.teta));

		ponto[0] -= this.posicao[0];
		ponto[1] -= this.posicao[1];

		double modPonto = Math.sqrt(Math.pow(ponto[0], 2) + Math.pow(ponto[1], 2));
		ponto[0] *= (2 / modPonto);
		ponto[1] *= (2 / modPonto);

		this.entrada(ponto[0], ponto[1]);
	}

	private void desenhar() {
		this.tela.remove(this.corpo);
		this.corpo.setBounds((int)Math.floor(this.posicao[0]), (int)Math.floor(this.posicao[1]), this.tamanho, this.tamanho);
		this.tela.add(this.corpo);
		this.tela.repaint();
	}

	public boolean colisaoBorda() {
		boolean colidiu = false;

		if(this.posicao[0] + this.tamanho >= this.bot_x) {
			this.posicao[0] = this.bot_x - this.tamanho;
			this.velocidade[0] *= -1;
			colidiu = true;
		}
		
		if(this.posicao[0] <= this.top_x) {
			this.posicao[0] = this.top_x;
			this.velocidade[0] *= -1;
			colidiu = true;
		}
		
		if(this.posicao[1] + this.tamanho >= this.bot_y) {
			this.posicao[1] = this.bot_y - this.tamanho;
			this.velocidade[1] *= -1;
			colidiu = true;
		}

		if(this.posicao[1] <= this.top_y) {
			this.posicao[1] = this.top_y;
			this.velocidade[1] *= -1;
			colidiu = true;
		}

		return colidiu;
	}

	public boolean colidiu(double x, double y, double t) {
		if(((this.posicao[0] <= x + t && this.posicao[0] + this.tamanho >= x + t) || (this.posicao[0] + this.tamanho >= x && this.posicao[0] <= x)) 
			&& ((this.posicao[1] + this.tamanho >= y && this.posicao[1] <= y + t) || (this.posicao[1] >= y + this.tamanho && this.posicao[1] + this.tamanho >= y + t)))
			return true;
		return false;	
	}

	public void verificaColisao() {
		ArrayList<Creatura> populacao = this.tela.getSimulacao().getPopulacao();

		for(Creatura individuo: populacao) {
			if(individuo != this && this.colidiu(individuo.getPosicao()[0], individuo.getPosicao()[0], individuo.getTamanho())){
				this.multVelocidade(-1, -1);
				individuo.multVelocidade(-1, -1);
			}
		}
	}

	public void boraInfinita() {
		//X
		if(this.posicao[0] > this.bot_x + this.tamanho)
			this.posicao[0] = this.top_x;
		else if(this.posicao[0] < this.top_x)
			this.posicao[0] = this.bot_x - this.tamanho;

		//Y
		if(this.posicao[1] > this.bot_y + this.tamanho)
			this.posicao[1] = this.top_y;
		else if(this.posicao[1] < this.top_y)
			this.posicao[1] = this.bot_y - this.tamanho;

	}

	public boolean verificaProximidade() {
		boolean proximoAEscravo = false;
		ArrayList<Creatura> populacao = this.tela.getSimulacao().getPopulacao();

		for(Creatura individuo: populacao) {
			if(individuo != this && individuo != this.tela.getSimulacao().getMestre()){
				double posIndividuo[] = individuo.getPosicao();
				double distanciaEntre = Math.sqrt(Math.pow(posIndividuo[0] - this.posicao[0], 2) + Math.pow(posIndividuo[1] - this.posicao[1], 2));

				if(distanciaEntre <= 80 + this.tamanho + individuo.getTamanho()) {
					this.entrada(-individuo.getAceleracao()[0], -individuo.getAceleracao()[1]);
					individuo.entrada(-this.aceleracao[0], -this.aceleracao[1]);
					
					proximoAEscravo = true;
				}
			}
		}
		return proximoAEscravo;
	}

	//SETs & GETs
	public void setSimulando(boolean simulando) {this.simulando = simulando;}
	
	public void setMestre(boolean mestre) {this.mestre = mestre;}

	public void setPosicao(double x, double y) {
		this.posicao[0] = x;
		this.posicao[1] = y;		
	}

	public void setPosicaoAleatoria() {
		this.posicao[0] = ThreadLocalRandom.current().nextDouble(this.top_x, this.bot_x);
		this.posicao[1] = ThreadLocalRandom.current().nextDouble(this.top_y, this.bot_y);		
	}
	
	public void setVelocidade(double x, double y) {
		this.velocidade[0] = x;
		this.velocidade[1] = y;		
	}

	public void setAceleracao(double x, double y) {
		this.aceleracao[0] = x;
		this.aceleracao[1] = y;		
	}

	public void setVelocidadeMaxima(double velocidade) {this.VELOCIDADE_MAXIMA = velocidade;}

	public void multVelocidade(double x, double y) {
		this.velocidade[0] *= -x;
		this.velocidade[1] *= -y;
	}

	public int getTamanho() {return this.tamanho;}

	public double[] getPosicao() {return this.posicao;}

	public double[] getVelocidade() {return this.velocidade;}

	public double[] getAceleracao() {return this.aceleracao;}
}
