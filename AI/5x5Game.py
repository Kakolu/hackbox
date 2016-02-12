import sys,re,operator,copy
board ={}
boardTraverse = []
player1 = ""
player2 = ""
depth = 0
fmove=None
fbat=None
flog=None
infinity = float("Infinity")
stratp1 = 0
stratp2 = 0
depthp1 = 0
depthp2 = 0

def BATTLE(position,p1,p2):
    global player1,player2,fbat,depth
    newpos = copy.deepcopy(position)
    fbat = open("trace_state.txt","w+")
    player1 = p1
    player2 = p2
    while(has_space(newpos)):
        player1 = p1
        player2 = p2
        depth = depthp1
        if stratp1 == 1:
            move = GreedyBFS(newpos)
        elif stratp1 == 2:
            move = MINIMAX(newpos)
        elif stratp1 == 3:
            move = ALPHABETA(newpos)
        else:
            return
        newpos = print_nextmove(move,fbat,newpos)
        if has_space(newpos) == False:
            break
        player1 = p2
        player2 = p1
        depth = depthp2
        if stratp2 == 1:
            move = GreedyBFS(newpos)
        elif stratp2 == 2:
            move = MINIMAX(newpos)
        elif stratp2 == 3:
            move = ALPHABETA(newpos)
        else:
            return
        newpos = print_nextmove(move,fbat,newpos)
    fbat.close()
    return
def GreedyBFS(position):
    move = MINIMAX(position)
    return move

def MINIMAX(position):
    level = 0
    move = "root"
    newpos = copy.deepcopy(position)
    val = -infinity
    move,val = max(val,MAX(level,newpos,move,position))
    print_nextmove(move,fmove,newpos)
    return move

def print_nextmove(move,fmove,position):
    for x in range(0,5):
        for y in range(0,5):
            if move == boardTraverse[x][y]:
                raid = checkadj(x,y,player1,position)
    position[player1].update({move:board[move]})

    for x in range(0,5):
        for y in range(0,5):
            if boardTraverse[x][y] in position[player1] or boardTraverse[x][y] in raid or boardTraverse[x][y] == move:
                fmove.write(player1)
            elif boardTraverse[x][y] in position[player2]:
                fmove.write(player2)
            else:
                fmove.write("*")
        fmove.write("\n")
    if bool(raid):
        for u in raid:
            position[player1].update({u:board[u]})
            position[player2].pop(u)
    return position



def MAX(level,newpos,move,position):
    if level == depth or has_space(newpos)== False:
        val = E(newpos)
        print_log(move,level,val)
        return move,val

    val = -infinity
    poslist = {}
    prev_move = move
    level+=1
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move not in newpos[player1] and move not in newpos[player2]:
                if bool(poslist):
                    printval,printname = max_in_list(poslist)
                else:
                    printval = val
                print_log(prev_move,level-1,printval)
                newpos[player1].update({move:board[move]})
                raid = checkadj(x,y,player1,newpos)
                if bool(raid):
                    newpos = updatenewpos(newpos,raid,player1)
                if has_space(newpos)== False:
                    val = E(newpos)
                    print_log(move,level,val)
                    return move,val
                good_move,val = MIN(level,newpos,move,position)

                poslist.update({move:val})
                if move in newpos[player1]:
                    newpos[player1].pop(move)
                if bool(raid):
                    newpos = undoraid(newpos,raid,player1)
                if level == 1:
                    newpos = copy.deepcopy(position)
            if x == 4 and y == 4:
                newpos = copy.deepcopy(position)

    if level%2 == 1:
        print_log(prev_move,level-1,printval)
        val = printval
        printval,prev_move = max_in_list(poslist)
        val = printval
    return prev_move,val

def MIN(level,newpos,move,position):
    if level == depth or has_space(newpos)== False:
        val = E(newpos)
        print_log(move,level,val)
        return move,val
    val = infinity
    poslist = {}
    prev_move = move
    level+=1
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move not in newpos[player1] and move not in newpos[player2]:
                if bool(poslist):
                    printval,printmove = min_in_list(poslist)
                else:
                    printval = val
                print_log(prev_move,level-1,printval)
                newpos[player2].update({move:board[move]})
                raid = checkadj(x,y,player2,newpos)
                if bool(raid):
                    newpos = updatenewpos(newpos,raid,player2)
                if has_space(newpos)== False:
                    val = E(newpos)
                    print_log(move,level,val)
                    return move,val
                good_move,val = MAX(level,newpos,move,position)

                poslist.update({move:val})
                if move in newpos[player2]:
                    newpos[player2].pop(move)
                if bool(raid):
                    newpos = undoraid(newpos,raid,player2)
            if x == 4 and y == 4:
                newpos = copy.deepcopy(position)
    if level%2 ==0:
        print_log(prev_move,level-1,printval)
        val = printval
        printval,prev_move = min_in_list(poslist)
        val = printval
    return prev_move,val


def max_in_list(poslist):
    maxele = ""
    maxval = -99999
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move in poslist:
                if poslist[move] > maxval:
                    maxele = move
                    maxval = poslist[move]
    return maxval,maxele

def min_in_list(poslist):
    minele = ""
    minval = 99999
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move in poslist:
                if poslist[move] < minval:
                    minele = move
                    minval = poslist[move]
    return minval,minele

def has_space(newpos):
    count = 0
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move not in newpos[player1] and move not in newpos[player2]:
                count+=1
    if count > 0:
        return True
    return False

def updatenewpos(newpos,raid,player):
    if player == player1:
        p1 = player1
        p2 = player2
    else:
        p1 = player2
        p2 = player1
    for x in raid:
        newpos[p1].update({x:board[x]})
        newpos[p2].pop(x)
    return newpos

def undoraid(newpos,raid,player):
    if player == player1:
        p1 = player1
        p2 = player2
    else:
        p1 = player2
        p2 = player1
    for x in raid:
        newpos[p2].update({x:board[x]})
        newpos[p1].pop(x)
    return newpos

def print_log(move,level,val):
    if flog!=None:
        if val == infinity:
            val = "Infinity"
            flog.write(move+","+str(level)+","+val+"\n")
        elif val == -infinity:
            val = "-Infinity"
            flog.write(move+","+str(level)+","+val+"\n")
        else:
            flog.write(move+","+str(level)+","+str(val)+"\n")

def checkadj(x,y,player,newpos):
    newset ={}
    if player == player1:
        p1 = player1
        p2 = player2
    else:
        p1 = player2
        p2 = player1
    if (x+1 < 5 and boardTraverse[x+1][y] in newpos[p1]) or (y+1 < 5 and boardTraverse[x][y+1] in newpos[p1]) or (x-1 > -1 and boardTraverse[x-1][y] in newpos[p1]) or (y-1 > -1 and boardTraverse[x][y-1] in newpos[p1]):
        if x+1 < 5:
            if boardTraverse[x+1][y] in newpos[p2]:
                newset.update({boardTraverse[x+1][y]:board[boardTraverse[x+1][y]]})
        if y+1 < 5:
            if boardTraverse[x][y+1] in newpos[p2]:
                newset.update({boardTraverse[x][y+1]:board[boardTraverse[x][y+1]]})
        if x-1 > -1:
            if boardTraverse[x-1][y] in newpos[p2]:
                newset.update({boardTraverse[x-1][y]:board[boardTraverse[x-1][y]]})
        if y-1 > -1:
            if boardTraverse[x][y-1] in newpos[p2]:
                newset.update({boardTraverse[x][y-1]:board[boardTraverse[x][y-1]]})
    return newset

def E(new):
    x=0
    y=0
    for a in new[player1]:
        x+=int(new[player1][a])
    for b in new[player2]:
        y+=int(new[player2][b])

    return x-y

def ALPHABETA(position):
    global fmove
    level = 0
    move = "root"
    newpos = copy.deepcopy(position)
    alpha = -infinity
    beta = infinity
    move,val = abMAX(level,newpos,move,alpha,beta,position)
    print_nextmove(move,fmove,position)
    fmove.close()
    newpos[player1].update({move:board[move]})
    return newpos



def abMAX(level,newpos,move,alpha,beta,position):
    if level == depth or has_space(newpos)== False:
        val = E(newpos)
        abprint_log(move,level,val,alpha,beta)
        return move,val

    val = -infinity
    poslist = {}
    prev_move = move
    level+=1
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move not in newpos[player1] and move not in newpos[player2]:
                if bool(poslist):
                    printval,printname = max_in_list(poslist)
                else:
                    printval = val
                abprint_log(prev_move,level-1,printval,alpha,beta)
                newpos[player1].update({move:board[move]})
                raid = checkadj(x,y,player1,newpos)
                if bool(raid):
                    newpos = updatenewpos(newpos,raid,player1)
                if has_space(newpos)== False:
                    val = E(newpos)
                    print_log(move,level,val)
                    return move,val
                good_move,val = abMIN(level,newpos,move,alpha,beta,position)
                if val>=beta:
                    abprint_log(prev_move,level-1,printval,alpha,beta)
                    if move in newpos[player1]:
                        newpos[player1].pop(move)
                    if bool(raid):
                        newpos = undoraid(newpos,raid,player1)
                    newpos = copy.deepcopy(position)
                    return move,val
                alpha = max(alpha,val)
                poslist.update({move:val})
                if move in newpos[player1]:
                    newpos[player1].pop(move)
                if bool(raid):
                    newpos = undoraid(newpos,raid,player1)
                if level == 1:
                    newpos = copy.deepcopy(position)
            if x == 4 and y == 4:
                newpos = copy.deepcopy(position)
    if level%2 == 1:
        abprint_log(prev_move,level-1,max(val,alpha),alpha,beta)
        printval,prev_move = max_in_list(poslist)
        val = printval

    return prev_move,val

def abMIN(level,newpos,move,alpha,beta,position):
    if level == depth or has_space(newpos)== False:
        val = E(newpos)
        abprint_log(move,level,val,alpha,beta)
        return move,val

    val = infinity
    poslist = {}
    prev_move = move
    level+=1
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move not in newpos[player1] and move not in newpos[player2]:
                if bool(poslist):
                    printval,printmove = min_in_list(poslist)
                else:
                    printval = val
                abprint_log(prev_move,level-1,printval,alpha,beta)
                newpos[player2].update({move:board[move]})
                raid = checkadj(x,y,player2,newpos)
                if bool(raid):
                    newpos = updatenewpos(newpos,raid,player2)
                if has_space(newpos)== False:
                    val = E(newpos)
                    print_log(move,level,val)
                    return move,val
                good_move,val = abMAX(level,newpos,move,alpha,beta,position)
                if val<=alpha:
                    abprint_log(prev_move,level-1,val,alpha,beta)
                    if move in newpos[player1]:
                        newpos[player1].pop(move)
                    if bool(raid):
                        newpos = undoraid(newpos,raid,player2)
                    newpos = copy.deepcopy(position)
                    return move,val
                beta = min(beta,val)
                poslist.update({move:val})
                if move in newpos[player2]:
                    newpos[player2].pop(move)
                if bool(raid):
                    newpos = undoraid(newpos,raid,player2)
            if x == 4 and y == 4:
                newpos = copy.deepcopy(position)
    if level%2 ==0:
        abprint_log(prev_move,level-1,printval,alpha,beta)
        val = printval
        printval,prev_move = min_in_list(poslist)
        val = printval
    return prev_move,val

def abprint_log(move,level,val,alpha,beta):
    if flog!=None:
        if val == infinity:
            val = "Infinity"
        elif val == -infinity:
            val = "-Infinity"
        else:
            val = str(val)
        if alpha == infinity:
            alpha = "Infinity"
        elif alpha == -infinity:
            alpha = "-Infinity"
        else:
            alpha = str(alpha)
        if beta == infinity:
            beta = "Infinity"
        elif beta == -infinity:
            beta = "-Infinity"
        else:
            beta = str(beta)
        flog.write(move+","+str(level)+","+val+","+alpha+","+beta+"\n")

def has_space(newpos):
    count = 0
    for x in range(0,5):
        for y in range(0,5):
            move = boardTraverse[x][y]
            if move not in newpos[player1] and move not in newpos[player2]:
                count+=1
    if count > 0:
        return True
    return False

def get_empty(pos):
    empty = {}
    for x in board:
        if x not in pos["X"] and x not in pos["O"]:
           empty.update({x:board[x]})
    return empty
def createBoard(input1):
    global boardTraverse
    global board
    R1 = re.split("\W+",input1[3])
    R2 = re.split("\W+",input1[4])
    R3 = re.split("\W+",input1[5])
    R4 = re.split("\W+",input1[6])
    R5 = re.split("\W+",input1[7])
    board = {
        "A1":R1[0],"B1":R1[1],"C1":R1[2],"D1":R1[3],"E1":R1[4],
        "A2":R2[0],"B2":R2[1],"C2":R2[2],"D2":R2[3],"E2":R2[4],
        "A3":R3[0],"B3":R3[1],"C3":R3[2],"D3":R3[3],"E3":R3[4],
        "A4":R4[0],"B4":R4[1],"C4":R4[2],"D4":R4[3],"E4":R4[4],
        "A5":R5[0],"B5":R5[1],"C5":R5[2],"D5":R5[3],"E5":R5[4]
    }

    boardTraverse = [
        ["A1","B1","C1","D1","E1"],
        ["A2","B2","C2","D2","E2"],
        ["A3","B3","C3","D3","E3"],
        ["A4","B4","C4","D4","E4"],
        ["A5","B5","C5","D5","E5"]
    ]


def createPos(input1,board):
    R1 = list(input1[8])
    R2 = list(input1[9])
    R3 = list(input1[10])
    R4 = list(input1[11])
    R5 = list(input1[12])
    pos = {
        "A1":R1[0],"B1":R1[1],"C1":R1[2],"D1":R1[3],"E1":R1[4],
        "A2":R2[0],"B2":R2[1],"C2":R2[2],"D2":R2[3],"E2":R2[4],
        "A3":R3[0],"B3":R3[1],"C3":R3[2],"D3":R3[3],"E3":R3[4],
        "A4":R4[0],"B4":R4[1],"C4":R4[2],"D4":R4[3],"E4":R4[4],
        "A5":R5[0],"B5":R5[1],"C5":R5[2],"D5":R5[3],"E5":R5[4]
    }
    X = {}
    O = {}
    for x in pos:
        if pos[x] == "X":
            X.update({x : board[x]})
        if pos[x] == "O":
            O.update({x : board[x]})

    positions = {
        "X":X,
        "O":O
        }
    return positions


def main():
    global player1,player2,depth,pos,flog,stratp1,stratp2,depthp1,depthp2,fmove
    path = "Sampleinputsoutputs/5/input.txt"
    #path = sys.argv[2]
    input1 = []
    pos={}
    f = open(path,"r")
    flog = open("traverse_log.txt","w+")
    fmove = open("next_state.txt","w+")
    input1 = [line.strip() for line in f]

    task = int(input1[0])
    if task == 4:
        p1 = input1[1]
        p2 = input1[4]
        stratp1 = int(input1[2])
        stratp2 = int(input1[5])
        depthp1 = int(input1[3])
        depthp2 = int(input1[6])
        i = 0
        input2 = []
        input2 = input1[4:17]

        createBoard(input2)
        position= pos = createPos(input2,board)

        BATTLE(position,p1,p2)
    else:
        player1 = input1[1]
        if player1 == "X":
            player2 = "O"
        else:
            player2 = "X"
        depth = int(input1[2])

        createBoard(input1)
        pos = createPos(input1,board)
        newpos = {}
        if task == 1:

            newpos = GreedyBFS(pos)

        elif task == 2:

            flog.write("Node,Depth,Value\n")
            newpos = MINIMAX(pos)

        elif task == 3:

            flog.write("Node,Depth,Value,Alpha,Beta\n")
            newpos = ALPHABETA(pos)
    flog.close()
    fmove.close()
    f.close()




main()