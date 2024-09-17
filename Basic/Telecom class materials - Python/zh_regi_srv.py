from socket import socket,AF_INET, SOCK_STREAM
import sys
import struct
import select
import json

f = open("foglalasok.json")
data = json.load(f)

server_addr = ("localhost", 10000)
packer = struct.Struct('100s')

with socket(AF_INET, SOCK_STREAM) as server:
    inputs = [ server ]
    server.bind(server_addr)
    #server.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    server.listen(5)
    while True:
        try:
            timeout = 1
            r, w, e = select.select(inputs,inputs,inputs,timeout)
                
            if not (r or w or e):
                continue
            print(1)    
            for s in r:
                if s is server:
                    print(2)
                    client_socket, client_addr = server.accept()
                    inputs.append(client_socket)
                else:
                    print(3)
                    data_rcv = client_socket.recv(4)
                    print(4)
                    if not data_rcv:
                        inputs.remove(server)
                        server.close()
                    else:
                        print(5)
                        print("Kapott szám: " + data_rcv.decode())
                        if(data[str(data_rcv.decode())] == "szabad"):
                            data[str(data_rcv.decode())] = "foglalt"
                            print("A hely sikeresen lefoglalva")
                            msg = str(data_rcv.decode()) + "|Sikeres"
                            client_socket.sendall(bytes(msg, "utf-8"))
                        else:
                            print("A hely nem lett lefoglalva")
                            msg = str(data_rcv.decode()) + "|Sikertelen"
                            client_socket.sendall(bytes(msg, "utf-8"))
        except:
            server.close()
f.close()