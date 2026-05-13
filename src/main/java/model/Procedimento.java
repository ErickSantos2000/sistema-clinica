package model;

public interface Procedimento {
	public abstract float getValorProcedimento();
	public abstract String getTipoProcedimento(); 
	public abstract String relatorioProcedimentos(int quantidade);
}