package service;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

import model.Internacao;
import model.Procedimento;
import model.TipoLeito;
import model.Prontuario;
import repository.ProntuarioRepository;

public class ProntuarioService {

	private ProntuarioRepository procecProntuarioRepository;

	public ProntuarioService(ProntuarioRepository prontuarioRepository){
		this.procecProntuarioRepository = prontuarioRepository;
	}

	public String imprimaConta(Prontuario prontuario) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();

		String conta = "----------------------------------------------------------------------------------------------";

		float valorDiarias = 0.0f;
		Internacao internacao = prontuario.getInternacao();
		List<Procedimento> procedimentos = prontuario.getProcedimentos();
		String nomePaciente = prontuario.getNomePaciente();

		// Contabilizar as diárias
		// ==== utilizar polimorfimos aqui
		if (internacao != null) {
			switch (internacao.getTipoLeito()) {
				case ENFERMARIA:
					if (internacao.getQtdeDias() <= 3) {
						valorDiarias += 40.00 * internacao.getQtdeDias(); // Internação Básica
					} else if (internacao.getQtdeDias() <= 8) {
						valorDiarias += 35.00 * internacao.getQtdeDias(); // Internação Média
					} else {
						valorDiarias += 30.00 * internacao.getQtdeDias(); // Internação Grave
					}
					break;
				case APARTAMENTO:
					if (internacao.getQtdeDias() <= 3) {
						valorDiarias += 100.00 * internacao.getQtdeDias(); // Internação Básica
					} else if (internacao.getQtdeDias() <= 8) {
						valorDiarias += 90.00 * internacao.getQtdeDias();  // Internação Média
					} else {
						valorDiarias += 80.00 * internacao.getQtdeDias();  // Internação Grave
					}
					break;
			}
		}

		float valorTotalProcedimentos = 0.00f;

		//Contabiliza os procedimentos
		// ==== utilizar polimorfimos aqui
		for (Procedimento procedimento : procedimentos) {
			// uso do polimorfismo
			valorTotalProcedimentos += procedimento.getValor();	

			// switch (procedimento.getTipoProcedimento()) {
			// 	case BASICO:
			// 		qtdeProcedimentosBasicos++;
			// 		valorTotalProcedimentos += procedimento.getVa;
			// 		break;

			// 	case COMUM:
			// 		qtdeProcedimentosComuns++;
			// 		valorTotalProcedimentos += 150.00;
			// 		break;

			// 	case AVANCADO:
			// 		qtdeProcedimentosAvancados++;
			// 		valorTotalProcedimentos += 500.00;
			// 		break;
			// }
		}

		conta += "\nA conta do(a) paciente " + nomePaciente + " tem valor total de __ " + formatter.format(valorDiarias + valorTotalProcedimentos) + " __";
		conta += "\n\nConforme os detalhes abaixo:";

		if (internacao != null) {
			conta += "\n\nValor Total Diárias:\t\t\t" + formatter.format(valorDiarias);
			conta += "\n\t\t\t\t\t" + internacao.getQtdeDias() + " diária" + (internacao.getQtdeDias() > 1 ? "s" : "")
					+ " em " + (internacao.getTipoLeito() == TipoLeito.APARTAMENTO ? "apartamento" : "enfermaria");
		}

		if (procedimentos.size() > 0) {
			conta += "\n\nValor Total Procedimentos:\t\t" + formatter.format(valorTotalProcedimentos);

			// usa a lista de procedimentos pra fazer agrupamento por tipo
			Map<String, List<Procedimento>> grupos = procedimentos.stream()
				.collect(
					Collectors.groupingBy(
						// define o criterio de agrupamento pelo tipo 
						Procedimento::getTipo, 			
						// como HashMap comum não garante a ordem das chaves
						// ao usar LinkedHashMap garante que os grupos apareçam na mesma ordem em que foram inseridos
						LinkedHashMap::new,    
						// diz que em cada balde do mapa, eu quero um List com todos os objs
						Collectors.toList()
					));

			// grupos.keySet() pega o nome de todos os grupos	
			for (String tipo : grupos.keySet()) {
				List<Procedimento> listaDoTipo = grupos.get(tipo); // entra no balde e tira a lista de procedimetos
				int qtd = listaDoTipo.size(); // descobre quantos procedimentos daquele tipo existem
				// pega o primeiro procedimento da lista 
				Procedimento exemplo = listaDoTipo.get(0); 
				// uso do polimorfismo
				conta += exemplo.imprimeRelatorio(qtd);

				// conta += "\n\nValor Total Procedimentos:\t\t" + formatter.format(valorTotalProcedimentos);

				// if (qtdeProcedimentosBasicos > 0) {
				// 	conta += "\n\t\t\t\t\t" + qtdeProcedimentosBasicos + " procedimento" + (qtdeProcedimentosBasicos > 1 ? "s" : "")
				// 			+ " básico" + (qtdeProcedimentosBasicos > 1 ? "s" : "");
				// }

				// if (qtdeProcedimentosComuns > 0) {
				// 	conta += "\n\t\t\t\t\t" + qtdeProcedimentosComuns + " procedimento" + (qtdeProcedimentosComuns > 1 ? "s" : "")
				// 			+ " comu" + (qtdeProcedimentosComuns > 1 ? "ns" : "m");
				// }

				// if (qtdeProcedimentosAvancados > 0) {
				// 	conta += "\n\t\t\t\t\t" + qtdeProcedimentosAvancados + " procedimento" + (qtdeProcedimentosBasicos > 1 ? "s" : "")
				// 			+ " avançado" + (qtdeProcedimentosAvancados > 1 ? "s" : "");
				// }

				// ======= (OUTRA FORMA DE FAZER) ========
				// 	if (procedimentos.size() > 0) {
				// conta += "\n\nValor Total Procedimentos:\t\t" + formatter.format(valorTotalProcedimentos);

				// // usa a lista de procedimentos pra fazer agrupamento por tipo
				// Map<String, Long> contagem = procedimentos.stream()
				// 	.collect(Collectors.groupingBy(
				// 		Procedimento::getTipo, 
				// 		LinkedHashMap:: new,
				// 		Collectors.counting()
				// 	));

				// 	for (String tipo : contagem.keySet()) {
				// 		long qtd = contagem.get(tipo);

				// 		// Buscamos qualquer procedimento que tenha essa descrição para usar seu método de formatar
				// 		Procedimento exemplo = procedimentos.stream()
				// 			.filter(p -> p.getTipo().equals(tipo))
				// 			.findFirst()
				// 			.get();

				// 		conta += exemplo.imprimeRelatorio((int) qtd);
				// 	}
				// }

			}
		}
		
		conta += "\n\nVolte sempre, a casa é sua!";
		conta += "\n----------------------------------------------------------------------------------------------";

		return conta;
	}

	// ==== DEVE FICAR EM UMA CLASSE DE REPOSITORIO
	public Prontuario carregueProntuario(String arquivoCsv) throws IOException {
		return this.procecProntuarioRepository.carregueProntuario(arquivoCsv);
	}

	// ==== DEVE FICAR EM UMA CLASSE DE REPOSITORIO
	public String salveProntuario(Prontuario prontuario) throws IOException {
		return this.procecProntuarioRepository.salveProntuario(prontuario);
	}
}
