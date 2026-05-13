package model;

public class ProcedimentoAvancado implements Procedimento{
    @Override
    public float getValorProcedimento() {
        return 500.0f;
    }

    @Override
    public String relatorioProcedimentos(int quantidade) {
        if (quantidade > 0) {
			return "\n\t\t\t\t\t" + quantidade + " procedimento" + (quantidade > 1 ? "s" : "") + " avançado" + (quantidade > 1 ? "s" : "");
        }
        
        return null;
    }

    @Override
    public String getTipoProcedimento() {
        return "AVANCADO";
    }
}