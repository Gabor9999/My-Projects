import sys
import socket
import struct
import time
import random

server_addr = ("localhost", 10000)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(server_addr)

packer = struct.Struct('1s')
unpacker = struct.Struct('100s')
    
for i in range(3):
    wait_num = random.randint(1, 5)
    seat_num = random.randint(1, 30)
    print("Foglalás igénylése erre a helyre: %i" % (seat_num) )	
    print(1)
    sock.send(bytes(str(seat_num), "utf-8"))
    print(2)
    data = sock.recv(unpacker.size)
    print(3)
    print(data.decode())
    print("Várunk " + str(wait_num) + " mp-t a küldések között")
    time.sleep(wait_num)
sock.close()