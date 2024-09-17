import socket
import struct
import select

def calculate_function(func_type, value):
    if func_type == b'f':
        result = 3 * value + 4
    elif func_type == b'g':
        result = pow(2, value) - 1
    else:
        result = None
    return result

server_addr = ("localhost", 10000)
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(server_addr)
server_socket.listen(5)

inputs = [server_socket]
clients = {}

print("Szerver fut...")

while True:
    #3.
    r, _, _ = select.select(inputs, [], [])

    for s in r:
        if s is server_socket:
            client_socket, client_addr = server_socket.accept()
            print(f"{client_addr} csatlakozott")
            inputs.append(client_socket)
            clients[client_socket] = struct.Struct('1sI')
        else:
            data = s.recv(clients[s].size)
            if data:
                func_type, value = clients[s].unpack(data)
                result = calculate_function(func_type, value)
                if result is not None and result < 100:
                    response = struct.pack('2sI', b'OK', result)
                else:
                    response = struct.pack('2sI', b'HIBA', 0)
                s.sendall(response)
            else:
                print(f"{s.getpeername()} lekapcsolódott")
                inputs.remove(s)
                del clients[s]
                s.close()
