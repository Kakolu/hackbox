import binascii
import sys
with open(sys.argv[1], "rb") as f:
    fout = open('utf8encoder_out.txt', 'wb')
    intake = (f.read(2))
    temp = binascii.hexlify(intake)
    finalstr = []
    while temp != "":
        if int(temp,16) < 0x7F :
            out = int(temp,16) & 0x7F
            finalstr.append(out)

        elif int(temp,16) < 0x7FF :
            b2 = 0x80 | (int(temp,16) & 0x3F)
            temp = int(temp,16) >> 6
            b1 = 0xC0 | (temp & 0x1F)
            out = str(b1)+ str(b2)
            finalstr.append(b1)
            finalstr.append(b2)
        elif int(temp,16) < 0xFFFF :
            b3 = 0x80 | (int(temp,16) & 0x3F)
            temp = int(temp,16) >> 6
            b2 = 0x80 | (temp & 0x3F)
            temp = temp >> 6
            b1 = 0xE0 | (temp & 0xF)
            out = str(b1)+ str(b2) + str(b3)
            finalstr.append(b1)
            finalstr.append(b2)
            finalstr.append(b3)
        intake = (f.read(2))
        temp = binascii.hexlify(intake)
    newArray = bytearray(finalstr)
    fout.write(newArray)

f.close()
fout.close()
