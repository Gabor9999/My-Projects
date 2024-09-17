from socket import socket,AF_INET, SOCK_STREAM, timeout, SOL_SOCKET, SO_REUSEADDR
import struct
import random
import sys
from select import select

server_addr = ('', int(sys.argv[2]))
unpacker = struct.Struct('c i')

num = random.randint(1,100)
print(num)
guessed = False

with socket(AF_INET, SOCK_STREAM) as server:
    server.bind(server_addr)
    server.listen(1)
    server.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    
    socketek = [ server ]
    
    while True:
        r,w,e = select(socketek,[],[],1)

        if not (r or w or e):
            continue
        
        for s in r:
            if s is server:
                client, client_addr = s.accept()
                socketek.append(client)
                print("Csatlakozott", client_addr)
            else:
                data = s.recv(unpacker.size)
                if not data:
                    socketek.remove(s)
                    s.close()
                    print("Kilepett")
                else:
                    print("Kaptam:",data)
                    unp_data = unpacker.unpack(data)
                    print("Unpack:",unp_data)
                    x = unp_data[0].decode() + " " + str(unp_data[1])
                    print("Kiertekeltem es visszakuldtem", x)
                    if(guessed):
                        msg = unpacker.pack(b'V', 0)
                        s.sendall(msg)
                    else:
                        if (unp_data[0].decode() == '>'):
                            if(num > unp_data[1]):
                                msg = unpacker.pack(b'I', 0)
                                s.sendall(msg)
                            else:
                                msg = unpacker.pack(b'N', 0)
                                s.sendall(msg)
                        elif (unp_data[0].decode() == '<'):
                            if(num >= unp_data[1]):
                                msg = unpacker.pack(b'N', 0)
                                s.sendall(msg)
                            else:
                                msg = unpacker.pack(b'I', 0)
                                s.sendall(msg)
                        elif (unp_data[1] == num):
                            msg = unpacker.pack(b'Y', 0)
                            s.sendall(msg)
                            guessed = True
                        else:
                            msg = unpacker.pack(b'K', 0)
                            s.sendall(msg)
                        