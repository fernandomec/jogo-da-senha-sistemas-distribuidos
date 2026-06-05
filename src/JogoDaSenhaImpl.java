import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class JogoDaSenhaImpl extends UnicastRemoteObject implements JogoDaSenha {
	private String senhaSecreta;
	private int jogadoresConectados;
	private boolean finalizado;
	private boolean senhaDefinida;

	public JogoDaSenhaImpl() throws RemoteException {
		super();
		this.jogadoresConectados = 0;
		this.finalizado = false;
		this.senhaDefinida = false;
		this.senhaSecreta = "";
	}

	@Override
	public synchronized int registrarJogador() throws RemoteException {
		if (jogadoresConectados < 2) {
			jogadoresConectados++;
			return jogadoresConectados; // 1- criador ou 2-adivinhado
		}
		return -1; // Servidor cheio
	}

	@Override
	public boolean aguardarOponente() throws RemoteException {
		return jogadoresConectados == 2;
	}

	@Override
	public synchronized String definirSenha(int idJogador, String senha) throws RemoteException {
		if (idJogador != 1)
			return "Apenas o Jogador 1 pode definir a senha.";
		if (senha.length() != 4)
			return "A senha deve ter exatamente 4 caracteres.";

		this.senhaSecreta = senha;
		this.senhaDefinida = true;
		return "Senha registrada com sucesso. Aguardando tentativas do Jogador 2...";
	}

	@Override
	public synchronized String fazerTentativa(int idJogador, String tentativa) throws RemoteException {
		if (idJogador != 2)
			return "Apenas o Jogador 2 pode adivinhar.";
		if (!senhaDefinida)
			return "Aguarde, a senha ainda não foi definida.";
		if (finalizado)
			return "O jogo já acabou!";
		if (tentativa.length() != 4)
			return "Sua tentativa deve ter 4 caracteres.";

		int exatos = 0;
		int parciais = 0;

		// lógica de verificação (exatos e parciais)
		boolean[] marcadosSenha = new boolean[4];
		boolean[] marcadosTentativa = new boolean[4];

		for (int i = 0; i < 4; i++) {
			if (tentativa.charAt(i) == senhaSecreta.charAt(i)) {
				exatos++;
				marcadosSenha[i] = true;
				marcadosTentativa[i] = true;
			}
		}

		for (int i = 0; i < 4; i++) {
			if (!marcadosTentativa[i]) {
				for (int j = 0; j < 4; j++) {
					if (!marcadosSenha[j] && tentativa.charAt(i) == senhaSecreta.charAt(j)) {
						parciais++;
						marcadosSenha[j] = true;
						break;
					}
				}
			}
		}

		if (exatos == 4) {
			this.finalizado = true;
			return "VOCE GANHOU! " + senhaSecreta;
		}

		return String.format("Dica: %d exatos (posição e valor), %d parciais (apenas valor).", exatos, parciais);
	}

	@Override
	public boolean jogoPronto() throws RemoteException {
		return this.senhaDefinida;
	}

	@Override
	public boolean jogoFinalizado() throws RemoteException {
		return this.finalizado;
	}
	
	@Override
    public synchronized void resetarServidor() throws RemoteException {
        this.jogadoresConectados = 0;
        this.finalizado = false;
        this.senhaDefinida = false;
        this.senhaSecreta = "";
    }
}