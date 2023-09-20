# 180000000 - KIN (9900000)

from net.swordie.ms.loaders import StringData

HAIR_MIN = 30000
HAIR_MAX = 42127
FACE_MIN = 20000
FACE_MAX = 26827

def divide_chunks(l, n):
    for i in range(0, len(l), n):
        yield l[i:i + n]

answer = sm.sendNext("What do you want to do?#b\r\n"
                     "#L0#Change Hair#l\r\n"
                     "#L1#Change Face#l\r\n"
                     "#L2#Exit#l")

if answer == 0:
    options = []
    for hair in range(HAIR_MIN, HAIR_MAX, 10):
        if not StringData.getItemStringById(hair) is None:
            options.append(hair)
    chunks = list(divide_chunks(options, 128))
    chunk_idx = sm.sendNext("Choose your new hairstyle!#b\r\n" + "\r\n".join("#L{}#Hairs {} - {} ({})#l".format(i, chunks[i][0], chunks[i][-1], len(chunks[i])) for i in range(len(chunks))))
    avatar_idx = sm.sendAskAvatar("Choose your new hairstyle!", False, False, chunks[chunk_idx])
    if answer < len(chunks[chunk_idx]):
        sm.changeCharacterLook(chunks[chunk_idx][avatar_idx])
elif answer == 1:
    options = []
    for face_group in range(FACE_MIN, FACE_MAX, 1000):
        for i in range(100):
            face = face_group + i
            if not StringData.getItemStringById(face) is None:
                options.append(face)
    chunks = list(divide_chunks(options, 128))
    chunk_idx = sm.sendNext("Choose your new face!#b\r\n" + "\r\n".join("#L{}#Faces {} - {} ({})#l".format(i, chunks[i][0], chunks[i][-1], len(chunks[i])) for i in range(len(chunks))))
    avatar_idx = sm.sendAskAvatar("Choose your new face!", False, False, chunks[chunk_idx])
    if answer < len(chunks[chunk_idx]):
        sm.changeCharacterLook(chunks[chunk_idx][avatar_idx])

sm.dispose()