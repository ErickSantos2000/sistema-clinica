public class ProcedimentoFactory {
	public Procedimento criaProcedimento(String tipo){
		if(tipo.equalsIgnoreCase("BASICO")){
			return new ProcedimentoBasico();
		} 
		else if (tipo.equalsIgnoreCase("COMUM")){
			return new ProcedimentoComum();
		} 
		else if(tipo.equalsIgnoreCase("AVANCADO")){
			return new ProcedimentoAvancado();
		} 

		return null;
	}
}
