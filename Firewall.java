import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Firewall {
	List<IpPortPair> in_tcpList = new ArrayList<>();
	List<IpPortPair> out_tcpList = new ArrayList<>();
	List<IpPortPair> in_udpList = new ArrayList<>();
	List<IpPortPair> out_udpList = new ArrayList<>();
	
	public Firewall(String path) throws FileNotFoundException {
		File inputFile = new File(path);
		Scanner in = new Scanner(inputFile);
		while (in.hasNextLine()) {
			String tempLine = in.nextLine();
			String[] input = tempLine.split(",");
			if (input[0].equals("inbound") && input[1].equals("tcp")) {
				in_tcpList.add(BuildPair(input));
			} else if (input[0].equals("outbound") && input[1].equals("tcp")) {
				out_tcpList.add(BuildPair(input));
			} else if (input[0].equals("inbound") && input[1].equals("udp")) {
				in_udpList.add(BuildPair(input));
			} else if (input[0].equals("outbound") && input[1].equals("udp")) {
				out_udpList.add(BuildPair(input));
			}
		}
		Collections.sort(in_tcpList);
		Collections.sort(out_tcpList);
		Collections.sort(in_udpList);
		Collections.sort(out_udpList);
		in.close();
	}
	
	public boolean accept_packet (String direction, String protocol, int port, String ip) {
		if (direction.equals("inbound") && protocol.equals("tcp")) {
			return findTarget(port, ip, in_tcpList);
		} else if (direction.equals("outbound") && protocol.equals("tcp")) {
			return findTarget(port, ip, out_tcpList);
		} else if (direction.equals("inbound") && protocol.equals("udp")) {
			return findTarget(port, ip, in_udpList);
		} else {
			return findTarget(port, ip, out_udpList);
		}
	}
	
	public boolean findTarget(int port, String ip, List<IpPortPair> list) {
		Ipaddress targetIp = new Ipaddress(ip);
		int start = 0;
		int end = list.size() - 1;
		boolean res = false;
		while (start <= end) {
			int mid = start + (end - start) / 2;
			if (list.get(mid).getStartIp().compareTo(targetIp) < 0) {
				start = mid + 1;
			} else if (list.get(mid).getStartIp().compareTo(targetIp) > 0) {
				end = mid - 1;
			} else {
				int leftSide = mid, rightSide = mid;
				while (leftSide >= 0 && list.get(leftSide).getStartIp().compareTo(targetIp) == 0) {
					if (list.get(leftSide).findPort(port) == 1) {
						res = true;
						return res;
					}
					leftSide--;
				}
				while (rightSide <= list.size() - 1 && list.get(rightSide).getStartIp().compareTo(targetIp) == 0) {
					if (list.get(rightSide).findPort(port) == 1) {
						res = true;
						return res;
					}
					rightSide++;
				}
				return false;
			}
		}
		return false;
	}
	
	public IpPortPair BuildPair(String[] input) {
		String[] ip = input[3].split("-");
		String ipStart = ip[0];
		String ipEnd = ip[0];
		if (ip.length > 1) {
			ipEnd = ip[1];
		}
		IpPortPair pair = new IpPortPair(ipStart, ipEnd);
		String[] port = input[2].split("-");
		int portStart = Integer.parseInt(port[0]);
		int portEnd = Integer.parseInt(port[0]);
		if (port.length > 1) {
			portEnd = Integer.parseInt(port[1]);
		}
		pair.addPort(portStart, portEnd);
		return pair;
	}
	
	class Ipaddress implements Comparable<Ipaddress>{
		private int part1;
		private int part2;
		private int part3;
		private int part4;
		
		public Ipaddress(String input) {
			String[] temp = input.split("\\.");
			part1 = Integer.parseInt(temp[0]);
			part2 = Integer.parseInt(temp[0]);
			part3 = Integer.parseInt(temp[0]);
			part4 = Integer.parseInt(temp[0]);
		}
		
		public int compareTo(Ipaddress b) {
			int part1Diff = part1 - b.part1;
			int part2Diff = part2 - b.part2;
			int part3Diff = part3 - b.part3;
			int part4Diff = part4 - b.part4;
			if (part1Diff != 0) {
				return part1Diff;
			} else {
				if (part2Diff != 0) {
					return part2Diff;
				} else {
					if (part3Diff != 0) {
						return part3Diff;
					} else {
						return part4Diff;
					}
				}
			}
		}
	}
	
	class IpPortPair implements Comparable<IpPortPair>{
		private Ipaddress start;
		private Ipaddress end;
		private int[] port;
		
		public IpPortPair(String start, String end) {
			this.start = new Ipaddress(start);
			this.end = new Ipaddress(end);
			port = new int[65536];
		}
		
		public void addPort(int startNum, int endNum) {
			for (int i = startNum; i <= endNum; i++) {
				port[i] = 1;
			}
		}
		
		public int findPort(int n) {
			return port[n];
		}
		
		public Ipaddress getStartIp() {
			return start;
		}
		
		public boolean findIp(Ipaddress target) {
			if (start.compareTo(target) > 0) {
				return false;
			} else {
				if (end.compareTo(target) < 0) {
					return false;
				} else {
					return true;
				}
			}
		}
		
		public int compareTo(IpPortPair b) {
			return this.start.compareTo(b.start);
		}
	}
}
