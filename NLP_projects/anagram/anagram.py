import sys
f = open('anagram_out.txt', 'wb')
def ana(a):
    if(len(a) == 1):
        return [a]
    res = []
    for i,v in enumerate(a):
        res += [v+p for p in ana(a[:i]+a[i+1:])]
    res.sort()
    return res

x = sys.argv[1]
a_list = ana(x)
f.write('\n'.join(a_list))
f.write("\n")
f.close()