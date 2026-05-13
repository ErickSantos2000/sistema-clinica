package model;

public interface Leito {
    public abstract float getValorLeito(float dias);
    public abstract String getTipoLeito();
    public abstract String relatorioLeito(int quantidade);
}
