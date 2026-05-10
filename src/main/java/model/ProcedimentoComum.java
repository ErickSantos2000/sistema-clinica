package model;

public class ProcedimentoComum implements Procedimento {
    @Override
    public float getValor() {
        return 150.00f;
    }

    @Override
    public String imprimeRelatorio(int quantidade) {
        if (quantidade > 0) {
		    return "\n\t\t\t\t\t" + quantidade + " procedimento" + (quantidade > 1 ? "s" : "") + " comu" + (quantidade > 1 ? "ns" : "m");
            }

        return null;  
    }

    @Override
    public String getTipo() {
        return "COMUM";
    }
}