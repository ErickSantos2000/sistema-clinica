package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import factory.LeitoFactory;
import factory.ProcedimentoFactory;
import model.Internacao;
import model.Procedimento;
import model.Prontuario;

public class ProntuarioRepository {

    private ProcedimentoFactory procedimentoFactory = new ProcedimentoFactory();
	private LeitoFactory leitoFactory = new LeitoFactory();

    public ProntuarioRepository(){

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

				String tipoLeito = dados[1] != null && !dados[1].trim().isEmpty() ? dados[1].trim() : null;

				int qtdeDiasInternacao = dados[2] != null && !dados[2].trim().isEmpty() ? Integer.parseInt(dados[2].trim()) : -1;

				String tipoProcedimento = dados[3] != null && !dados[3].trim().isEmpty() ? dados[3].trim() : null;

				int qtdeProcedimentos = dados.length == 5 && dados[4] != null && !dados[4].trim().isEmpty() ? Integer.parseInt(dados[4].trim()) : -1;

				prontuario.setNomePaciente(nomePaciente);

				if (tipoLeito != null && qtdeDiasInternacao > 0) {
					prontuario.setInternacao(new Internacao(leitoFactory.criaLeito(tipoLeito), qtdeDiasInternacao));
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
					Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

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