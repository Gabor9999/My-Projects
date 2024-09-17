import socket
import struct

client_addr = ("localhost", 10001)
server_addr = ("localhost", 10000)
server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_socket.bind(server_addr)

datas = {}

def handle_client_request(data, client_socket):
    func_type, value = struct.unpack('1sI', data)
    key = (func_type, value)

    if key in datas:
        response_type, result = datas[key]
    else:
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        server_socket.sendto(data, server_addr)
        response, _ = server_socket.recvfrom(6)
        response_type, result = struct.unpack('2sI', response)
        datas[key] = (response_type, result)
        server_socket.close()

        if len(datas) > 5:
            old_key = next(datas)
            del datas[old_key]

        response = struct.pack('2sI', response_type, result)
        client_socket.sendall(response)
        

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(client_addr)

while True:
    data = client_socket.recv(8)
    if not data:
        break
    handle_client_request(data, client_socket)

client_socket.close()
