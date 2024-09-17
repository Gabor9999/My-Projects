import sys
import socket
import random
import struct
import time

server_addr = (str(sys.argv[1]), int(sys.argv[2]))

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

sock.connect(server_addr)

packer = struct.Struct('c i')

ops = ['>', '<']
eq = '='
r1 = 1
r2 = 100
for nrmd in range(100):
    szam = random.randint(r1,r2)
    if(r2 - r1 == 0):
        op = eq
    else:
        op = ops[nrmd % len(ops)]
    msg = packer.pack(op.encode(),szam)
    print( "Uzenet: %c %d" % (op, szam) )	
    sock.sendall( msg )

    msg = sock.recv(packer.size)
    parsed_msg = packer.unpack( msg )
    print( "Kapott eredmeny: %c %d" % (parsed_msg[0].decode(), parsed_msg[1]))
    if(parsed_msg[0].decode() == 'I'):
        if(op == '>'):
            r1 = szam+1
        else:
            r2 = szam-1
    if(parsed_msg[0].decode() == 'N'):
        if(op == '>'):
            r2 = szam
        elif(op == '<'):
            r1 = szam
    if(parsed_msg[0].decode() == 'Y'):
        print("Nyertél")
        break
    if(parsed_msg[0].decode() == 'K'):
        print("Kiestél")
        break
    if(parsed_msg[0].decode() == 'V'):
        print("Vége")
        break
    time.sleep(4)
sock.close()