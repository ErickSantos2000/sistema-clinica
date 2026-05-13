package factory;

import model.Apartamento;
import model.Enfermaria;
import model.Leito;

public class LeitoFactory {
	public Leito criaLeito(String tipo){
		if(tipo.equalsIgnoreCase("APARTAMENTO")){
			return new Apartamento();
		} 
		else if (tipo.equalsIgnoreCase("ENFERMARIA")){
			return new Enfermaria();
		}

		return null;
	}
}
