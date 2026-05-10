package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Internacao;
import model.Procedimento;
import model.Prontuario;
import model.TipoLeito;
import factory.ProcedimentoFactory;

public class ProntuarioService {

	private ProcedimentoFactory procedimentoFactory = new ProcedimentoFactory();

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
		Prontuario prontuario = new Prontuario(null);
		
		// passa o caminho do arquivo CSV
		Path path = Paths.get(arquivoCsv);

		// transforma o CSV em uma Stream de Strings
		// Files.lines(path) instrui a Stream ler o arquivo linha por linha
		Stream<String> linhas = Files.lines(path);

		// Auxiliar para pular o cabeçalho
		final boolean[] isHeader = {true};

		linhas.forEach((str) -> {
			if (isHeader[0]) {
				isHeader[0] = false;
			} else {
				System.out.println(str);

				String[] dados = str.split(",");

				String nomePaciente = dados[0].trim();

				TipoLeito tipoLeito = dados[1] != null && !dados[1].trim().isEmpty() ? TipoLeito.valueOf(dados[1].trim()) : null;

				int qtdeDiasInternacao = dados[2] != null && !dados[2].trim().isEmpty() ? Integer.parseInt(dados[2].trim()) : -1;

				String tipoProcedimento = dados[3] != null && !dados[3].trim().isEmpty() ? dados[3].trim() : null;

				int qtdeProcedimentos = dados.length == 5 && dados[4] != null && !dados[4].trim().isEmpty() ? Integer.parseInt(dados[4].trim()) : -1;

				prontuario.setNomePaciente(nomePaciente);

				if (tipoLeito != null && qtdeDiasInternacao > 0) {
					prontuario.setInternacao(new Internacao(tipoLeito, qtdeDiasInternacao));
				}

				if (tipoProcedimento != null && qtdeProcedimentos > 0) {
					while (qtdeProcedimentos > 0) {
						prontuario.addProcedimento(procedimentoFactory.criaProcedimento(tipoProcedimento));
						qtdeProcedimentos--;
					}
				}
			}
		});

		return prontuario;
	}

	// ==== DEVE FICAR EM UMA CLASSE DE REPOSITORIO
	public String salveProntuario(Prontuario prontuario) throws IOException {
		List<String> lines = new ArrayList<>();
		String nomePaciente = prontuario.getNomePaciente();
		Internacao internacao = prontuario.getInternacao();
		List<Procedimento> procedimentos = prontuario.getProcedimentos();

		lines.add("nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos");

		String l1 = nomePaciente + ",";

		if (internacao != null) {
			l1 += internacao.getTipoLeito() + "," + internacao.getQtdeDias() + ",,";
			lines.add(l1);
		}

		if (procedimentos.size() > 0) {
			Map<String, Long> procedimentosAgrupados = procedimentos.stream().collect(
					Collectors.groupingBy(Procedimento::getTipo, Collectors.counting()));

			List<String> procedimentosOrdenados = new ArrayList<>(procedimentosAgrupados.keySet());
			Collections.sort(procedimentosOrdenados);

			for (String chave : procedimentosOrdenados) {
				String l2 = nomePaciente + ",,," + chave + "," + procedimentosAgrupados.get(chave);
				lines.add(l2);
			}
		}

		if (lines.size() == 1) {
			l1 += ",,,";
			lines.add(l1);
		}

		Path path = Paths.get(nomePaciente.replaceAll(" ", "_").concat(String.valueOf(System.currentTimeMillis())).concat(".csv"));

		Files.write(path, lines);

		return path.toString();
	}
}
