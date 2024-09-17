import socket
import sys
import struct
import time
import math
import select

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

cs_server_addr = (str(sys.argv[1]), int(sys.argv[2]))
sock.bind(cs_server_addr)
sock.listen()
packer = struct.Struct('100s')
srv_data = []
c_time = time.time()
elapsed_time = 0
inputs = [sock]

if (len(sys.argv) > 2):
    while True:
        try:
            timeout = 1
            r, w, e = select.select(inputs,inputs,inputs,timeout)
            
            if not (r or w or e):
                continue
            
            for s in r:
                if s is sock:
                    client_socket, client_addr = sock.accept()
                    inputs.append(client_socket)
                else:
                    #print(srv_data)
                    for i in range(len(srv_data)):
                        line = srv_data[i].split("|")
                        #print(line)
                        elapsed_time = time.time() - c_time
                        if(int(line[1]) - math.floor(elapsed_time) < 0):
                            del srv_data[i]
                            #print(line[0] + "lejart")
                        else:
                            line[1] = str(int(line[1]) - math.floor(elapsed_time))
                            srv_data[i] = '|'.join(line)
                    data = s.recv(packer.size)
                    if not data:
                        inputs.remove(s)
                        s.close()
                    else:
                        s_data = data.decode().split("|")
                        print(srv_data)
                        if(s_data[0] == "KI"):
                            msg = "0|"
                            for d in srv_data:
                                line = d.split("|")
                                if(line[0] == s_data[1]):
                                    msg = line[2] + "|" + line[3]
                                    break     
                            client_socket.sendall(bytes(msg, "utf-8"))  
                            client_socket.close()        
                        else:
                            srv_data.append(s_data[1] + "|60|" + s_data[3] + "|" + s_data[4].strip("\x00"))
                            client_socket.sendall(b'OK')
                            client_socket.close()
                        c_time = time.time()
                        inputs.remove(s)
                        s.close()
        except KeyboardInterrupt:
            client_socket.close()
            break
        