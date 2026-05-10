package model;

import java.util.ArrayList;
import java.util.List;

public class Prontuario {

	private String nomePaciente;
	private Internacao internacao;
	private List<Procedimento> procedimentos = new ArrayList<>();

	public Prontuario(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public String getNomePaciente() {
		return this.nomePaciente;
	}

	public void setInternacao(Internacao internacao) {
		this.internacao = internacao;
	}

	public Internacao getInternacao() {
		return this.internacao;
	}

	public void addProcedimento(Procedimento procedimento) {
		this.procedimentos.add(procedimento);
	}

	public List<Procedimento> getProcedimentos() {
		return this.procedimentos;
	}
}
