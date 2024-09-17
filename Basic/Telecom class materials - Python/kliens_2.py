from socket import socket, AF_INET, SOCK_STREAM

server_addr = ('localhost',42069)

client1 = socket(AF_INET, SOCK_STREAM)
client1.connect(server_addr)
client1.sendall("Hello Server!".encode())
data = client1.recv(16)
print("Received1:", data.decode())
client2 = socket(AF_INET, SOCK_STREAM)
client2.connect(server_addr)
client2.sendall("Hello Papacita!".encode())
data = client2.recv(16)
print("Received2:", data.decode())
client3 = socket(AF_INET, SOCK_STREAM)
client3.connect(server_addr)
client3.sendall("Hello sracok!".encode())
data = client3.recv(16)
print("Received3:", data.decode())

client1.close()
client2.close()
client3.close()