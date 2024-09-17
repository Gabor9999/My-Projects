import sys
import socket
import struct
import time
import hashlib

if(len(sys.argv) > 5):
    
    server_addr = (str(sys.argv[1]), int(sys.argv[2]))
    
    cs_server_addr = (str(sys.argv[3]), int(sys.argv[4]))

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    checksum = ''
    sock.connect(server_addr)

    packer = struct.Struct('100s')
    unpacker = struct.Struct('2s')
    
    with open(str(sys.argv[6]), 'rb') as f:
        for line in f:
            msg = packer.pack(line)
            print( "Uzenet: %s" % (line) )	
            sock.sendall( msg )
        data = f.read()    
        checksum = hashlib.md5(data).hexdigest()
        f.close()
    time.sleep(2)
    sock.close()
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(cs_server_addr)
    msg = packer.pack(bytes("BE|" + str(sys.argv[5]) + "|60|" + str(sys.getsizeof(checksum)) + "|" + checksum, "utf-8"))
    print("BE|" + str(sys.argv[5]) + "|60|" + str(sys.getsizeof(checksum)) + "|" + checksum)
    sock.sendall(msg)
    data = sock.recv(unpacker.size)
    #print(data.decode())
    sock.close()
    