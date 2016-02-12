import json,math,os,sys,re,string
def tokenize(text_string) :
    text = text_string.translate(string.maketrans("", ""), string.punctuation)
    text = text_string.lower()
    return filter(None, re.split("\W+",text))


def word_count(text):#counts each word
    text = tokenize(text)
    count = {}
    for t in text:
        if t not in stop_words:
            count[t] = count.get(t,0.0)+1.0
    return count

def calc_score(score,input_str,dicts):
    word_list = word_count(input_str)
    for t in word_list:
        if t in dicts["pos"]:
            score["pos"] += math.log(dicts["pos"][t])
        if t in dicts["neg"]:
            score["neg"] += math.log(dicts["neg"][t])

    for t in word_list:
        if t in dicts["dec"]:
            score["dec"] += math.log(dicts["dec"][t])
        if t in dicts["tru"]:
            score["tru"] += math.log(dicts["tru"][t])

    return score

def read_calc(path,dicts):
    fout = open('nboutput.txt', 'w+')
    testing_string = ""
    for root, dirs, files in os.walk(path, topdown=False):
        for name in files:
            score = {
                "pos" : math.log(dicts["prior"]["pos"]),
                "neg" : math.log(dicts["prior"]["neg"]),
                "dec" : math.log(dicts["prior"]["dec"]),
                "tru" : math.log(dicts["prior"]["tru"])
            }
            sfile = os.path.join(root, name)
            if "fold1" in sfile and ".DS_Store" not in sfile:
                f=open(sfile, 'r')
                testing_string = f.read()
                final_score = calc_score(score,testing_string,dicts)
                #print final_score
                output_str = ""
                if final_score["tru"] > final_score["dec"]:
                    output_str += "truthful"
                else:
                    output_str += "deceptive"
                if final_score["pos"] > final_score["neg"]:
                    output_str += " positive "
                else:
                    output_str += " negative "
                output_str += str(sfile) + "\n"
                fout.write(output_str)
                f.close()
    fout.close()


path = sys.argv[1]
#path = "S:\\thirdsem\NLP\hw2\op_spam_train\op_spam_train"
stop_words =["a","about","above","after","again","against","all","am","an","and","any","are","aren't","as","at","be","because","been","before","being","below","between","both","but","by","can't","cannot","could","couldn't","did","didn't","do","does","doesn't","doing","don't","down","during","each","few","for","from","further","had","hadn't","has","hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him","himself","his","how","how's","i","i'd","i'll","i'm","i've","if","in","into","is","isn't","it","it's","its","itself","let's","me","more","most","mustn't","my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shan't","she","she'd","she'll","she's","should","shouldn't","so","some","such","than","that","that's","the","their","theirs","them","themselves","then","there","there's","these","they","they'd","they'll","they're","they've","this","those","through","to","too","under","until","up","very","was","wasn't","we","we'd","we'll","we're","we've","were","weren't","what","what's","when","when's","where","where's","which","while","who","who's","whom","why","why's","with","won't","would","wouldn't","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves"]
dicts = json.load(open("nbmodel.txt"))
testing_string = ""
#print dict["prior"]
read_calc(path,dicts)

