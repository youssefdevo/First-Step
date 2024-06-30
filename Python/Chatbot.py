from flask import Flask, request, jsonify  # Import Flask modules to create web applications and handle HTTP requests and responses
import pandas as pd  # Import Pandas for data manipulation and analysis
import math  # Import math module for mathematical functions
import re  # Import regular expressions module for string operations
import nltk  # Import NLTK for NLP

from collections import defaultdict  # Import defaultdict for default dictionary values
from nltk.corpus import stopwords  # Import stopwords from NLTK to remove common words
from nltk.tokenize import word_tokenize  # Import word_tokenize from NLTK to split text into words
from flask_cors import CORS  # Import CORS from Flask to handle Cross-Origin Resource Sharing
from azure.storage.blob import BlobServiceClient  # Import BlobServiceClient to interact with Azure Blob storage
from io import StringIO  # Import StringIO to handle in-memory file objects
import schedule  # Import schedule library for scheduling tasks
import threading  # Import threading to run the scheduler in a separate thread
import time  # Import time for time-related


app = Flask(__name__)  # Create a Flask application instance
CORS(app)  # Enable CORS for the Flask application
nltk.download('stopwords')  # Download the stopwords dataset from NLTK
nltk.download('punkt')  # Download the punkt tokenizer models from NLTK
STOPWORDS = set(stopwords.words("english"))  # Set of English stopwords

# Azure Blob storage connection string
connection_string = 'DefaultEndpointsProtocol=https;AccountName=firststepdata;AccountKey=/1MX4Fc4Y9d7Bo94AMt2+CxMzMS/FgXMEOchPKHQnLvg9CB+Yh2C1/WMDU8BOrHUk5TI9Xf6gLbc+AStnxXlGw==;EndpointSuffix=core.windows.net'
# Initialize the BlobServiceClient using the connection string
blob_service_client = BlobServiceClient.from_connection_string(connection_string)

# Define the container and blob name for the data
container_name = 'projectsdata'
blob_name = 'updated_pitch_decks_dataset.csv'

# Get a blob client to interact with the specified blob
blob_client = blob_service_client.get_blob_client(container=container_name, blob=blob_name)

try:
    # Download the blob content as text
    blob_content = blob_client.download_blob().content_as_text()
    # Load the CSV content into a Pandas DataFrame
    df = pd.read_csv(StringIO(blob_content), encoding='ISO-8859-1')
    print(df.columns)  # Debug statement to print column names
except Exception as e:
    # Handle exceptions during data loading
    df = pd.DataFrame()  # Assign an empty DataFrame if there's an error

# Initialize global variables for the application
N = len(df)  # Number of documents
vocabulary = set()  # Set of all unique terms in the documents
postings = defaultdict(dict)  # Dictionary to store term postings
document_frequency = defaultdict(int)  # Dictionary to store document frequency of terms
length = defaultdict(float)  # Dictionary to store document lengths

# Function to remove special characters from text
def remove_special_characters(text):
    regex = re.compile(r"[^a-zA-Z0-9\s]")
    return re.sub(regex, "", text)

# Function to remove digits from text
def remove_digits(text):
    regex = re.compile(r"\d")
    return re.sub(regex, "", text)

# Function to initialize the indexing process
def initialize():
    global df, N, vocabulary, postings, document_frequency, length
    try:
        # Download the blob content as text
        blob_content = blob_client.download_blob().content_as_text()
        # Load the CSV content into a Pandas DataFrame
        df = pd.read_csv(StringIO(blob_content), encoding='ISO-8859-1')
        print("Data reloaded at:", time.ctime())  # Debug statement to print reload time
    except Exception as e:
        df = pd.DataFrame()  # Assign an empty DataFrame if there's an error
    N = len(df)
    vocabulary = set()
    postings = defaultdict(dict)
    document_frequency = defaultdict(int)
    length = defaultdict(float)
    initialize_terms_and_postings()
    initialize_document_frequencies()
    initialize_lengths()
# Function to initialize terms and their postings
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
            continue  # Skip non-string documents
        document = remove_special_characters(document)  # Remove special characters
        document = remove_digits(document)  # Remove digits
        terms = tokenize(document)  # Tokenize the document
        unique_terms = set(terms)  # Get unique terms in the document
        vocabulary = vocabulary.union(unique_terms)  # Add unique terms to the vocabulary
        for term in unique_terms:
            postings[term][index] = terms.count(term)  # Store term frequency for each document

# Function to tokenize a document
def tokenize(document):
    terms = word_tokenize(document)  # Split document into words
    terms = [term.lower() for term in terms if term.lower() not in STOPWORDS]  # Remove stopwords and convert to lowercase
    return terms

# Function to initialize document frequencies
def initialize_document_frequencies():
    global document_frequency
    for term in vocabulary:
        document_frequency[term] = len(postings[term])  # Count number of documents containing each term

# Function to initialize document lengths
def initialize_lengths():
    global length
    for index in df.index:
        l = 0
        for term in vocabulary:
            l += term_frequency(term, index) ** 2  # Calculate squared term frequency
        length[index] = math.sqrt(l)  # Calculate document length (Euclidean norm)
    for index in df.index:
        if length[index] == 0:
            length[index] = 1.0  # Avoid division by zero

# Function to calculate term frequency
def term_frequency(term, index):
    if index in postings[term]:
        return postings[term][index]
    else:
        return 0.0

# Function to calculate inverse document frequency
def inverse_document_frequency(term):
    if term in vocabulary:
        return math.log(N / document_frequency[term], 2)  # Calculate IDF using log base 2
    else:
        return 0.0

# Function to perform search on the documents
def do_search(query):
    query = tokenize(query)  # Tokenize the search query
    scores = [(index, similarity(query, index)) for index in df.index]  # Calculate similarity scores for all documents
    scores.sort(key=lambda x: x[1], reverse=True)  # Sort scores in descending order

    threshold = 0.85  # Set a similarity threshold
    results = [int(df.loc[index, 'projectID']) for index, score in scores if score >= threshold]
    return results[:5]  # Return only the top 5 results

# Function to calculate similarity between a query and a document
def similarity(query, index):
    similarity = 0.0
    for term in query:
        if term in vocabulary:
            tf = term_frequency(term, index)
            idf = inverse_document_frequency(term)
            similarity += (tf * idf)  # Calculate weighted term frequency
    similarity = similarity / length[index]  # Normalize by document length
    return similarity

# Define the default route
@app.route('/')
def home():
    return "Hello, this is the default route!"

# Define the search route
@app.route('/search', methods=['GET'])
def search():
    query = request.args.get('query')
    if not query:
        return jsonify({'error': 'Query parameter is missing'}), 400  # Return error if query is missing

    results = do_search(query)  # Perform search
    return jsonify({'results': results})  # Return search results as JSON


# Scheduler to reload data every 60 minutes
def run_scheduler():
    schedule.every(60).minutes.do(initialize)
    while True:
        schedule.run_pending()
        time.sleep(1)

# Initialize the application
initialize()

# Start the scheduler in a separate thread
scheduler_thread = threading.Thread(target=run_scheduler)
scheduler_thread.start()

# Uncomment the following lines to run the application locally
#if __name__ == "__main__":
#    initialize()
#    app.run(host='0.0.0.0', port=5000)