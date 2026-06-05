import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoDaSenha extends Remote {
	int registrarJogador() throws RemoteException;

	boolean aguardarOponente() throws RemoteException;

	String definirSenha(int idJogador, String senha) throws RemoteException;

	String fazerTentativa(int idJogador, String tentativa) throws RemoteException;

	boolean jogoPronto() throws RemoteException;

	boolean jogoFinalizado() throws RemoteException;
	
	void resetarServidor() throws RemoteException;
}