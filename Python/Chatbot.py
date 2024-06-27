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
CORS(app)

STOPWORDS = set(stopwords.words("english"))


# Connecting with private storage using connection_string.
connection_string = 'DefaultEndpointsProtocol=https;AccountName=firststepdata;AccountKey=/1MX4Fc4Y9d7Bo94AMt2+CxMzMS/FgXMEOchPKHQnLvg9CB+Yh2C1/WMDU8BOrHUk5TI9Xf6gLbc+AStnxXlGw==;EndpointSuffix=core.windows.net'

# Initialize the BlobServiceClient
blob_service_client = BlobServiceClient.from_connection_string(connection_string)

# Define the container and blob name
container_name = 'projectsdata'
blob_name = 'updated_pitch_decks_dataset.csv'

# Get a blob client
blob_client = blob_service_client.get_blob_client(container=container_name, blob=blob_name)

# Download the blob content as text
blob_content = blob_client.download_blob().content_as_text()

# Load the CSV content into a DataFrame
df = pd.read_csv(StringIO(blob_content), encoding='ISO-8859-1')

# Initialize global variables
N = len(df)
vocabulary = set()
postings = defaultdict(dict)
document_frequency = defaultdict(int)
length = defaultdict(float)

def initialize():
    initialize_terms_and_postings()
    initialize_document_frequencies()
    initialize_lengths()

def initialize_terms_and_postings():
    global vocabulary, postings
    for index, row in df.iterrows():
        document = (
            str(row['Full Description']) + " " +
            str(row['Slogan']) + " " +
            str(row['Amount Raised']) + " " +
            str(row['Year']) + " " +
            str(row['Stage']) + " " +
            str(row['Business Model']) + " " +
            str(row['Investors']) + " " +
            str(row['About']) + " " +
            str(row['Industry']) + " " +
            str(row['Tags']) + " " +
            str(row['Customer Model']) + " " +
            str(row['Website']) + " " +
            str(row['Legal Name']) + " " +
            str(row['Type'])
        )
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
    scores = [(index, similarity(query, index)) for index in df.index]
    scores.sort(key=lambda x: x[1], reverse=True)  # Sort scores in descending order

    threshold = 0.85  # Adjust this value as needed
    results = [int(df.loc[index, 'projectID']) for index, score in scores if score >= threshold]
    return results[:5]  # Return only the top 5 results

def similarity(query, index):
    similarity = 0.0
    for term in query:
        if term in vocabulary:
            tf = term_frequency(term, index)
            idf = inverse_document_frequency(term)
            similarity += (tf * idf)
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
    if not results:
        return jsonify({'results': 'Sorry, there are no projects matching your query.'})
    else:
        response = {"results": []}
        for project_id in results:
            response["results"].append(project_id)
    return jsonify(response)

if __name__ == "__main__":
    initialize()
    app.run(host='0.0.0.0', port=5000)
