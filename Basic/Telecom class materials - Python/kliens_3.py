from socket import socket, AF_INET, SOCK_STREAM
import struct

server_addr = ('localhost',10000)
packer = struct.Struct('I I 1s')
client1 = socket(AF_INET, SOCK_STREAM)
client1.connect(server_addr)

szam1 = 1
szam2 = 2
op = '+'
value = (szam1,szam2,op.encode())
packed_data = packer.pack(*value)

client1.sendall(packed_data)
data = client1.recv(16)
print("Received:", data.decode())

client1.close()