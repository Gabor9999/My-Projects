from socket import socket, AF_INET, SOCK_STREAM
import sys
import struct
import time

server_addr = (str(sys.argv[1]), int(sys.argv[2]))
cs_server_addr = (str(sys.argv[3]), int(sys.argv[4]))
unpacker = struct.Struct('100s')

if (len(sys.argv) > 5):
    with socket(AF_INET,SOCK_STREAM) as sock:
        sock.bind(server_addr)
        sock.listen()
        eof = False
        with open(str(sys.argv[6]), 'wb') as f:
            client_socket, client_addr = sock.accept()
            while not eof:
                data = client_socket.recv(unpacker.size)
                if data:
                    print("Received:", data)
                    f.write(data)
                else:
                    client_socket.close()
                    eof = True                     
        sock.close()
    time.sleep(2)
    sock = socket(AF_INET, SOCK_STREAM)
    sock.connect(cs_server_addr)
    msg = unpacker.pack(bytes("KI|" + sys.argv[5], "utf-8"))
    print("KI|" + sys.argv[5])
    sock.sendall(msg)
    data = sock.recv(unpacker.size)
    data = data.decode().split("|")
    if(data[0] == 0):
        print("CSUM CORRUPTED")
    else:
        print("CSUM OK")
    sock.close()