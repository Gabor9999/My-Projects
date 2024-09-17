from socket import socket, AF_INET, SOCK_DGRAM
import sys

server_addr = ('localhost', 10000)

if len(sys.argv) > 1:
    file = sys.argv[1]
else:
    file = "input.txt"

with socket(AF_INET,SOCK_DGRAM) as client:
	with open(file, "rb") as f: #binĂĄris mĂłdban nyitjuk meg
		l = f.read(10)
		while l:
			client.sendto(l,server_addr)
			l = f.read(10)
	client.sendto(b'',server_addr)
print("Elkuldtem")