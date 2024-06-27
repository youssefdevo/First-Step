from flask import Flask, request, jsonify
import pandas as pd
import math
import re
from collections import defaultdict
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from flask_cors import CORS
from azure.storage.blob import BlobServiceClient
from io import StringIO

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

STOPWORDS = set(stopwords.words("english"))

# Connecting with private storage using connection_string.
connection_string = 'Your_Connection_String_Here'

# Initialize the BlobServiceClient
blob_service_client = BlobServiceClient.from_connection_string(connection_string)

# Define the container and blob name
container_name = 'projectsdata'
blob_name = 'updated_pitch_decks_dataset.csv'

# Get a blob client
blob_client = blob_service_client.get_blob_client(container=container_name, blob=blob_name)

# Initialize global variables
df = None  # Initialize as None initially

def initialize():
    """Initialize the search engine components."""
    global df
    blob_content = blob_client.download_blob().content_as_text()
    df = pd.read_csv(StringIO(blob_content), encoding='ISO-8859-1')
    initialize_terms_and_postings()
    initialize_document_frequencies()
    initialize_lengths()

def initialize_terms_and_postings():
    """Initialize terms and postings lists from the dataset."""
    global vocabulary, postings
    vocabulary = set()
    postings = defaultdict(dict)
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

def tokenize(document):
    """Tokenize the document into terms, excluding stopwords."""
    terms = word_tokenize(document)
    terms = [term.lower() for term in terms if term.lower() not in STOPWORDS]
    return terms

def initialize_document_frequencies():
    """Initialize document frequencies for terms."""
    global document_frequency
    document_frequency = defaultdict(int)
    for term in vocabulary:
        document_frequency[term] = len(postings[term])

def initialize_lengths():
    """Initialize document lengths for normalization."""
    global length
    length = defaultdict(float)
    for index in df.index:
        l = 0
        for term in vocabulary:
            l += term_frequency(term, index) ** 2
        length[index] = math.sqrt(l)
    for index in df.index:
        if length[index] == 0:
            length[index] = 1.0

def term_frequency(term, index):
    """Calculate term frequency in a document."""
    if index in postings[term]:
        return postings[term][index]
    else:
        return 0.0

def inverse_document_frequency(term):
    """Calculate inverse document frequency for a term."""
    if term in vocabulary:
        return math.log(N / document_frequency[term], 2)
    else:
        return 0.0

def do_search(query):
    """Perform a search for the query and return the top results."""
    query = tokenize(query)
    scores = sorted(
        [(index, similarity(query, index)) for index in df.index],
        key=lambda x: x[1],
        reverse=True,
    )
    threshold = 0.95  # Adjust this value as needed
    results = [int(df.loc[index, 'projectID']) for index, score in scores if score >= threshold]
    return results[:10]

def similarity(query, index):
    """Calculate the similarity between the query and a document."""
    similarity = 0.0
    for term in query:
        if term in vocabulary:
            similarity += term_frequency(term, index) * inverse_document_frequency(term)
    similarity = similarity / length[index]
    return similarity

def remove_special_characters(text):
    """Remove special characters from text."""
    regex = re.compile(r"[^a-zA-Z0-9\s]")
    return re.sub(regex, "", text)

def remove_digits(text):
    """Remove digits from text."""
    regex = re.compile(r"\d")
    return re.sub(regex, "", text)

@app.route('/search', methods=['GET'])
def search():
    """Handle search requests."""
    query = request.args.get('query')
    if not query:
        return jsonify({'error': 'Query parameter is missing'}), 400

    results = do_search(query)
    if not results:
        return jsonify({'results': 'Sorry, there are no projects matching your query.'})
    else:
        response = {"results": []}
        for project_id in results:
            response["results"].append(project_id)
    return jsonify(response)

@app.route('/reload', methods=['POST'])
def reload():
    """Handle reload request triggered by Azure Function or Event Grid."""
    try:
        initialize()
        return jsonify({'message': 'Data reloaded successfully'}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == "__main__":
    initialize()
    app.run(host='0.0.0.0', port=5000)
