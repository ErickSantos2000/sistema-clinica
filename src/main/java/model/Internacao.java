package model;

public class Internacao {

	private TipoLeito tipoLeito;
	private int qtdeDias;

	public Internacao(TipoLeito tipoLeito, int qtdeDias) {
		this.tipoLeito = tipoLeito;
		this.qtdeDias = qtdeDias;
	}

	public TipoLeito getTipoLeito() {
		return tipoLeito;
	}

	public void setTipoLeito(TipoLeito tipoLeito) {
		this.tipoLeito = tipoLeito;
	}

	public int getQtdeDias() {
		return qtdeDias;
	}

	public void setQtdeDias(int qtdeDias) {
		this.qtdeDias = qtdeDias;
	}

}
