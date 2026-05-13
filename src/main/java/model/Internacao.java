package model;

public class Internacao {

	private Leito leito;
	private int qtdeDias;

	public Internacao(Leito leito, int qtdeDias) {
		this.leito = leito;
		this.qtdeDias = qtdeDias;
	}

	// metodo para contabilizar diaria,Internacao precisa se comunicar diretamente com leito
	public float contabilizaDiaria(){
		return leito.getValorLeito(qtdeDias);
	}

	// metodo para pegar tipo de leito, Internacao precisa se comunicar diretamente com leito
	public String getTipoLeito(){
		return leito.getTipoLeito();
	}

	public String relatorioLeito(){
		return leito.relatorioLeito(qtdeDias);
	}

	public Leito getleito() {
		return leito;
	}

	public void setleito(Leito leito) {
		this.leito = leito;
	}

	public int getQtdeDias() {
		return qtdeDias;
	}

	public void setQtdeDias(int qtdeDias) {
		this.qtdeDias = qtdeDias;
	}

}
