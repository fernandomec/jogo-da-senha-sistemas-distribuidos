import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
	public static void main(String[] args) {
		try {
			// IPV4 pc desktop
			System.setProperty("java.rmi.server.hostname", "192.168.0.101");
			
			// localhost
			//System.setProperty("java.rmi.server.hostname", "127.0.0.1");
			
			//template
			//System.setProperty("java.rmi.server.hostname", "IP");

			JogoDaSenhaImpl jogo = new JogoDaSenhaImpl();
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("JogoDaSenhaService", jogo);

			System.out.println("Servidor RMI do Jogo da Senha rodando na porta 1099...");
		} catch (Exception e) {
			System.err.println("Erro no Servidor: " + e.toString());
			e.printStackTrace();
		}
	}
}