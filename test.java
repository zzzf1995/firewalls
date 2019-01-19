import java.io.FileNotFoundException;

public class test {
	public static void main(String[] args) throws FileNotFoundException {
		Firewall fw = new Firewall("input.txt");
		System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
		System.out.println(fw.accept_packet("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(fw.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(fw.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
		System.out.println(fw.accept_packet("inbound", "udp", 24, "52.12.48.92"));
		System.out.println(fw.accept_packet("inbound", "udp", 90, "192.168.3.1"));
	}
}
