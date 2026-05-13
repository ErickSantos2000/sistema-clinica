package model;

public class ProcedimentoBasico implements Procedimento {
    @Override
    public float getValorProcedimento() {
        return 50.00f;
    }

    @Override
    public String relatorioProcedimentos(int quantidade) {
        if (quantidade > 0) {
		    return "\n\t\t\t\t\t" + quantidade + " procedimento" + (quantidade > 1 ? "s" : "") + " básico" + (quantidade > 1 ? "s" : "");
            }

        return null;
    }

    @Override
    public String getTipoProcedimento() {
        return "BASICO";
    }
}