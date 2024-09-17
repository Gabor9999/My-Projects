from socket import socket, AF_INET, SOCK_DGRAM
import sys

server_addr = ('localhost', 10000)

if len(sys.argv) > 1:
    file = sys.argv[1]
else:
    file = "output.txt"

with socket(AF_INET,SOCK_DGRAM) as server:
	server.bind(server_addr)

	end = False
	with open(file, "wb") as f: #binĂĄris mĂłdban nyitjuk meg
		while not end:
			data, _ = server.recvfrom(10)
			print("alma")
			if data:
				f.write(data)
			else:
				end = True
print("Sikerult")