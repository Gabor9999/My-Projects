import socket
import struct

server_addr = ("localhost", 10000)
#proxy_addr = ("localhost", 10001)
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(server_addr)

while True:
    func_type = input("Adja meg a függvény típusát (f/g): ").encode('utf-8')
    while(func_type != b'f' and func_type != b'g'):
        print("Input hiba - A függvény típusa helytelen")
        func_type = input("Adja meg a függvény típusát (f/g): ").encode('utf-8')
    value = int(input("Adja meg az értéket: "))

    data = struct.pack('1sI', func_type, value)
    client_socket.sendall(data)

    response = client_socket.recv(8)
    response_type, result = struct.unpack('2sI', response)
    
    #print("Válasz: " + response_type.decode() + " " + str(result))

    if response_type == b'OK':
        print(f"Eredmény: {result}")
    else:
        print("Hiba - Az eredmény nem kisebb mint 100")

