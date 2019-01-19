# firewalls
Description:
Given a set of firewall rules, a network packet will be accepted by the firewall if and only if the direction, protocol, port, and IP address match at least one of the input rules. The provided input will be a CSV file in which each line contains exactly four columns: direction, protocol, ports, and IP address.

This Firewall class can read a set of rules from the input.txt file and store all the rules into the memory. When given a network packet, it will take O(logn) time to determine whether this packet can pass or not, where n is the number of rules in the input.txt.

The test.java is used to test the function of Firewall class. You just need to run test.java and it will print true or false based on the given test cases. If you want to test more cases, you can just add a new printout sentence and pass relevant parameter to it.

In the real world, a firewall should be fast at determining whether a packet can pass or not. So typically we would like to sacrifice space to gain high speed. In the Firewall class, I define two new classes to store the IP and port pair, and a range of IP address. Since the range of IP address is much larger than that of port address, I just use an int array to store the port address in order to make the time complexity of finding a target port number O(1). And I sort the array during the construction of the Firewall so I can use binary search to find a target IP address. The time complexity to construct a new Firewall class is O(nlogn), and it will it will take O(logn) time to determine whether this packet can pass or not.

By the way, I am interested in the policy team.
