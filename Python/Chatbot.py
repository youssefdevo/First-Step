from flask import Flask, request, jsonify
import pandas as pd
import math
import re
import sys
from collections import defaultdict
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from flask_cors import CORS  # Import CORS

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

STOPWORDS = set(stopwords.words("english"))
DATASET = r"C:\Users\Lenovo\Desktop\chat\pitch_decks_dataset.csv"

document_filenames = {}
N = 0
vocabulary = set()
postings = defaultdict(dict)
document_frequency = defaultdict(int)
length = defaultdict(float)

def initialize():
    get_corpus()
    initialize_terms_and_postings()
    initialize_document_frequencies()
    initialize_lengths()

def get_corpus():
    global df, N
    df = pd.read_csv(DATASET, encoding='ISO-8859-1')
    N = len(df)

def initialize_terms_and_postings():
    global vocabulary, postings
    for index, row in df.iterrows():
        document = row['Full Description']
        if not isinstance(document, str):
            continue
        document = remove_special_characters(document)
        document = remove_digits(document)
        terms = tokenize(document)
        unique_terms = set(terms)
        vocabulary = vocabulary.union(unique_terms)
        for term in unique_terms:
            postings[term][index] = terms.count(term)

def combine_fields(row):
    combined = ""
    for field in ['Company Name', 'Slogan', 'Amount Raised', 'Year', 'Stage', 'Business Model',
                  'Full Description', 'Investors', 'About', 'Industry', 'Tags', 'Customer Model',
                  'Website', 'Legal Name', 'Type']:
        if isinstance(row[field], str):
            combined += row[field] + " "
    return combined.strip()

def tokenize(document):
    terms = word_tokenize(document)
    terms = [term.lower() for term in terms if term.lower() not in STOPWORDS]
    return terms

def initialize_document_frequencies():
    global document_frequency
    for term in vocabulary:
        document_frequency[term] = len(postings[term])

def initialize_lengths():
    global length
    for index in df.index:
        l = 0
        for term in vocabulary:
            l += term_frequency(term, index) ** 2
        length[index] = math.sqrt(l)
    for index in df.index:
        if length[index] == 0:
            length[index] = 1.0

def term_frequency(term, index):
    if index in postings[term]:
        return postings[term][index]
    else:
        return 0.0

def inverse_document_frequency(term):
    if term in vocabulary:
        return math.log(N / document_frequency[term], 2)
    else:
        return 0.0

def do_search(query):
    query = tokenize(query)
    scores = sorted(
        [(index, similarity(query, index)) for index in df.index],
        key=lambda x: x[1],
        reverse=True,
    )
    threshold = 0.95
    results = [df.loc[index, 'Company Name'] for index, score in scores if score >= threshold]
    return results[:5]  # Return only the top 5 results

def similarity(query, index):
    similarity = 0.0
    for term in query:
        if term in vocabulary:
            similarity += term_frequency(term, index) * inverse_document_frequency(term)
    similarity = similarity / length[index]
    return similarity

def remove_special_characters(text):
    regex = re.compile(r"[^a-zA-Z0-9\s]")
    return re.sub(regex, "", text)

def remove_digits(text):
    regex = re.compile(r"\d")
    return re.sub(regex, "", text)

@app.route('/search', methods=['GET'])
def search():
    query = request.args.get('query')
    if not query:
        return jsonify({'error': 'Query parameter is missing'}), 400

    results = do_search(query)
    return jsonify({'results': results})

if __name__ == "__main__":
    initialize()
    app.run(host='0.0.0.0', port=5000)
