import sys,re,json,string,glob, os,math

poswordcount = 0.0
negwordcount = 0.0
decwordcount = 0.0
truwordcount = 0.0

def tokenize(text_string) :
    #text = text_string.strip(string.punctuation)
    #transtab = re.compile('[%s]' % re.escape(string.punctuation))
    #text = transtab.sub(' ', text_string)
    text = text_string.translate(string.maketrans("", ""), string.punctuation)
    text = text_string.lower()

    return filter(None, re.split("\W+",text))


def word_count(text,n):#counts each word
    global poswordcount
    global negwordcount
    global decwordcount
    global truwordcount
    text = tokenize(text)
    count = {}
    for t in text:
        if t not in stop_words:
            count[t] = count.get(t,0.0)+1.0
            if n == 1:
                poswordcount+=1
            elif n==2:
                negwordcount+=1
            elif n==3:
                decwordcount+=1
            elif n==4:
                truwordcount+=1
    return count

def create_model(s,dirs,n):#create prob list for each word
    global poswordcount
    global negwordcount
    global decwordcount
    global truwordcount
    list_len = 0.0
    if n==1:
        list_len=poswordcount
    if n==2:
        list_len=negwordcount
    if n==3:
        list_len=decwordcount
    if n==4:
        list_len=truwordcount

    total_len = len(dirs)
    model = {}
    for w in dirs:
        if w in s:
            model[w] = (s[w]+1)/float(total_len+list_len)
        else:
            model[w] = 1/float(total_len+list_len)

    return model

def read_files(path):#read files n return 1 string
    full_string = ""
    string_list = []
    for root, dirs, files in os.walk(path, topdown=False):
        for name in files:
            file = os.path.join(root, name)
            if "fold1" not in file:
                if ".DS_Store" not in file:
                    f=open(file, 'r')
                    string_list += f.read()
                    f.close()
    for s in string_list:
        full_string += s
    full_string = full_string.replace('\n', ' ')
    return full_string

def count_docs(path):#counts number of .txt files in path given
    N = 0
    for root, dirs, files in os.walk(path, topdown=False):
        for name in files:

            file = os.path.join(root, name)
            if "fold1" not in file and ".DS_Store" not in file:
                N+=1.0
    return N


#print sys.argv
document = sys.argv[1]
#document = "S:\\thirdsem\NLP\hw2\op_spam_train\op_spam_train"
N = 1.0
stop_words =["a","about","above","after","again","against","all","am","an","and","any","are","aren't","as","at","be","because","been","before","being","below","between","both","but","by","can't","cannot","could","couldn't","did","didn't","do","does","doesn't","doing","don't","down","during","each","few","for","from","further","had","hadn't","has","hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him","himself","his","how","how's","i","i'd","i'll","i'm","i've","if","in","into","is","isn't","it","it's","its","itself","let's","me","more","most","mustn't","my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shan't","she","she'd","she'll","she's","should","shouldn't","so","some","such","than","that","that's","the","their","theirs","them","themselves","then","there","there's","these","they","they'd","they'll","they're","they've","this","those","through","to","too","under","until","up","very","was","wasn't","we","we'd","we'll","we're","we've","were","weren't","what","what's","when","when's","where","where's","which","while","who","who's","whom","why","why's","with","won't","would","wouldn't","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves"]
N = count_docs(document)
pos_path = document + "/positive_polarity"
neg_path = document + "/negative_polarity"
deceptive1 = document + "/positive_polarity/deceptive_from_MTurk"
deceptive2 = document + "/negative_polarity/deceptive_from_MTurk"
truth1 = document + "/positive_polarity/truthful_from_TripAdvisor"
truth2 = document + "/negative_polarity/truthful_from_Web"
directory = read_files(document)
pos = read_files(pos_path)
Tpos = len(pos)
Npos = count_docs(pos_path)

neg = read_files(neg_path)
Tneg = len(neg)
Nneg = count_docs(neg_path)
dec = read_files(deceptive1) +" "+ read_files(deceptive2)
Tdec = len(dec)
Ndec = count_docs(deceptive1) + count_docs(deceptive2)
tru = read_files(truth1) +" "+ read_files(truth1)
Ttru = len(tru)
Ntru = count_docs(truth1) + count_docs(truth1)
Ppos = float(Npos)/float(N)
Pneg = float(Nneg)/float(N)
Pdec = float(Ndec)/float(N)
Ptru = float(Ntru)/float(N)

priors = {
    "pos": Ppos,
    "neg": Pneg,
    "dec": Pdec,
    "tru": Ptru
}

full_doc = word_count(directory,0)
pos_list = word_count(pos,1)
neg_list = word_count(neg,2)
dec_list = word_count(dec,3)
tru_list = word_count(tru,4)

positive = create_model(pos_list,full_doc,1)
negetive = create_model(neg_list,full_doc,2)
deceptive = create_model(dec_list,full_doc,3)
truthful = create_model(tru_list,full_doc,4)
dictionary = {
    "prior" : priors,
    "pos" : {},
    "neg" : {},
    "dec" : {},
    "tru" : {}
}
dictionary["pos"]=positive
dictionary["neg"]=negetive
dictionary["dec"]=deceptive
dictionary["tru"]=truthful
#print dictionary
json.dump(dictionary,open("nbmodel.txt",'w'))
#d2 = json.load(open("nbmodel.txt"))
#print d2["prior"]

