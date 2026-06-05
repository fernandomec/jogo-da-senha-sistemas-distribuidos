import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);

			// digitar ip do servidor
			System.out.print("Digite o IP do Servidor (ou 'localhost' para testar na mesma maquina): ");
			String ipServidor = scanner.nextLine();

			// usa o ip digitado
			Registry registry = LocateRegistry.getRegistry(ipServidor, 1099);
			JogoDaSenha jogo = (JogoDaSenha) registry.lookup("JogoDaSenhaService");

			int meuId = jogo.registrarJogador();

			if (meuId == -1) {
				System.out.println("ERRO");
				return;
			}

			System.out.println("Conectado! Você é o Jogador " + meuId);

			if (meuId == 1) {
				System.out.println("Aguardando o Jogador 2 se conectar...");
				while (!jogo.aguardarOponente()) {
					Thread.sleep(1000);
				}

				System.out.print("Jogador 2 conectado! Defina a senha (4 caracteres): ");
				String senha = scanner.nextLine();
				System.out.println(jogo.definirSenha(meuId, senha));

				System.out.println("Aguarde enquanto o Jogador 2 tenta adivinhar...");
				while (!jogo.jogoFinalizado()) {
					Thread.sleep(2000);
				}
				System.out.println("Fim de jogo! O Jogador 2 finalizou suas tentativas.");

			} else if (meuId == 2) {
				System.out.println("Aguardando o Jogador 1 definir a senha...");
				while (!jogo.jogoPronto()) {
					Thread.sleep(1000);
				}

				System.out.println("A senha foi definida! O jogo começou.");

				while (!jogo.jogoFinalizado()) {
					System.out.print("Sua tentativa (4 caracteres): ");
					String tentativa = scanner.nextLine();

					String resposta = jogo.fazerTentativa(meuId, tentativa);
					System.out.println(resposta);

					if (resposta.contains("VITORIA")) {
						break;
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
			System.err.println("Erro no Cliente: " + e.toString());
		}
	}
}