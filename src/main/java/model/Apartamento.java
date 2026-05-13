package model;

public class Apartamento implements Leito{
    @Override
    public float getValorLeito(float dias) {
        float valorDiarias = 0.0f;
        if (dias <= 3) {
                valorDiarias += 100.00 * dias; // Internação Básica
            } else if (dias <= 8) {
                valorDiarias += 90.00 * dias;  // Internação Média
            } else {
                valorDiarias += 80.00 * dias;  // Internação Grave
            }
        return valorDiarias;
    }

    @Override
    public String getTipoLeito() {
        return "APARTAMENTO";
    }

    @Override
    public String relatorioLeito(int quantidadeDias) {
        return 
        "\n\t\t\t\t\t" 
        + quantidadeDias 
        + " diária" + (quantidadeDias > 1 ? "s" : "")
        + " em " + "apartamento";
    }
}
