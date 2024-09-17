from socket import socket, AF_INET, SOCK_STREAM,SOL_SOCKET, SO_REUSEADDR
from select import select
import queue
import struct

server_addr = ('',10000)

msg_q = queue.Queue()
username = {}
packer = struct.Struct("10s 100s")

with socket(AF_INET, SOCK_STREAM) as server:
    inputs = [ server ]
    server.bind(server_addr)
    server.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    server.listen(5)
    
    while True:
        timeout = 1
        r, w, e = select(inputs, inputs,inputs,timeout)
        
        if not (r or w or e):
            continue
        
        for s in r:
            if s is server:
                client, client_addr = s.accept()
                inputs.append(client)
                print("Csatlakozott:",client_addr)
            else:
                data = s.recv(packer.size)
                if not data:
                    msg_q.put((s, "LOGOUT: "+username[s] ))
                    inputs.remove(s)
                    s.close()
                    print("kliens kilepett")
                else:
                    msgtype, msg = packer.unpack(data)
                    if msgtype.strip(b'\x00') == b'NAME':
                        username[s] = msg.decode().strip("\x00")
                        msg_q.put((s, "LOGIN: "+username[s] ))
                    elif msgtype.strip(b'\x00') == b'MSG':
                        msg_q.put((s, msg.decode().strip("\x00") ))
        
        while not msg_q.empty():
            next_msg = msg_q.get_nowait()
            print("msg",next_msg[1])
            for s in w:
                if next_msg[0] != s:
                    s.sendall(("["+username[next_msg[0]]+"] "+next_msg[1]).encode())