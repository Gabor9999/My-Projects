import struct
import sys
import socket

packer = struct.Struct('20s i')

datas = [
        (b'inf.elte.hu',21),
        (b'elte.hu',22),
        (b'tanrend.elte.hu',25),
        (b'ggombos.web.elte.hu',42),
        (b'lakis.web.elte.hu',80),
        (b'szalaigj.web.elte.hu',80),
        (b'g7tzap.web.elte.hu',80),
        (b'tms.inf.elte.hu',88),
        (b'canvas.elte.hu',443),
        (b'neptun.elte.hu',22),
        (b'duckduckgo.com',443),
        ]

with open('valami.bin', 'wb') as f:
    '''for i in range(5):
            values = ('inf.elte.hu'.encode(), i+20)
            packed_data = packer.pack(*values)
            f.write(packed_data)'''
    for v in datas:
        packed_data = packer.pack(*v)
        f.write(packed_data)

if len(sys.argv) != 1:                   
        with open('valami.bin', 'rb') as f:
            f.seek(packer.size * int(sys.argv[2]))
            data = f.read(packer.size)
            unp_data = packer.unpack(data)
            if sys.argv[1] == "port":
                print(unp_data[1], socket.getservbyport(unp_data[1]))
            elif sys.argv[1] == "domain":
                print(unp_data[0], socket.gethostbyname(unp_data[0].decode().strip('\x00')))
                
else:
    print(socket.gethostname())